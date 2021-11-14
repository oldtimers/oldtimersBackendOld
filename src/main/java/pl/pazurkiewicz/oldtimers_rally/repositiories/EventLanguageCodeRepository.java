package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

@Repository
public interface EventLanguageCodeRepository extends JpaRepository<EventLanguageCode, Integer> {
}
