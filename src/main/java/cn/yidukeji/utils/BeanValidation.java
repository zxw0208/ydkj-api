package cn.yidukeji.utils;

import cn.yidukeji.exception.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class BeanValidation {

    public static void validate(Object object) throws ApiException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(object);

        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            throw new ApiException(constraintViolation.getMessage() + " [" + constraintViolation.getPropertyPath() + "]", 400);
        }

    }

}
