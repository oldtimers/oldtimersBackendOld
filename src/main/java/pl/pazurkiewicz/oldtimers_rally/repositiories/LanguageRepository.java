package pl.pazurkiewicz.oldtimers_rally.repositiories;

import pl.pazurkiewicz.oldtimers_rally.model.Language;

public interface LanguageRepository extends CustomRepository<Language, Integer> {
    Language getLanguageByCode(String code);
}
