package by.Filin.TaskManager.security;

import by.Filin.TaskManager.entity.Role;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.repository.RoleRepository;
import by.Filin.TaskManager.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserCreator implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setPasswordHash(passwordEncoder.encode("admin"));

        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);

        user.setRole(adminRole);

        Optional<User> userOptional = userRepository.findByIdAndUsername(1L, "admin");

        if (userOptional.isEmpty()) {
            userRepository.save(user);
        }

    }
}
