package pl.pazurkiewicz.oldtimers_rally.validator;


import org.springframework.beans.factory.annotation.Autowired;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUrlPossible.IsUrlPossibleValidator.class)
@Documented
public @interface IsUrlPossible {
    String message() default "This URL is forbidden";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsUrlPossibleValidator implements ConstraintValidator<IsUrlPossible, String> {
        private static final Set<String> forbiddenNames = new HashSet<>(Arrays.asList("rally", "rally", "rally", "photos", "static"));

        @Autowired
        EventRepository repository;

        @Override
        public void initialize(IsUrlPossible constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !forbiddenNames.contains(value);
        }
    }
}
