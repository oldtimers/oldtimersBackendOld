package pl.pazurkiewicz.oldtimers_rally.language;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language getLanguageByCode(String code);
}
