package nom.ignorance.component.redis.interceptor;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.component.redis.RedisClient;
import nom.ignorance.component.redis.annotation.RedisCache;
import nom.ignorance.component.redis.annotation.RedisKeyParam;
import nom.ignorance.component.utils.JsonUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by huangzhangting on 17/4/1.
 */
@Slf4j
public class RedisCacheInterceptor implements MethodInterceptor {
    //spring提供的软引用map
    private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache =
            new ConcurrentReferenceHashMap<AnnotationCacheKey, Annotation>(256);

    @Autowired
    private RedisClient redisClient;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        RedisCache redisCache = getAnnotation(RedisCache.class, method, invocation.getThis());
        if(redisCache==null){
            return invocation.proceed();
        }
        String key = getKey(invocation, redisCache.key());
        if(StringUtils.isEmpty(key)){
            return invocation.proceed();
        }
        //从缓存获取数据
        Object object;
        String redisStr = redisClient.get(key);
        if(redisStr!=null){
            if(redisClient.isNoData(redisStr)){
                return null;
            }
            object = parseCache(method, redisStr);
            if(object!=null){
                return object;
            }
        }
        //获取实际数据
        object = invocation.proceed();
        //设置缓存
        setCache(key, object, redisCache.expire());
        return object;
    }

    //获取注解
    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotation(Class<T> annotationClass, Method method, Object target){
        AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationClass);
        T annotation = (T)findAnnotationCache.get(cacheKey);
        if(annotation != null){
            return annotation;
        }
        annotation = method.getAnnotation(annotationClass);
        if(annotation == null) {
            try {
                Class<?> targetClass = target.getClass();
                //jdk代理时，如果注解用在实现类方法上
                if (method.getDeclaringClass().isInterface()) {
                    Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
                    annotation = m.getAnnotation(annotationClass);
                }else {
                    //cglib代理时，如果注解用在接口方法上
                    annotation = findOnInterfaces(annotationClass, method, targetClass.getInterfaces());
                }
            } catch (Exception e) {
                log.error("get annotation error, annotationClass="+annotationClass.getName(), e);
            }
        }
        if(annotation != null){
            //annotation = synthesizeAnnotation(annotation, method); //spring AnnotationUtils 中的代码，有待研究，起什么作用
            findAnnotationCache.put(cacheKey, annotation);
        }
        return annotation;
    }
    //从接口上获取注解
    private <T extends Annotation> T findOnInterfaces(Class<T> annotationClass, Method method, Class<?>[] interfaces){
        int len = interfaces.length;
        String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        T annotation = null;
        for(int i=0; i<len; i++){
            try {
                Method m = interfaces[i].getMethod(methodName, paramTypes);
                annotation = m.getAnnotation(annotationClass);
                if(annotation != null){
                    break;
                }
            }catch (NoSuchMethodException e){
                //接口中没有方法，不做处理
            }
        }
        return annotation;
    }

    //组装key
    private String getKey(MethodInvocation invocation, String key){
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        Object[] arguments = invocation.getArguments();
        Method method = invocation.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int len = parameterAnnotations.length;
        for(int i=0; i<len; i++){
            Annotation[] annotations = parameterAnnotations[i];
            for(Annotation annotation : annotations){
                if(annotation instanceof RedisKeyParam){
                    sb.append("_").append(arguments[i]);
                }
            }
        }
        return sb.toString();
    }

    //解析缓存数据
    private Object parseCache(Method method, String redisStr){
        Class rType = method.getReturnType();
        return JsonUtil.fromJson(redisStr, rType);
    }

    private void setCache(String key, Object object, int expire){
        if(object==null){
            redisClient.setNoData(key);
        }else {
            redisClient.lazySet(key, object, expire);
        }
    }


    //下面代码，参考自spring的 AnnotationUtils
    /**
     * Cache key for the AnnotatedElement cache.
     */
    private static class AnnotationCacheKey {

        private final AnnotatedElement element;

        private final Class<? extends Annotation> annotationType;

        public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
            this.element = element;
            this.annotationType = annotationType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AnnotationCacheKey)) {
                return false;
            }
            AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.annotationType.hashCode());
        }
    }

}
