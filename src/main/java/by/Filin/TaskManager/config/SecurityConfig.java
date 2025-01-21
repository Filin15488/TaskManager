package by.Filin.TaskManager.config;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import by.Filin.TaskManager.service.CustomUserDetailsService;
import by.Filin.TaskManager.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt для хэширования паролей
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Initialize SecurityFilterChain...");
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        {
                            logger.info("Setting session management to STATELESS");
                            sessionManagement.sessionCreationPolicy(STATELESS);

                        }
                )

                .authorizeHttpRequests(authorizeHttpRequests ->
                        {
                            logger.info("Configuring authorization rules...");
                            authorizeHttpRequests
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                    .requestMatchers("/api/users", "/api/users/**").hasRole("ADMIN")
                                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Разрешить доступ
                                    .anyRequest().authenticated();
                        }

                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static void getEmptyTokenResponse(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        response.getWriter().write(
                "{\"error\": \"To get access you need token\"}"
        );
    }

}
