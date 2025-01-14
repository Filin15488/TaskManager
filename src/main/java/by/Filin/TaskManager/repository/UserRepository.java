package by.Filin.TaskManager.repository;

import by.Filin.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
//    Optional<User> existsByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Object> findByEmail(String email);
}
