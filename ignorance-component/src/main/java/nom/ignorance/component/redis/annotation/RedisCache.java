package nom.ignorance.component.redis.annotation;

import java.lang.annotation.*;

/**
 * Created by huangzhangting on 17/4/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisCache {
    String key();

    int expire() default 3600; //默认1小时
}
