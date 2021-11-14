package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

@Repository
public interface EventLanguageRepository extends JpaRepository<EventLanguage, Integer> {
}
