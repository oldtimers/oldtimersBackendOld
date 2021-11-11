package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

public interface EventLanguageRepository extends JpaRepository<EventLanguage, Integer> {
}
