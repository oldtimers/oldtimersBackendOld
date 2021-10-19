package pl.pazurkiewicz.oldtimers_rally.models.dictionary;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsRequiredValidator.class)
@Documented
public @interface IsRequired {
    String message() default "#{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
