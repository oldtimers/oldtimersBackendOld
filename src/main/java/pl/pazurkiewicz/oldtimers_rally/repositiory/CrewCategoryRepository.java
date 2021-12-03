package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.CrewCategory;

public interface CrewCategoryRepository extends JpaRepository<CrewCategory, Integer> {
}
