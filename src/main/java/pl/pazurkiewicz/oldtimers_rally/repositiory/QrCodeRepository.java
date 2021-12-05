package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;

import java.util.Set;

public interface QrCodeRepository extends JpaRepository<QrCode, Integer> {
    Set<QrCode> getAllByEvent(Event event);

    Set<QrCode> getByEventAndCrewIsNull(Event event);
}
