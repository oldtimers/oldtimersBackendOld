package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pazurkiewicz.oldtimers_rally.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByLogin(String login);


}
