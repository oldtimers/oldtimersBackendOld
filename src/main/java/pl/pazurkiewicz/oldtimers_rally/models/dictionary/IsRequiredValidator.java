package pl.pazurkiewicz.oldtimers_rally.models.dictionary;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsRequiredValidator implements ConstraintValidator<IsRequired, Dictionary> {

    @Override
    public boolean isValid(Dictionary value, ConstraintValidatorContext context) {
        if (value != null) {
//            value.
        }
        return false;
    }
}
