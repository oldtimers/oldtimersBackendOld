package pl.pazurkiewicz.oldtimers_rally.validator;


import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.service.CalculatorService;

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
@Constraint(validatedBy = IsFunctionValid.IsFunctionValidValidator.class)
public @interface IsFunctionValid {
    String message() default "{category.invalidFunction}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsFunctionValidValidator implements ConstraintValidator<IsFunctionValid, Competition> {

        @Override
        public boolean isValid(Competition value, ConstraintValidatorContext context) {
            if (value.getType() == CompetitionTypeEnum.REGULAR_DRIVE) {
                return true;
            }
            if (value.getFunctionCode() != null) {
                Argument[] arguments = new Argument[5];
                int j = Math.min(value.getFields().size(), 5);
                for (int i = 0; i < j; i++) {
                    arguments[i] = new Argument(CalculatorService.variableMapping.get(i));
                }
                Expression e = new Expression(value.getFunctionCode(), arguments);
                if (e.checkSyntax()) {
                    return true;
                }
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("functionCode").addConstraintViolation();
            return false;

        }
    }
}
