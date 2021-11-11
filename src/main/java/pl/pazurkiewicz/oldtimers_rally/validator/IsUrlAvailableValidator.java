package pl.pazurkiewicz.oldtimers_rally.validator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsUrlAvailableValidator implements ConstraintValidator<IsUrlAvailable, String> {
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
