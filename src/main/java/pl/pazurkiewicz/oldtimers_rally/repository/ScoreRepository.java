package pl.pazurkiewicz.oldtimers_rally.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.Score;

import java.util.List;
import java.util.Set;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    @Query("select s from Score s where s.competition=:competition and s.crew in :crews and s.result is not null and s.invalidResult = false order by s.result")
    List<Score> getValidScoresForCompetition(Competition competition, Set<Crew> crews);

    @Query("select s from Score s where s.competition=:competition and s.crew in :crews and s.result is not null and s.invalidResult = true order by s.result")
    List<Score> getInvalidScoresForCompetition(Competition competition, Set<Crew> crews);

    @Query("select s.competition.id,s.crew.id from Score s where s.competition.event=:event group by s.competition, s.crew having count(s.crew)>1 order by s.competition.id, s.crew.id")
    List<List<Integer>> getDuplicates(Event event);

    Score getByCompetitionAndCrew(Competition competition, Crew crew);
}
