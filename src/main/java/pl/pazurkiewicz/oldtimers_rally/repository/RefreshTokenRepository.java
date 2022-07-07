package pl.pazurkiewicz.oldtimers_rally.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.RefreshToken;
import pl.pazurkiewicz.oldtimers_rally.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser_Id(Integer userId);


    @Modifying
    int deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from RefreshToken where expiryDate < CURRENT_TIME ")
    void deleteAllExpired();
}
