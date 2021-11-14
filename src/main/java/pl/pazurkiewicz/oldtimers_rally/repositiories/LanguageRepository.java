package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language getLanguageByCode(String code);
}
