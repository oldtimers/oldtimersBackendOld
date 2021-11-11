package pl.pazurkiewicz.oldtimers_rally.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsFieldRequiredValidator implements ConstraintValidator<IsFieldRequired, Object> {
    private String field;
    private String isRequired;

    @Override
    public void initialize(IsFieldRequired constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.isRequired = constraintAnnotation.isRequired();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String fieldValue = (String) new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Boolean isRequiredValue = (Boolean) new BeanWrapperImpl(value)
                .getPropertyValue(isRequired);

        if (Boolean.TRUE.equals(isRequiredValue) && (fieldValue == null || fieldValue.isBlank())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(field).addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}
