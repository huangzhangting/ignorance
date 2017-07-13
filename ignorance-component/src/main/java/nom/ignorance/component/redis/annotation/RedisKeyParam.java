package nom.ignorance.component.redis.annotation;

import java.lang.annotation.*;

/**
 * Created by huangzhangting on 17/4/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface RedisKeyParam {
}
