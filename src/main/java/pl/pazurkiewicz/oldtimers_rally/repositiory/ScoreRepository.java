package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Score;

import java.util.List;
import java.util.Set;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> getByCompetitionAndResultNotNullAndCrew_InOrderByResult(Competition competition, Set<Crew> crews);

    Score getByCompetitionAndCrew(Competition competition, Crew crew);
}
