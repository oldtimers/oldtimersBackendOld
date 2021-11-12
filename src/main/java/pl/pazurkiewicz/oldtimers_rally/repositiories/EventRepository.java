package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Boolean existsEventByUrl(String url);

    @Query("select e.id from Event e where e.url = :url")
    Integer getIdByUrl(@Param("url") String url);
}
