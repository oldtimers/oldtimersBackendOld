package pl.pazurkiewicz.oldtimers_rally.validator;

import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CategoryEnum;

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
@Constraint(validatedBy = AreYearsTyped.AreYearsTypedValidator.class)
public @interface AreYearsTyped {
    String message() default "{category.yearsNotTyped}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AreYearsTypedValidator implements ConstraintValidator<AreYearsTyped, Category> {

        @Override
        public boolean isValid(Category value, ConstraintValidatorContext context) {
            if (value.getMode() == CategoryEnum.year) {
                return !(value.getMinYear() == null && value.getMaxYear() == null);
            }
            return true;
        }
    }
}
