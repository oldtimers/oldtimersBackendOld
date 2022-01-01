package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Boolean existsByUrlAndUrlNot(String url, String oldUrl);

    //    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cacheable(value = "eventsId", key = "#url", unless = "#result == null")
    @Query("select e.id from Event e where e.url = :url")
    Integer getIdByUrl(@Param("url") String url);

    @Cacheable(value = "eventsByUrl", key = "#url", unless = "#result == null")
    @Query("select distinct e from Event e " +
            "left join fetch e.name n  " +
            "left join fetch e.description " +
            "where e.url = :url")
//    @Query("select distinct e from Event e left join fetch e.name left join fetch e.description where e.url = :url")
    Event getByUrl(String url);

    @Query("select distinct e from Event e left join fetch e.name left join fetch e.description order by e.url")
    List<Event> findAllSorted();

    @Query("select distinct e from Event e, UserGroup u left join fetch e.name left join fetch e.description where u.event=e and u.user.id=:userId order by e.id")
    <T>
    List<T> getEventsWithJudgePrivilegesForId(Integer userId, Class<T> type);

    @Query("select distinct e from Event e, UserGroup u inner join fetch e.eventLanguages el inner join fetch el.language where u.user.id=:userId and u.event is null")
    <T>
    List<T> getEventsWithGlobalJudgePrivilegesForId(Integer userId, Class<T> type);

    <T> Optional<T> findById(Integer id, Class<T> type);
}
