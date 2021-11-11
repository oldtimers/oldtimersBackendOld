package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language getLanguageByCode(String code);
}
