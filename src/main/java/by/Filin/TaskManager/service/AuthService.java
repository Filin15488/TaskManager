package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.jwt.AuthRequest;
import by.Filin.TaskManager.DTO.jwt.RegisterRequest;
import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.entity.AccessToken;
import by.Filin.TaskManager.entity.RefreshToken;
import by.Filin.TaskManager.entity.Role;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.UserMapper;
import by.Filin.TaskManager.repository.AccessTokenRepository;
import by.Filin.TaskManager.repository.RefreshTokenRepository;
import by.Filin.TaskManager.repository.RoleRepository;
import by.Filin.TaskManager.repository.UserRepository;
import by.Filin.TaskManager.token.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.AccessLifetime}")
    private Duration accessTokenDuration;

    @Value("${jwt.RefreshLifetime}")
    private Duration refreshTokenDuration;

    @Autowired
    private TokenService tokenService;

    public UserDTO login(AuthRequest authRequest, HttpServletResponse response) {
        // Загружаем пользователя
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        logger.info("login user: " + userDetails.getUsername());

        if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            logger.warning("Wrong password");
            throw new AccessDeniedException("Invalid credentials");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() ->
                {
                    logger.warning("User not found");
                    new AccessDeniedException("User not found");

                    return null;
                });

        logger.info("user found in the system: " + user.getUsername());

        AccessToken currentAccessToken;
        RefreshToken currentRefreshToken;

        // Если пользователь логиниться в первый раз
        if (accessTokenRepository.findAllByUserId(user.getId()).isEmpty() && refreshTokenRepository.findAllByUserId(user.getId()).isEmpty()) {
            logger.warning("Access token not found");
            currentAccessToken = tokenService.createAccessToken(user);

            logger.warning("Refresh token not found");
            currentRefreshToken = tokenService.createRefreshToken(user);

            // Устанавливаем токены в ответ
            setTokensInResponse(response, currentAccessToken.getToken(), currentRefreshToken.getToken());
            return userMapper.toDTO(user);
        }

        // Получаем токены из базы данных
        currentAccessToken = tokenService.getAccessTokenFromDB(user).getLast();
        currentRefreshToken= tokenService.getRefreshTokenFromDB(user).getLast();

        // Проверяем валидность токенов
        boolean accessTokenValid = tokenService.validateAccessToken(currentAccessToken.getToken());
        boolean refreshTokenValid = tokenService.validateRefreshToken(currentRefreshToken.getToken());

        // Если токен истёк, генерируем новые
        if (!accessTokenValid || !refreshTokenValid) {
            if (!refreshTokenValid) {
                currentRefreshToken = tokenService.createRefreshToken(user);
            }
            currentAccessToken = tokenService.createAccessToken(user);
        }

        logger.info("Set tokens from user: " + user.getUsername());

        // Устанавливаем токены в ответ
        setTokensInResponse(response, currentAccessToken.getToken(), currentRefreshToken.getToken());
        return userMapper.toDTO(user);
    }

    public void refreshTokens(String refreshToken, HttpServletResponse response) {

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            throw new AccessDeniedException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        tokenService.deactivateAllAccessTokenByUserId(user.getId());
//        String newAccessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        AccessToken newAccessToken = tokenService.createAccessToken(user);

        setTokensInResponse(response, newAccessToken.getToken(), refreshToken);
    }

    public void register(@Valid RegisterRequest registerRequest, HttpServletResponse response) {

        logger.info("Starting user registration: " + registerRequest.getUsername());

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            logger.warning("Registration failed: Username already exists - " + registerRequest.getUsername());
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.warning("Registration failed: Email already exists - " + registerRequest.getEmail());
            throw new RuntimeException("Email already exists");
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(
                () ->
                {
                    log.error("Role not found");
                    return new RuntimeException("Role not found");
                }
        );

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleUser);

        userRepository.save(user);
        logger.info("User registered: " + user.getUsername());

        AccessToken accessToken = tokenService.createAccessToken(user);
        RefreshToken refreshToken = tokenService.createRefreshToken(user);

        setTokensInResponse(response, accessToken.getToken(), refreshToken.getToken());



    }


    private void setTokensInResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        logger.info("Access token: " + accessToken);
        logger.info("Refresh token: " + refreshToken);

        Cookie accessCookie = createCookie("access_token", accessToken, Math.toIntExact(accessTokenDuration.toSeconds()));
        Cookie refreshCookie = createCookie("refresh_token", refreshToken, Math.toIntExact(refreshTokenDuration.toSeconds()));

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}