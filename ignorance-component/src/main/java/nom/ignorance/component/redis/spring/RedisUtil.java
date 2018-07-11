package nom.ignorance.component.redis.spring;

import com.alibaba.fastjson.JSON;
import nom.ignorance.component.utils.SpringContextUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 对spring提供的 StringRedisTemplate 进行简单的封装
 *
 */
public class RedisUtil {
    private static final StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
    private static final long DEFAULT_EXPIRE = 10; //默认缓存时间，单位：分钟


    public static <T> List<T> getListFromJson(String key, Class<T> cla){
        if(key == null || cla == null){
            return null;
        }
        String redisStr = get(key);
        if(redisStr == null){
            return null;
        }
        return JSON.parseArray(redisStr, cla);
    }

    public static <T> T getFromJson(String key, Class<T> cla){
        if(key == null || cla == null){
            return null;
        }
        String redisStr = get(key);
        if(redisStr == null){
            return null;
        }
        return JSON.parseObject(redisStr, cla);
    }

    public static String get(String key){
        if(key == null){
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 以json串形式缓存
     * @param key
     * @param val
     * @param expire 缓存时间，单位：分钟
     */
    public static void setJson(String key, Object val, long expire){
        set(key, JSON.toJSONString(val), expire);
    }

    public static void setJson(String key, Object val){
        setJson(key, val, DEFAULT_EXPIRE);
    }

    public static void set(String key, String val, long expire){
        redisTemplate.boundValueOps(key).set(val, expire, TimeUnit.MINUTES);
    }

    public static void set(String key, String val){
        set(key, val, DEFAULT_EXPIRE);
    }

}
