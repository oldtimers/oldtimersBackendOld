package pl.pazurkiewicz.oldtimers_rally.model.projection;

import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.web.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.model.web.PossibleLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;
import pl.pazurkiewicz.oldtimers_rally.validator.IsEndDateValid;
import pl.pazurkiewicz.oldtimers_rally.validator.IsUrlAvailable;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Reload
@IsEndDateValid(starDate = "startdate", endDate = "endDate")
public class EventWriteModel {
    private final PossibleLanguageSelector possibleLanguageSelector;
    private final DefaultLanguageSelector defaultLanguageSelector;
    @Valid
    private EventLanguageCode name;
    @Valid
    private EventLanguageCode description;
    @NotNull
    private Instant startDate = Instant.now();
    @NotNull
    private Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
    @NotBlank
    @IsUrlAvailable
    private String url;

    public EventWriteModel(DefaultLanguageSelector defaultLanguage, PossibleLanguageSelector possibleLanguageSelector) {
        this.defaultLanguageSelector = defaultLanguage;
        this.possibleLanguageSelector = possibleLanguageSelector;
    }

    public static EventWriteModel generateNewEventWriteModel(LanguageService languageService, LanguageRepository repository) {
        Event event = new Event();
        DefaultLanguageSelector defaultLanguageSelector = new DefaultLanguageSelector(languageService, repository);
        PossibleLanguageSelector possibleLanguageSelector = new PossibleLanguageSelector(defaultLanguageSelector, event);
        EventWriteModel eventWriteModel = new EventWriteModel(defaultLanguageSelector, possibleLanguageSelector);
        eventWriteModel.name = EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        eventWriteModel.description = EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        return eventWriteModel;
    }

    void reload() {
        List<EventLanguage> eventLanguages = possibleLanguageSelector.getEventLanguages(defaultLanguageSelector);
        name.reload(eventLanguages);
        description.reload(eventLanguages);
    }

    public Instant getStartDate() {
        return startDate;
    }

    public PossibleLanguageSelector getPossibleLanguageSelector() {
        return possibleLanguageSelector;
    }

    public EventLanguageCode getName() {
        return name;
    }

    public void setName(EventLanguageCode name) {
        this.name = name;
    }

    public EventLanguageCode getDescription() {
        return description;
    }

    public void setDescription(EventLanguageCode description) {
        this.description = description;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public DefaultLanguageSelector getDefaultLanguageSelector() {
        return defaultLanguageSelector;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void generateEvent(){

    }
}

// This "validator" reload object before real validation
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = Reload.IsReloaded.class)
@interface Reload{
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    class IsReloaded implements ConstraintValidator<Reload, EventWriteModel> {

        @Override
        public boolean isValid(EventWriteModel value, ConstraintValidatorContext context) {
            value.reload();
            return true;
        }
    }
}
