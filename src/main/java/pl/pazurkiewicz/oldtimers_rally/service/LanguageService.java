package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.web.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.repositiory.LanguageRepository;

@Service
@ApplicationScope
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MyConfigurationProperties properties;

    public Language getDefaultSystemLanguage() {
        Language result = languageRepository.getLanguageByCode(properties.getDefaultLanguage());
        if (result == null) {
            throw new InvalidConfigurationProperties("Invalid property: custom.defaultLanguage");
        }
        return result;
    }

    public DefaultLanguageSelector generateDefaultLanguageSelector() {
        return new DefaultLanguageSelector(languageRepository.findAll(), getDefaultSystemLanguage());
    }

    public DefaultLanguageSelector generateDefaultLanguageSelectorByEvent(Event event) {
        return new DefaultLanguageSelector(languageRepository.findAll(), event.getSingleDefaultLanguage().getLanguage());
    }
}
