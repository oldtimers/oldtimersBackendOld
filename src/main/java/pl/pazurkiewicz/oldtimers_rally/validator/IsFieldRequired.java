package pl.pazurkiewicz.oldtimers_rally.validator;


import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsFieldRequired.IsFieldRequiredValidator.class)
public @interface IsFieldRequired {
    String message() default "{dictionary.required}";

    String field();

    String isRequired();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsFieldRequiredValidator implements ConstraintValidator<IsFieldRequired, Object> {
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
            try {
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
            } catch (NullValueInNestedPathException e) {
                return true;
            }
        }
    }
}

