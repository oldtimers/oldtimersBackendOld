package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Language;

import java.util.List;


public class DefaultLanguageSelector {
    private List<Language> possibleLanguages;
    private Language defaultLanguage;

    public DefaultLanguageSelector(List<Language> possibleLanguages, Language defaultLanguage) {
        this.possibleLanguages = possibleLanguages;
        this.defaultLanguage = defaultLanguage;
    }

    public DefaultLanguageSelector() {
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
