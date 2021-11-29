package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    @Query("select c from Competition c left join c.name left join c.description where c.event.id=:eventId")
    List<Competition> getByEvent_Id(Integer eventId);
}
