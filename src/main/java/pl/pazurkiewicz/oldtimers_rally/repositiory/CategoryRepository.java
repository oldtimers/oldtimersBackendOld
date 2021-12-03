package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CategoryEnum;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select distinct c from Category c left join fetch c.name left join fetch c.description where c.event.id=:eventId")
    List<Category> findByEvent_IdOrderById(Integer eventId);


    <T> Set<T> getByEvent_IdAndMode(Integer eventId, CategoryEnum mode, Class<T> type);

    default Set<Category> getByEvent_IdAndModeYear(Integer eventId) {
        return getByEvent_IdAndMode(eventId, CategoryEnum.year, Category.class);
    }

    List<Category> getByEvent(Event event);
}
