package nom.ignorance.component.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by huangzhangting on 17/3/31.
 */
@Slf4j
@Component
public class RedisDataSourceImpl implements RedisDataSource {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public ShardedJedis getResource() {
        try {
            return shardedJedisPool.getResource();
        }catch (Exception e){
            log.error("get redis resource error", e);
        }
        return null;
    }

    @Override
    public void returnResource(ShardedJedis shardedJedis) {
        if(shardedJedis != null) {
            shardedJedis.close();
        }
    }

}
