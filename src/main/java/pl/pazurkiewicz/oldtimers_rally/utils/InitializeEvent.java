package pl.pazurkiewicz.oldtimers_rally.utils;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.hibernate.Hibernate;
import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

public class InitializeEvent implements CacheEventListener<String, Event> {
    @Override
    public void onEvent(CacheEvent<? extends String, ? extends Event> cacheEvent) {
        if (cacheEvent.getNewValue() != null) {
            initializeEvent(cacheEvent.getNewValue());
        }
    }

    private void initializeEvent(Event event) {
        Hibernate.initialize(event.getEventLanguages());
        for (EventLanguage eventLanguage : event.getEventLanguages()) {
            initializeEventLanguage(eventLanguage);
        }
        initializeEventLanguageCode(event.getName());
        initializeEventLanguageCode(event.getDescription());
    }

    private void initializeEventLanguage(EventLanguage eventLanguage) {
        Hibernate.initialize(eventLanguage);
        Hibernate.initialize(eventLanguage.getLanguage());
    }

    private void initializeEventLanguageCode(EventLanguageCode code) {
        Hibernate.initialize(code);
        Hibernate.initialize(code.getDictionaries());
        for (Dictionary dictionary : code.getDictionaries()) {
            initializeDictionary(dictionary);
        }
    }

    private void initializeDictionary(Dictionary dictionary) {
        initializeEventLanguage(dictionary.getEventLanguage());
    }
}
