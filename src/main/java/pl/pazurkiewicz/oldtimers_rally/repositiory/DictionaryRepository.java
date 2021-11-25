package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
}
