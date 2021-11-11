package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

public class LanguageSelectorElement {
    private Language language;
    private EventLanguage eventLanguage;
    private Boolean accept;

    LanguageSelectorElement(Event event, Language language, Boolean accept) {
        eventLanguage = new EventLanguage(event, language, false);
        this.language = language;
        this.accept = accept;
    }

    public LanguageSelectorElement() {
    }

    public EventLanguage getEventLanguage() {
        return eventLanguage;
    }

    public void setEventLanguage(EventLanguage eventLanguage) {
        this.eventLanguage = eventLanguage;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }
}
