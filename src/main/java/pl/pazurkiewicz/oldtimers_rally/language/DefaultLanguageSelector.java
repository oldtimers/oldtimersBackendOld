package pl.pazurkiewicz.oldtimers_rally.language;

import java.util.List;


public class DefaultLanguageSelector {
    private final List<Language> possibleLanguages;
    private Language defaultLanguage;

    public DefaultLanguageSelector(LanguageService languageService, LanguageRepository repository) {
        this.defaultLanguage = languageService.getDefaultSystemLanguage();
        this.possibleLanguages = repository.findAll();
    }

    public List<Language> getPossibleLanguages() {
        return possibleLanguages;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
