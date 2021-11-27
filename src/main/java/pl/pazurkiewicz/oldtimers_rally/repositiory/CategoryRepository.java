package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select distinct c from Category c left join fetch c.name left join fetch c.description where c.event.id=:eventId")
    List<Category> findByEvent_IdOrderById(Integer eventId);
}
