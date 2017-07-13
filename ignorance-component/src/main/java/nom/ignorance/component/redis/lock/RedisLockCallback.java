package nom.ignorance.component.redis.lock;

/**
 * Created by huangzhangting on 17/4/17.
 */
public interface RedisLockCallback<T> {
    T callback();
}
