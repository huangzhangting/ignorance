package nom.ignorance.component.redis;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.component.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.*;

/**
 * Created by huangzhangting on 17/3/31.
 */
@Slf4j
@Component
public class RedisClientImpl implements RedisClient {
    //永久缓存失效时间：5年
    private static final int FOREVER_CACHE_TIME = 3600*24*365*5;

    @Autowired
    private RedisDataSource redisDataSource;


    @Override
    public String setNoData(String key) {
        return setNoData(key, RedisKeys.EXPIRE_TEN_MINUTES);
    }

    @Override
    public String setNoData(String key, int expire) {
        return set(key, RedisKeys.NO_DATA, expire);
    }

    @Override
    public boolean isNoData(String value) {
        return RedisKeys.NO_DATA.equals(value);
    }

    @Override
    public String lazySet(String key, Object value, int expire) {
        String jsonStr = JsonUtil.toJson(value);
        return set(key, jsonStr, expire);
    }

    @Override
    public <T> T lazyGet(String key, Class<T> tClass) {
        String jsonStr = get(key);
        if(jsonStr==null){
            return null;
        }
        return JsonUtil.fromJson(jsonStr, tClass);
    }

    @Override
    public <T> List<T> lazyGetList(String key, Class<T> tClass) {
        String jsonStr = get(key);
        if(jsonStr==null){
            return new ArrayList<>();
        }
        return JsonUtil.strToList(jsonStr, tClass);
    }

    @Override
    public String set(String key, String value, int expire) {
        if(StringUtils.isEmpty(key) || value==null || expire<0){
            return null;
        }
        ShardedJedis jedis = redisDataSource.getResource();
        if(jedis==null){
            return null;
        }
        if(expire==0){
            expire = FOREVER_CACHE_TIME;
        }
        try {
            return jedis.setex(key, expire, value);
        }catch (Exception e){
            log.error("redis setex error, key="+key+", value="+value, e);
        }finally {
            redisDataSource.returnResource(jedis);
        }
        return null;
    }

    @Override
    public String get(String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        ShardedJedis jedis = redisDataSource.getResource();
        if(jedis==null){
            return null;
        }
        try {
            return jedis.get(key);
        }catch (Exception e){
            log.error("redis get error, key="+key, e);
        }finally {
            redisDataSource.returnResource(jedis);
        }
        return null;
    }

    @Override
    public Long incr(String key) {
        ShardedJedis jedis = redisDataSource.getResource();
        if(jedis==null){
            return null;
        }
        try {
            return jedis.incr(key);
        }catch (Exception e){
            log.error("redis incr error, key="+key, e);
        }finally {
            redisDataSource.returnResource(jedis);
        }
        return null;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        Set<String> set = new HashSet<>();
        if(StringUtils.isEmpty(pattern)){
            return set;
        }
        ShardedJedis shardedJedis = redisDataSource.getResource();
        if(shardedJedis==null){
            return set;
        }
        try {
            Collection<Jedis> jedisCollection = shardedJedis.getAllShards();
            if(CollectionUtils.isEmpty(jedisCollection)){
                return set;
            }
            for (Jedis jedis : jedisCollection) {
                set.addAll(jedis.keys(pattern));
            }
        }catch (Exception e){
            log.error("redis get keys error, pattern="+pattern, e);
        }

        return set;
    }

    @Override
    public Long delKey(String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        ShardedJedis jedis = redisDataSource.getResource();
        if(jedis==null){
            return null;
        }
        try {
            return jedis.del(key);
        }catch (Exception e){
            log.error("redis del key error, key="+key, e);
        }
        return null;
    }

    @Override
    public Long delKeys(Set<String> keys) {
        if(CollectionUtils.isEmpty(keys)){
            return null;
        }
        ShardedJedis shardedJedis = redisDataSource.getResource();
        if(shardedJedis==null){
            return null;
        }
        try {
            Collection<Jedis> jedisCollection = shardedJedis.getAllShards();
            if(CollectionUtils.isEmpty(jedisCollection)){
                return null;
            }
            long result = 0;
            String[] strings = keys.toArray(new String[keys.size()]);
            for (Jedis jedis : jedisCollection) {
                result += jedis.del(strings);
            }
            return result;
        }catch (Exception e){
            log.error("redis del keys error, keys="+keys, e);
        }
        return null;
    }

}
