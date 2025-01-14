package by.Filin.TaskManager.service;

import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.repository.UserRepository;
import by.Filin.TaskManager.security.CustomUserDetails;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

//    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);
    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warning("User not found: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        String role = user.getRole().getName();
        logger.info("Loaded user: " + username + " with role: " + role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles(role)
                .build();
    }
}
