package pl.pazurkiewicz.oldtimers_rally.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("delete from Category where id in :ids and event.id = :eventId")
    void deleteAllByIdsAndEvent_Id(Iterable<Integer> ids, Integer eventId);

    @Query("select c from Category c left join fetch c.crewCategories as cc left join fetch cc.crew where c.event.id = :eventId")
    Set<Category> getByEvent_Id(Integer eventId);

    Set<Category> getByEvent(Event event);
}
