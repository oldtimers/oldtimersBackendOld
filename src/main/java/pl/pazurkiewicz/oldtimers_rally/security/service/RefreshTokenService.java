package pl.pazurkiewicz.oldtimers_rally.security.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.TokenRefreshException;
import pl.pazurkiewicz.oldtimers_rally.model.RefreshToken;
import pl.pazurkiewicz.oldtimers_rally.repositiory.RefreshTokenRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(MyConfigurationProperties configurationProperties, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        refreshTokenDurationMs = configurationProperties.getJwtRefreshExpiration();
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Integer userId) {
        Optional<RefreshToken> oldToken = refreshTokenRepository.findByUser_Id(userId);
        RefreshToken refreshToken;
        if (oldToken.isEmpty()) {
            refreshToken = new RefreshToken();

            refreshToken.setUser(userRepository.getById(userId));

        } else {
            refreshToken = oldToken.get();
        }
        refreshToken.setExpiryDate(LocalDateTime.now().plus(refreshTokenDurationMs, ChronoUnit.MILLIS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }

    public RefreshToken increaseExpiration(RefreshToken token) {
        token.setExpiryDate(LocalDateTime.now().plus(refreshTokenDurationMs, ChronoUnit.MILLIS));
        return token;
    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        return refreshTokenRepository.deleteByUser(userRepository.getById(userId));
    }

    @Scheduled(fixedRate = 86400000)
    public void removeExpiredRefreshTokens() {
        refreshTokenRepository.deleteAllExpired();
    }
}
