package by.Filin.TaskManager.repository;

import by.Filin.TaskManager.entity.AccessToken;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByToken(String token);
    Optional<List<AccessToken>> findAllByUserId(Long userId);
    List<AccessToken> findByExpiresAtBeforeAndIsValidTrue(Date now);
    List<AccessToken> findByExpiresAtBeforeAndIsValidFalse(Date now);
}
