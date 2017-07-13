package nom.ignorance.component.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by huangzhangting on 17/3/31.
 */
public interface RedisDataSource {
    /**
     * 获取redis资源
     */
    ShardedJedis getResource();

    /**
     * 返还redis资源
     */
    void returnResource(ShardedJedis shardedJedis);

}
