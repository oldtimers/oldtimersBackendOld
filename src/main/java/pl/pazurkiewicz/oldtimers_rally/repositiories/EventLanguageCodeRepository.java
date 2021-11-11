package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

public interface EventLanguageCodeRepository extends JpaRepository<EventLanguageCode, Integer> {
}
