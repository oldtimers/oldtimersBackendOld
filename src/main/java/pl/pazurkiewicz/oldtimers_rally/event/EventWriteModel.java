package pl.pazurkiewicz.oldtimers_rally.event;

import org.springframework.validation.annotation.Validated;
import pl.pazurkiewicz.oldtimers_rally.language.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageService;
import pl.pazurkiewicz.oldtimers_rally.language.PossibleLanguageSelector;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Validated
public class EventWriteModel {
    private final PossibleLanguageSelector possibleLanguageSelector;
    private final DefaultLanguageSelector defaultLanguageSelector;
    private EventLanguageCode name;
    private EventLanguageCode description;
    private Instant startDate = Instant.now();
    private Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
    @NotBlank
    private String url = "";

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

    public void reload() {
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
}
