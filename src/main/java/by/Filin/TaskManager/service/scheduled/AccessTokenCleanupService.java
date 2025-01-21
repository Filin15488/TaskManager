package by.Filin.TaskManager.service.scheduled;

import by.Filin.TaskManager.entity.AccessToken;
import by.Filin.TaskManager.repository.AccessTokenRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenCleanupService {

    private final AccessTokenRepository accessTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateExpiredTokens() {
        Date now = new Date();

        List<AccessToken> expiredTokens = accessTokenRepository.findByExpiresAtBeforeAndIsValidTrue(now);

        if (!expiredTokens.isEmpty()) {
            expiredTokens.forEach(accessToken -> accessToken.setValid(false));

            accessTokenRepository.saveAll(expiredTokens);
            log.info("Deactivated access tokens: " + expiredTokens.size());
        }
        else {
            log.info("There are no expired access tokens at the moment");
        }

    }

    @Scheduled(cron = "0 0 0 * * Sat,Sun")
    public void cleanupAccessTokens() {
        Date now = new Date();

        List<AccessToken> expiredTokens = accessTokenRepository.findByExpiresAtBeforeAndIsValidFalse(now);

        if (!expiredTokens.isEmpty()) {
            accessTokenRepository.deleteAll(expiredTokens);
            log.info("Delete expired tokens: " + expiredTokens.size());
        }
        else {
            log.info("There are currently no expired tokens to delete.");
        }

    }

}
