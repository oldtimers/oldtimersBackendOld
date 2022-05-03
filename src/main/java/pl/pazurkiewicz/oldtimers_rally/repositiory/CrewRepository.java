package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrewRepository extends JpaRepository<Crew, Integer> {
    @Query("select distinct c from Crew c left join fetch c.description d left join fetch d.dictionaries " +
            "where c.event.id=:eventId " +
            "order by c.yearOfProduction")
    List<Crew> getSortedByEventId(Integer eventId);

    List<Crew> getAllByEvent_UrlAndPresentIsTrueOrderByQrCode_NumberAscYearOfProductionAsc(String url);

    List<Crew> getAllByEvent_UrlOrderByQrCode_NumberAscYearOfProductionAsc(String url);

    @Query("select c from Crew c where c.qrCode is null")
    Set<Crew> getAllByEventAndQrIsNull(Event event);

    @Query("select distinct c from Crew c join fetch c.categories ca where ca.category=:category")
    Set<Crew> getByCategory(Category category);

    @Query("select c from Crew c where c.qrCode.qr like :qr")
    Optional<Crew> findByQrLike(String qr);

    @Query("select c from Crew c left join fetch c.categories where c.event=:event")
    Set<Crew> getByEvent(Event event);


    @Query("select c from Crew c left join fetch c.description d left join fetch d.dictionaries where c.id = :crewId and c.event = :event")
    Optional<Crew> getByIdWithLanguages(Integer crewId, Event event);

    @Query("select c from Crew c where c.event.id=:eventId and c.qrCode.qr like :qr")
    <T>
    Optional<T> findByQrAndEventId(String qr, Integer eventId, Class<T> type);

    @Query("select c from Crew c inner join c.scores as s where s.competition = :competition and s.additional2 is null order by c.qrCode.number")
    <T>
    List<T> getAllStartedForCompetition(Competition competition, Class<T> type);

    @Query("UPDATE Crew c SET c.present=:present WHERE c.id =:id")
    @Modifying
    @Transactional
    void updatePresentByCrewId(Integer id, Boolean present);

    @Query("select c from Crew c where c.event=:event and c.qrCode is null order by c.yearOfProduction asc , c.id asc")
    List<Crew> getSortedEmptyCrews(Event event);
}
