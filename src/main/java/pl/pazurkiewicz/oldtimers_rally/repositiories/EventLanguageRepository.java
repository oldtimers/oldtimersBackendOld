package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;

import java.util.List;
import java.util.Set;

@Repository
public interface EventLanguageRepository extends JpaRepository<EventLanguage, Integer> {
    @Query("select id from EventLanguage where event.id = :eventId")
    Set<Integer> getIdsByEventId(Integer eventId);
}
