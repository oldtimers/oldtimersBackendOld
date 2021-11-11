package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Boolean existsEventByUrl(String url);
}
