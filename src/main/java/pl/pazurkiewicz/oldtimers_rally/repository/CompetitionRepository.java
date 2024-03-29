package pl.pazurkiewicz.oldtimers_rally.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    @Query("select c from Competition c left join c.name left join c.description where c.event=:event")
    List<Competition> getByEvent(Event event);

    @Query("select c from Competition c left join c.name left join c.description where c.event.url=:url")
    List<Competition> getByUrl(String url);

    <T> List<T> getByEvent_Id(Integer eventId, Class<T> type);

    @Query("select c from Competition c left join c.name n left join n.dictionaries where c.event=:event and c.id=:competitionId")
    Competition getByEventAndId(Event event, Integer competitionId);

    <T> Optional<T> getByEvent_idAndId(Integer eventId, Integer competitionId, Class<T> type);
}
