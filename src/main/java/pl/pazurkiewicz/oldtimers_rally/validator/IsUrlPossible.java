package pl.pazurkiewicz.oldtimers_rally.validator;


import org.springframework.beans.factory.annotation.Autowired;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;

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
@Constraint(validatedBy = IsUrlPossible.IsUrlPossibleValidator.class)
@Documented
public @interface IsUrlPossible {
    String message() default "{event.urlForbidden}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsUrlPossibleValidator implements ConstraintValidator<IsUrlPossible, String> {

        @Autowired
        EventRepository repository;

        @Override
        public void initialize(IsUrlPossible constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value.matches(MyConfigurationProperties.eventRegex);
        }
    }
}
