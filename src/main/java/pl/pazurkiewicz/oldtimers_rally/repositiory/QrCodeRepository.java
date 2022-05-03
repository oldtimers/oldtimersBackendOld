package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QrCodeRepository extends JpaRepository<QrCode, Integer> {
    Set<QrCode> getAllByEvent(Event event);

    List<QrCode> getByEventOrderByNumberAsc(Event event);

    @Query("select q from QrCode q where q.qr like :qr")
    Optional<QrCode> findByQrLike(String qr);
}
