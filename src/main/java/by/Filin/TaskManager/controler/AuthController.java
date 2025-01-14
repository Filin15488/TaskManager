package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.jwt.*;
import by.Filin.TaskManager.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        authService.login(authRequest, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refresh_token", required = true) String refreshToken, HttpServletResponse response) {
        authService.refreshTokens(refreshToken, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        authService.register(registerRequest, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
