package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.jwt.AuthResponse;
import by.Filin.TaskManager.DTO.jwt.RegisterRequest;
import by.Filin.TaskManager.entity.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.Filin.TaskManager.DTO.jwt.LoginRequst;
import by.Filin.TaskManager.DTO.jwt.RefreshRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequst request) {
        return authService.authenticate(request.getUsername(), request.getPassword());
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshRequest request) {
        return authService.refreshAccessToken(request.getRefreshToken());
    }

//    @PostMapping("/registration")
//    public AuthResponse register(@RequestBody RegisterRequest request) {
//        return authService.register(request);
//    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);

        // Добавление токенов в заголовки
        response.setHeader("Access-Token", authResponse.getAccessToken());
        response.setHeader("Refresh-Token", authResponse.getRefreshToken());

        // Установка токенов в куки
        Cookie accessCookie = new Cookie("access_token", authResponse.getAccessToken());
        accessCookie.setHttpOnly(true); // Ограничить доступ к cookie только сервером
        accessCookie.setSecure(true); // Только через HTTPS
        accessCookie.setPath("/");
        accessCookie.setMaxAge(24 * 60 * 60); // Время жизни access токена (24 часа)

        Cookie refreshCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // Время жизни refresh токена (7 дней)

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }
}
