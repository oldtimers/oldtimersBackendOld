package pl.pazurkiewicz.oldtimers_rally.language;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PossibleLanguageSelector {
    private final List<LanguageSelectorElement> possibleLanguages;

    public PossibleLanguageSelector(DefaultLanguageSelector defaultLanguageSelector) {
        possibleLanguages = defaultLanguageSelector.getPossibleLanguages().stream()
                .map(language -> new LanguageSelectorElement(language, Objects.equals(defaultLanguageSelector.getDefaultLanguage().getId(), language.getId()))).collect(Collectors.toList());
    }

    public List<LanguageSelectorElement> getPossibleLanguages() {
        return possibleLanguages;
    }
}
