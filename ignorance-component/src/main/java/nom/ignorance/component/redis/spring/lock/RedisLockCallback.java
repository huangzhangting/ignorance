package nom.ignorance.component.redis.spring.lock;

public interface RedisLockCallback<T> {
    T execute();
}
