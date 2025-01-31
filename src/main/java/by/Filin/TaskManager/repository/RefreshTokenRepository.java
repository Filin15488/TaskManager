package by.Filin.TaskManager.repository;

import by.Filin.TaskManager.entity.AccessToken;
import by.Filin.TaskManager.entity.RefreshToken;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUserId(Long userId);

    List<RefreshToken> findByExpiresAtBeforeAndIsValidTrue(Date now);

    List<RefreshToken> findByExpiresAtBeforeAndIsValidFalse(Date now);
}
