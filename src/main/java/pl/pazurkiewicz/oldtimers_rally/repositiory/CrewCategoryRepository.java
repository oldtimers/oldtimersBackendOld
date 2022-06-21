package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CrewCategory;

import java.util.Set;

public interface CrewCategoryRepository extends JpaRepository<CrewCategory, Integer> {

    @Query("select c from CrewCategory c inner join fetch c.crew where c.category=:category and c.crew.present=true and c.crew.qrCode is not null")
    Set<CrewCategory> getCrewCategoriesByCategoryAndPresent(Category category);
}
