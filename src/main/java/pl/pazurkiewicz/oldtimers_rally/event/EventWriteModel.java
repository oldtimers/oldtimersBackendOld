package pl.pazurkiewicz.oldtimers_rally.event;

import org.hibernate.validator.constraints.UniqueElements;
import pl.pazurkiewicz.oldtimers_rally.language.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageService;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class EventWriteModel {
    private final LanguageService languageService;
    private final LanguageRepository repository;
    private final DefaultLanguageSelector defaultLanguage;
    private EventLanguageCode name = new EventLanguageCode();
    private EventLanguageCode description = new EventLanguageCode();
    private Instant startDate = Instant.now();
    private Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
    @NotBlank
    private String url;

    public EventWriteModel(LanguageService languageService, LanguageRepository repository) {
        this.languageService = languageService;
        this.repository = repository;
        this.defaultLanguage = new DefaultLanguageSelector(languageService, repository);
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

    public Instant getStartDate() {
        return startDate;
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

    public DefaultLanguageSelector getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
