package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
    @Query("select distinct c from Crew c left join fetch c.description d left join fetch d.dictionaries " +
            "where c.event.id=:eventId " +
            "order by c.yearOfProduction")
    List<Crew> getSortedByEventId(Integer eventId);

    List<Crew> getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(String url);

    @Query("select c from Crew c where c.qrCodes.size=0")
    Set<Crew> getAllByEventAndQrIsNull(Event event);

    @Query("select distinct c from Crew c join fetch c.categories ca where ca.category=:category")
    Set<Crew> getByCategory(Category category);

    @Query("select c from Crew c left join fetch c.categories where c.event=:event")
    Set<Crew> getByEvent(Event event);


    @Query("select c from Crew c left join fetch c.description d left join fetch d.dictionaries where c.id = :crewId and c.event = :event")
    Optional<Crew> getByIdWithLanguages(Integer crewId, Event event);


    @Query("select c from Crew c inner join c.qrCodes qc where qc.qr = :qr and c.event.id = :eventId")
    <T>
    Optional<T> findByQrCodesContains(String qr, Integer eventId, Class<T> type);

    @Query("select c from Crew c inner join c.scores as s where s.competition = :competition and s.additional2 is null order by c.number")
    <T>
    List<T> getAllStartedForCompetition(Competition competition, Class<T> type);
}
