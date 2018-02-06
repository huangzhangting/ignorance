package nom.ignorance.component.utils;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class ValidatorUtil {
    private static final ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(
                    new ResourceBundleMessageInterpolator(
//                            new PlatformResourceBundleLocator( "UserMessage" )
                            new AggregateResourceBundleLocator(
                                    Arrays.asList(
//                                            ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, //默认配置文件
                                            "UserValidationMessages",
                                            "OtherValidationMessages"
                                    )
                            )
                    )
            )
            .buildValidatorFactory();
    private static final Validator validator = factory.getValidator();
    private static final ExecutableValidator executableValidator = validator.forExecutables();

    /**
     * 校验方法参数
     * @param obj method所属对象
     * @param method 目标方法
     * @param params 方法参数
     * @param <T>
     * @return
     */
    public static <T> Set<ConstraintViolation<T>> validateMethodParams(T obj, Method method, Object[] params){
        return executableValidator.validateParameters(obj, method, params);
    }

    /**
     * 校验 bean
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Set<ConstraintViolation<T>> validateEntity(T obj) {
        return validator.validate(obj);
    }

}
