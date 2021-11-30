package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    @Query("select c from Competition c left join c.name left join c.description where c.event=:event")
    List<Competition> getByEvent(Event event);

    @Query("select c from Competition c left join c.name n left join n.dictionaries where c.event=:event and c.id=:competitionId")
    Competition getByEventAndId(Event event, Integer competitionId);
}
