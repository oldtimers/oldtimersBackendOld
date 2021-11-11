package pl.pazurkiewicz.oldtimers_rally.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsFieldRequiredValidator.class)
public @interface IsFieldRequired {
    String message() default "{dictionary.required}";

    String field();

    String isRequired();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
