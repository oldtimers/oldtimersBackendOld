package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
