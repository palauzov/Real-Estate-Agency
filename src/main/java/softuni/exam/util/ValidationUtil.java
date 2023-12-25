package softuni.exam.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import javax.validation.Validation;

@Component
public class ValidationUtil {
    private final javax.validation.Validator validator;


    public ValidationUtil(Validator validator) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).isEmpty();
    }
}
