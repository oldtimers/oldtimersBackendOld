package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    <T> List<T> getAllByEvent_UrlOrderById(String url, Class<T> type);
}
