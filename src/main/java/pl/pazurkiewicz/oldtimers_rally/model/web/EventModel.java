package pl.pazurkiewicz.oldtimers_rally.model.web;

import org.springframework.format.annotation.DateTimeFormat;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;
import pl.pazurkiewicz.oldtimers_rally.validator.IsEndDateValid;
import pl.pazurkiewicz.oldtimers_rally.validator.IsUrlAvailable;
import pl.pazurkiewicz.oldtimers_rally.validator.IsUrlPossible;

import javax.validation.*;
import javax.validation.constraints.Min;
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

    class IsReloaded implements ConstraintValidator<Reload, EventModel> {

        @Override
        public boolean isValid(EventModel value, ConstraintValidatorContext context) {
            value.reload();
            return true;
        }
    }
}

@Reload
@IsUrlAvailable(url = "url", old = "event.url")
@IsEndDateValid(startDate = "startDate", endDate = "endDate")
public class EventModel {
    private final Event event;
    private final PossibleLanguageSelector possibleLanguageSelector;
    private final DefaultLanguageSelector defaultLanguageSelector;
    @Valid
    private final EventLanguageCode name;
    @Valid
    private final EventLanguageCode description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    @NotBlank(message = "{notBlank}")
    @IsUrlPossible
    private String url;
    @NotNull
    @Min(1)
    private Integer maxCrewNumber = 20;

    private List<EventLanguage> languages;

    public EventModel(Event event, PossibleLanguageSelector possibleLanguageSelector, DefaultLanguageSelector defaultLanguageSelector, EventLanguageCode name, EventLanguageCode description, LocalDateTime startDate, LocalDateTime endDate, String url) {
        this.event = event;
        this.possibleLanguageSelector = possibleLanguageSelector;
        this.defaultLanguageSelector = defaultLanguageSelector;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.url = url;
        reload();
    }

    public EventModel(Event event, PossibleLanguageSelector possibleLanguageSelector, DefaultLanguageSelector defaultLanguageSelector) {
        this.event = event;
        this.possibleLanguageSelector = possibleLanguageSelector;
        this.defaultLanguageSelector = defaultLanguageSelector;
        this.name = event.getName();
        this.description = event.getDescription();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.url = event.getUrl();
        this.maxCrewNumber = event.getMaxCrewNumber();
        reload();
    }

    public Integer getMaxCrewNumber() {
        return maxCrewNumber;
    }

    public void setMaxCrewNumber(Integer maxCrewNumber) {
        this.maxCrewNumber = maxCrewNumber;
    }

    public static EventModel generateNewEventWriteModel(LanguageService languageService) {
        Event event = new Event();
        DefaultLanguageSelector defaultLanguageSelector = languageService.generateDefaultLanguageSelector();
        PossibleLanguageSelector possibleLanguageSelector = new PossibleLanguageSelector(defaultLanguageSelector, event);
        return new EventModel(
                event,
                possibleLanguageSelector,
                defaultLanguageSelector,
                EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector)),
                EventLanguageCode.generateNewEventLanguageCode(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector)),
                LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                ""
        );


    }

    public static EventModel generateByEvent(Event event, LanguageService languageService) {
        DefaultLanguageSelector defaultLanguageSelector = languageService.generateDefaultLanguageSelectorByEvent(event);
        PossibleLanguageSelector possibleLanguageSelector = new PossibleLanguageSelector(defaultLanguageSelector, event);
        event.getName().prepareForLoad(event.getEventLanguages());
        event.getDescription().prepareForLoad(event.getEventLanguages());
        return new EventModel(event, possibleLanguageSelector, defaultLanguageSelector);
    }

    public static String generateURL(String url) {
        return url.replace(' ', '_').replace("/", "");
    }

    public List<EventLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<EventLanguage> languages) {
        this.languages = languages;
    }

    void reload() {
        List<EventLanguage> eventLanguages = possibleLanguageSelector.getEventLanguages(defaultLanguageSelector);
        eventLanguages.sort(new EventLanguageComparator());
        this.languages = eventLanguages;
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


    public EventLanguageCode getDescription() {
        return description;
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

    public Event getEvent() {
        return event;
    }

    public Event generateEvent() {
        name.preUpdate();
        description.preUpdate();
        event.setName(name);
        event.setDescription(description);
        event.setUrl(url);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setEventLanguages(possibleLanguageSelector.getEventLanguages(defaultLanguageSelector));
        event.setMaxCrewNumber(maxCrewNumber);
        return event;
    }
}
