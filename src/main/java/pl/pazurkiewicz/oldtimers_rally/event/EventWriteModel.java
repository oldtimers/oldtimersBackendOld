package pl.pazurkiewicz.oldtimers_rally.event;

import pl.pazurkiewicz.oldtimers_rally.language.Language;

import java.time.Instant;

public class EventWriteModel {
    private EventLanguageCode name = new EventLanguageCode();
    private EventLanguageCode description = new EventLanguageCode();
    private Instant startDate;
    private Instant endDate;
    private Language defaultLanguage = new Language();
    private String url;

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

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
