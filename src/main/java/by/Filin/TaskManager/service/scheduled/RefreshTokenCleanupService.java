package by.Filin.TaskManager.service.scheduled;

import by.Filin.TaskManager.entity.RefreshToken;
import by.Filin.TaskManager.repository.RefreshTokenRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateExpiredTokens() {
        Date now = new Date();

        List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpiresAtBeforeAndIsValidTrue(now);

        if (!expiredTokens.isEmpty()) {
            expiredTokens.forEach(refreshToken -> refreshToken.setValid(false));

            refreshTokenRepository.saveAll(expiredTokens);
            log.info("Deactivated refresh tokens: " + expiredTokens.size());
        }
        else {
            log.info("There are no expired refresh tokens at the moment");
        }

    }

    @Scheduled(cron = "0 0 0 * * Sat,Sun")
    public void cleanupRefreshTokens() {
        Date now = new Date();

        List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpiresAtBeforeAndIsValidFalse(now);

        if (!expiredTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(expiredTokens);
            log.info("Delete refresh expired tokens: " + expiredTokens.size());
        }
        else {
            log.info("There are currently no expired refresh tokens to delete.");
        }

    }

}
