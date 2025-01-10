package by.Filin.TaskManager.entity;

import by.Filin.TaskManager.DTO.jwt.AuthResponse;
import by.Filin.TaskManager.DTO.jwt.RegisterRequest;
import by.Filin.TaskManager.repository.UserRepository;
import by.Filin.TaskManager.service.TokenService;
import by.Filin.TaskManager.token.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; // Объект для хэширования и проверки паролей



    public AuthResponse authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        if(!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new BadCredentialsException("Wrong password");
        }

        AccessToken accessToken = tokenService.createAccessToken(user);
        RefreshToken refreshToken = tokenService.createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!tokenService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AccessToken newAccessToken = tokenService.createAccessToken(user);

        return new AuthResponse(newAccessToken.getToken(), refreshToken);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        // Создать нового пользователя
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        // Сгенерировать токены
        AccessToken accessToken = tokenService.createAccessToken(newUser);
        RefreshToken refreshToken = tokenService.createRefreshToken(newUser);

        return new AuthResponse(accessToken.getToken(), refreshToken.getToken());
    }

}
