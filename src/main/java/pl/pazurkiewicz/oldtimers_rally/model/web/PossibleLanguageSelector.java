package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PossibleLanguageSelector {
    private List<LanguageSelectorElement> possibleLanguages;

    public PossibleLanguageSelector(DefaultLanguageSelector defaultLanguageSelector, Event event) {
        possibleLanguages = defaultLanguageSelector.getPossibleLanguages().stream()
                .map(language -> new LanguageSelectorElement(event, language, Objects.equals(defaultLanguageSelector.getDefaultLanguage().getId(), language.getId()))).collect(Collectors.toList());
    }

    public List<LanguageSelectorElement> getPossibleLanguages() {
        return possibleLanguages;
    }

    public void setPossibleLanguages(List<LanguageSelectorElement> possibleLanguages) {
        this.possibleLanguages = possibleLanguages;
    }

    public List<EventLanguage> getEventLanguages(DefaultLanguageSelector defaultLanguageSelector) {
        return possibleLanguages.stream()
                .filter(LanguageSelectorElement::getAccept)
                .map(LanguageSelectorElement::getEventLanguage)
                .peek(eventLanguage -> eventLanguage.setIsDefault(Objects.equals(defaultLanguageSelector.getDefaultLanguage().getId(), eventLanguage.getLanguage().getId())))
                .collect(Collectors.toList());
    }
}
