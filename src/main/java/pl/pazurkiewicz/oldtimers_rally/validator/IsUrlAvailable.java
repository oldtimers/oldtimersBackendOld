package pl.pazurkiewicz.oldtimers_rally.validator;


import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUrlAvailable.IsUrlAvailableValidator.class)
@Documented
public @interface IsUrlAvailable {
    String message() default "{event.invalidUrl}";

    String url();

    String old();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsUrlAvailableValidator implements ConstraintValidator<IsUrlAvailable, Object> {
        @Autowired
        EventRepository repository;

        private String url;
        private String old;

        @Override
        public void initialize(IsUrlAvailable constraintAnnotation) {
            this.url = constraintAnnotation.url();
            this.old = constraintAnnotation.old();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            String urlValue = (String) new BeanWrapperImpl(value).getPropertyValue(url);
            String oldValue = (String) new BeanWrapperImpl(value).getPropertyValue(old);
            if (!repository.existsByUrlAndUrlNot(urlValue, oldValue)) {
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(url).addConstraintViolation();
                return false;
            }
        }
    }
}
