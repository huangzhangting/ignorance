package nom.ignorance.component.redis;

import java.util.List;
import java.util.Set;

/**
 * Created by huangzhangting on 17/3/31.
 */
public interface RedisClient {
    /**
     * 设置无数据标识，默认缓存10分钟
     * @param key
     * @return
     */
    String setNoData(String key);

    String setNoData(String key, int expire);

    boolean isNoData(String value);

    /**
     * 对象转换成json字符串，进行缓存
     * @param key
     * @param value
     * @param expire 过期时间，0：永久缓存
     * @return
     */
    String lazySet(String key, Object value, int expire);

    /**
     * 获取缓存数据，并转换成对象
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T lazyGet(String key, Class<T> tClass);

    /**
     * 获取缓存数据，并转换成集合对象
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    <T> List<T> lazyGetList(String key, Class<T> tClass);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @param expire 过期时间，0：永久缓存
     * @return
     */
    String set(String key, String value, int expire);

    /**
     * 根据key获取缓存值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 执行原子加 1 操作
     * @param key
     * @return
     */
    Long incr(String key);

    /**
     * 根据表达式获得key集合
     * @param pattern 例如：sigma_*
     * @return
     */
    Set<String> getKeys(String pattern);

    /**
     * 删除key
     * @param key
     * @return
     */
    Long delKey(String key);

    /**
     * 删除key集合
     * @param keys
     * @return
     */
    Long delKeys(Set<String> keys);

}
