package nom.ignorance.component.redis.lock;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.component.redis.RedisDataSource;
import nom.ignorance.component.redis.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by huangzhangting on 17/4/17.
 */
@Slf4j
@Component
public class RedisLock {
    public static final String LOCK_PREFIX = RedisKeys.PREFIX+"lock_pre_"; //redis锁前缀，方便后续排查问题
    private static final int WAIT_TIME = 60*1000; //获得锁的等待时间，单位：毫秒
    private static final int EXPIRE_TIME = 600*1000; //锁失效时间，单位：毫秒

    @Autowired
    private RedisDataSource redisDataSource;


    public <T> T execute(String lockKey, RedisLockCallback<T> callback){
        ShardedJedis shardedJedis = redisDataSource.getResource();
        Assert.notNull(shardedJedis, "RedisLock获取redis资源失败");
        lockKey = LOCK_PREFIX + lockKey;
        String expireTimeMillis = null;
        try {
            // 1、获取锁
            expireTimeMillis = waitGetLock(lockKey, shardedJedis);
            // 2、执行业务代码
            return callback.callback();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(), e.getCause());
        }finally {
            // 3、释放锁
            unlock(lockKey, shardedJedis, expireTimeMillis);
            // 释放redis
            redisDataSource.returnResource(shardedJedis);
        }
    }

    //获得锁
    private String waitGetLock(String lockKey, ShardedJedis shardedJedis) throws Exception{
        long waitTimeMillis = System.currentTimeMillis() + WAIT_TIME;
        //过期时间
        String expireTimeMillis;
        do{
            expireTimeMillis = getLock(lockKey, shardedJedis);
        }while (continueWait(expireTimeMillis, waitTimeMillis, lockKey));
        return expireTimeMillis;
    }
    //判断是否继续等待
    private boolean continueWait(String expireTimeMillis, long waitTimeMillis, String lockKey) throws Exception{
        if(expireTimeMillis!=null){
            return false;
        }
        if(System.currentTimeMillis()>=waitTimeMillis){
            throw new RuntimeException("获取redis锁, 等待超时, 等待时间:"+WAIT_TIME+" lockKey:"+lockKey);
        }
        Thread.sleep(100);
//        System.out.println("========== continueWait ==========");
        return true;
    }
    private String getExpireTimeMillis(){
        return Long.toString(System.currentTimeMillis() + EXPIRE_TIME);
    }
    //获得锁
    private String getLock(String lockKey, ShardedJedis shardedJedis){
        String expireTimeMillis = getExpireTimeMillis();
        //获取锁成功
        if(shardedJedis.setnx(lockKey, expireTimeMillis)==1){
            return expireTimeMillis;
        }
        //判断之前的值是否超时
        String timeStr = shardedJedis.get(lockKey);
        if(timeStr==null){ //恰好锁被释放
            timeStr = shardedJedis.getSet(lockKey, expireTimeMillis);
            if(timeStr==null){
                return expireTimeMillis;
            }
            //不为null，表示被其他线程，优先设值成功了，所以继续等待
            return null;
        }
        //如果已超时
//        System.out.println(System.currentTimeMillis());
        if(Long.parseLong(timeStr) <= System.currentTimeMillis()){
            expireTimeMillis = getExpireTimeMillis();
            String timeStr2 = shardedJedis.getSet(lockKey, expireTimeMillis);
            if(timeStr2==null){ //恰好锁被释放
                return expireTimeMillis;
            }
            if(timeStr.equals(timeStr2)){ //未被其他线程设值
                return expireTimeMillis;
            }
        }
        //继续等待
        return null;
    }

    //释放锁
    private void unlock(String lockKey, ShardedJedis shardedJedis, String expireTimeMillis){
        if(expireTimeMillis==null){
            return;
        }
        String timeStr = shardedJedis.get(lockKey);
        if(timeStr==null){
            return;
        }
        long diffTime = Long.parseLong(timeStr) - Long.parseLong(expireTimeMillis);
        if(diffTime < EXPIRE_TIME){
            shardedJedis.del(lockKey);
        }else{
            //如果超时了，打印日志
            log.error("redis lock time out, init expireTime:{}, lockKey:{}, overtime:{}", EXPIRE_TIME, lockKey, diffTime);
        }
    }

}
