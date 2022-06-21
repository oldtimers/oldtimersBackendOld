package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.Language;

import java.io.Serializable;

public class LanguageSelectorElement implements Serializable {
    private Language language;
    private EventLanguage eventLanguage;
    private Boolean accept;

    LanguageSelectorElement(Event event, Language language, Boolean accept) {
        this.eventLanguage = event.getEventLanguages().stream()
                .filter(el -> el.getLanguage().getId().equals(language.getId())).findFirst()
                .orElseGet(() -> new EventLanguage(event, language, false));
        this.language = language;
        if (eventLanguage.getId() == null) {
            this.accept = accept;
        } else {
            this.accept = true;
        }
    }

    public LanguageSelectorElement() {
    }

    public EventLanguage getEventLanguage() {
        return eventLanguage;
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
