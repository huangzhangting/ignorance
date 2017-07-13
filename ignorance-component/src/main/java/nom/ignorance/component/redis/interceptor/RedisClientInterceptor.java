package nom.ignorance.component.redis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by huangzhangting on 17/3/31.
 */
@Slf4j
public class RedisClientInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        }catch (Exception e){
            return exceptionProcessor(invocation, e);
        }
    }

    /**
     * 异常处理
     * @param invocation
     * @param e
     * @return
     */
    private Object exceptionProcessor(MethodInvocation invocation, Exception e){
        log.error("access redis error", e);
        return null;
    }

}
