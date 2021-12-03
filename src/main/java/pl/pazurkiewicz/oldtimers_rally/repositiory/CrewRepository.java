package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;

import java.util.List;
import java.util.Set;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
    @Query("select distinct c from Crew c left join fetch c.description d left join fetch d.dictionaries " +
            "where c.event.id=:eventId " +
            "order by c.yearOfProduction")
    List<Crew> getSortedByEventId(Integer eventId);

    <T> List<T> getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(String url, Class<T> type);

    @Query("select distinct c from Crew c join fetch c.categories ca where ca.category=:category")
    Set<Crew> getByCategory(Category category);

}
