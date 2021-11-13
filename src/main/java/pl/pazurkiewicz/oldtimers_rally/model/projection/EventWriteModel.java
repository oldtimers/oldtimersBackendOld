package pl.pazurkiewicz.oldtimers_rally.model.projection;

import org.springframework.format.annotation.DateTimeFormat;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.web.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.model.web.PossibleLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;
import pl.pazurkiewicz.oldtimers_rally.validator.IsEndDateValid;
import pl.pazurkiewicz.oldtimers_rally.validator.IsUrlAvailable;
import pl.pazurkiewicz.oldtimers_rally.validator.IsUrlPossible;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// This "validator" reload object before real validation
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = Reload.IsReloaded.class)
@interface Reload {
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

@Reload
@IsEndDateValid(startDate = "startDate", endDate = "endDate")
public class EventWriteModel {
    private final PossibleLanguageSelector possibleLanguageSelector;
    private final DefaultLanguageSelector defaultLanguageSelector;
    Event event;
    @Valid
    private EventLanguageCode name;
    @Valid
    private EventLanguageCode description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate = LocalDateTime.now();
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate = startDate.plus(1, ChronoUnit.DAYS);
    @NotBlank
    @IsUrlAvailable
    @IsUrlPossible
    private String url;

    private EventWriteModel(Event event, DefaultLanguageSelector defaultLanguage, PossibleLanguageSelector possibleLanguageSelector) {
        this.event = event;
        this.defaultLanguageSelector = defaultLanguage;
        this.possibleLanguageSelector = possibleLanguageSelector;
    }

    public static EventWriteModel generateNewEventWriteModel(LanguageService languageService, LanguageRepository repository) {
        Event event = new Event();
        DefaultLanguageSelector defaultLanguageSelector = languageService.generateDefaultLanguageSelector();
        PossibleLanguageSelector possibleLanguageSelector = new PossibleLanguageSelector(defaultLanguageSelector, event);
        EventWriteModel eventWriteModel = new EventWriteModel(event, defaultLanguageSelector, possibleLanguageSelector);
        eventWriteModel.name = EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        eventWriteModel.description = EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        return eventWriteModel;
    }

    public static String generateURL(String url) {
        return url.replace(' ', '_');
    }

    void reload() {
        List<EventLanguage> eventLanguages = possibleLanguageSelector.getEventLanguages(defaultLanguageSelector);
        name.reload(eventLanguages);
        description.reload(eventLanguages);
        url = generateURL(url);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
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

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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

    public Event generateEvent() {
        event.setName(name);
        event.setDescription(description);
        event.setUrl(url);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setEventLanguages(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        return event;
    }
}
