package pl.pazurkiewicz.oldtimers_rally.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.errors.InvalidConfigurationProperties;

@Service
@ApplicationScope
public class LanguageService {
    @Autowired
    private LanguageRepository repository;

    @Autowired
    private MyConfigurationProperties properties;

    public Language getDefaultSystemLanguage() {
        System.out.println("chuj");
        Language result = repository.getLanguageByCode(properties.getDefaultLanguage());
        if (result == null) {
            throw new InvalidConfigurationProperties("Invalid property: custom.defaultLanguage");
        }
        return result;
    }
}
