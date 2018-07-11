package nom.ignorance.component.redis.spring.lock;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.component.utils.SpringContextUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLock {
    private static final StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);


    /**
     * 加锁，并执行业务代码，自动释放锁
     *
     * @param lockKey 锁redis key
     * @param expire 锁超时时间，单位：秒
     * @param waitTime 获取锁等待时间，单位：秒
     * @param callback 回调方法，执行具体的业务逻辑
     * @return 如果等待超时，会抛出 LockWaitTimeOutException，调用方自行捕获处理
     */
    public static <T> T lock(String lockKey, long expire, long waitTime,
                             RedisLockCallback<T> callback) throws LockWaitTimeOutException{
        if(lockKey==null || expire <= 0 || waitTime <= 0){
            throw new IllegalArgumentException("非法参数");
        }

        //获取锁，要么成功，要么抛出等待超时异常
        String lock = waitGetLock(lockKey, expire, waitTime);
        try {
            //执行业务逻辑
            return callback.execute();
        }finally {
            unlock(lockKey, lock);
        }
    }

    private static String waitGetLock(String lockKey, long expire, long waitTime)
            throws LockWaitTimeOutException{
        long waitTimeOut = System.currentTimeMillis() + waitTime * 1000;
        do {
            String lock = getLock(lockKey, expire);
            if(lock != null){
                return lock;
            }
        } while (continueWait(waitTimeOut));
        throw new LockWaitTimeOutException("获取redis锁, 等待超时, 等待时间:" + waitTime + "s, lockKey:" + lockKey);
    }
    private static boolean continueWait(long waitTimeOut){
        if(waitTimeOut <= System.currentTimeMillis()){
            return false;
        }
        try {
            Thread.sleep(5);
        }catch (Exception e){
            //ignore
        }
        return true;
    }

    /**
     * 获取锁
     * @param lockKey
     * @param expire 锁超时时间，单位：秒
     * @return
     */
    private static String getLock(String lockKey, long expire){
        String lock = UUID.randomUUID().toString();
        try {
            if(redisTemplate.opsForValue().setIfAbsent(lockKey, lock)){
                //这个如果设置失败，有没有可能不抛出异常？
                redisTemplate.expire(lockKey, expire, TimeUnit.SECONDS);
                log.info("===== get lock, key:{} lock:{} =====", lockKey, lock);
                return lock;
            }
        }catch (Exception e){
            unlock(lockKey, lock); //出现异常，释放自己获得的锁
            throw e;
        }

        /*
        TTL key 指令
        从Redis2.8开始，错误返回值的结果有如下改变：
        如果key不存在或者已过期，返回 -2
        如果key存在并且没有设置过期时间（永久有效），返回 -1 。
        * */
        Long et = redisTemplate.getExpire(lockKey);
        if(et == -1){
            // 上次获取锁出息异常，导致没设置过期时间，且没有释放掉
            log.warn("上次获取redis锁, 未设置过期时间, lockKey:{}", lockKey);
            redisTemplate.expire(lockKey, 500, TimeUnit.MILLISECONDS); //不直接删除
        }
        return null;
    }


    /**
     * 释放锁
     * @param lockKey
     * @param lock 避免业务执行时间过长，实际上锁已超时，其他线程已经获取锁，这时老的线程不能释放lockKey
     * @return
     */
    private static boolean unlock(String lockKey, String lock){
        String val = redisTemplate.opsForValue().get(lockKey);
        if(val == null){
            return true;
        }
        log.info("===== unlock key:{} lock:{} redisVal:{} =====", lockKey, lock, val);
        if(val.equals(lock)){
            redisTemplate.delete(lockKey);
            return true;
        }
        return false;
    }

}
