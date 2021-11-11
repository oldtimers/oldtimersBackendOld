package pl.pazurkiewicz.oldtimers_rally.validator;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.parameters.P;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Instant;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsEndDateValid.IsEndDateValidValidator.class)
public @interface IsEndDateValid {
    String message() default "End date is invalid";

    String starDate();

    String endDate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsEndDateValidValidator implements ConstraintValidator<IsEndDateValid, Object> {

        private String startDate;
        private String endDate;
        @Override
        public void initialize(IsEndDateValid constraintAnnotation) {
            this.startDate = constraintAnnotation.starDate();
            this.endDate = constraintAnnotation.endDate();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            Instant startDateValue = (Instant) new BeanWrapperImpl(value)
                    .getPropertyValue(startDate);
            Instant endDateValue = (Instant) new BeanWrapperImpl(value)
                    .getPropertyValue(endDate);
            if (startDateValue != null && endDateValue != null && startDateValue.isBefore(endDateValue)){
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(endDate).addConstraintViolation();
                return false;
            }
        }
    }
}
