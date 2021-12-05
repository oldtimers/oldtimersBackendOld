package pl.pazurkiewicz.oldtimers_rally.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsValueMatch.FieldsValueMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {

    String message() default "{fieldValue.invalid}";

    String field();

    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }


    class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

        private String field;
        private String fieldMatch;

        public void initialize(FieldsValueMatch constraintAnnotation) {
            this.field = constraintAnnotation.field();
            this.fieldMatch = constraintAnnotation.fieldMatch();
        }

        public boolean isValid(Object value,
                               ConstraintValidatorContext context) {
            Object fieldValue = new BeanWrapperImpl(value)
                    .getPropertyValue(field);
            Object fieldMatchValue = new BeanWrapperImpl(value)
                    .getPropertyValue(fieldMatch);
            boolean result;
            if (fieldValue != null) {
                result = fieldValue.equals(fieldMatchValue);
            } else {
                result = fieldMatchValue == null;
            }
            if (!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(fieldMatch).addConstraintViolation();
            }
            return result;
        }
    }
}
