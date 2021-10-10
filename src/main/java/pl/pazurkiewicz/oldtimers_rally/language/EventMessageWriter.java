package pl.pazurkiewicz.oldtimers_rally.language;

import pl.pazurkiewicz.oldtimers_rally.event.EventLanguageCode;

import java.util.List;

public class EventMessageWriter {
    private final List<Language> languages;

    public EventMessageWriter(List<Language> languages) {
        this.languages = languages;
    }
}
