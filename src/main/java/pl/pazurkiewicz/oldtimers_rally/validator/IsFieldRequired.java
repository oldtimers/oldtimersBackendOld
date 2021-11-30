package pl.pazurkiewicz.oldtimers_rally.validator;


import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;

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

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsFieldRequiredValidator implements ConstraintValidator<IsFieldRequired, Dictionary> {

        @Override
        public boolean isValid(Dictionary value, ConstraintValidatorContext context) {
            if (Boolean.TRUE.equals(value.getEventLanguage().getIsDefault()) && (value.getValue() == null || value.getValue().isBlank())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("value").addConstraintViolation();
                return false;
            } else {
                return true;
            }
        }
    }
}


