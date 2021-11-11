package pl.pazurkiewicz.oldtimers_rally.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUrlAvailableValidator.class)
@Documented
public @interface IsUrlAvailable {
    String message() default "{event.invalidUrl}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
