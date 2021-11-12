package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.User;

import java.time.Instant;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByLogin(String login);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.login =:login")
    @Modifying
    @Transactional
    void updateLastLogin(@Param("lastLogin") Instant lastLogin, @Param("login") String login);
}
