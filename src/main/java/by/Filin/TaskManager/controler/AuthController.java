package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.jwt.AuthResponse;
import by.Filin.TaskManager.DTO.jwt.RegisterRequest;
import by.Filin.TaskManager.entity.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
