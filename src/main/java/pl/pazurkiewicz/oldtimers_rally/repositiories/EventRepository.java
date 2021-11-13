package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

public interface EventRepository extends CustomRepository<Event, Integer> {
    Boolean existsEventByUrl(String url);

    @Query("select e.id from Event e where e.url = :url")
    Integer getIdByUrl(@Param("url") String url);

    @Query("select distinct e from Event e join fetch e.name join fetch e.description where e.url = :url")
    Event getByUrl(String url);
}
