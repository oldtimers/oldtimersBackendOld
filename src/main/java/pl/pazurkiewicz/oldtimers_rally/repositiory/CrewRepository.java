package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
    @Query("select c from Crew c left join fetch c.description d left join fetch d.dictionaries " +
            "where c.event.id=:eventId " +
            "order by c.yearOfProduction")
    List<Crew> getSortedByEventId(Integer eventId);

    <T> List<T> getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(String url, Class<T> type);
}
