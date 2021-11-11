package pl.pazurkiewicz.oldtimers_rally.validator;


import org.springframework.beans.factory.annotation.Autowired;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUrlAvailable.IsUrlAvailableValidator.class)
@Documented
public @interface IsUrlAvailable {
    String message() default "{event.invalidUrl}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsUrlAvailableValidator implements ConstraintValidator<IsUrlAvailable, String> {
        @Autowired
        EventRepository repository;

        @Override
        public void initialize(IsUrlAvailable constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !repository.existsEventByUrl(value);
        }
    }
}
