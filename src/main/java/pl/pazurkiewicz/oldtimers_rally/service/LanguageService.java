package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.error.InvalidConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.web.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;

@Service
@ApplicationScope
public class LanguageService {
    @Autowired
    private LanguageRepository repository;

    @Autowired
    private MyConfigurationProperties properties;

    public Language getDefaultSystemLanguage() {
        Language result = repository.getLanguageByCode(properties.getDefaultLanguage());
        if (result == null) {
            throw new InvalidConfigurationProperties("Invalid property: custom.defaultLanguage");
        }
        return result;
    }

    public DefaultLanguageSelector generateDefaultLanguageSelector(){
        return new DefaultLanguageSelector(repository.findAll(),getDefaultSystemLanguage());
    }
}
