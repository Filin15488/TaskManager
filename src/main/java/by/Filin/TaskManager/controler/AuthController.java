package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.jwt.*;
import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthController", description = "Контроллер аутентификации пользователей")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Вход пользователя",
            description = "Аутентифицирует пользователя и возвращает токены.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)),
                            headers = {
                                    @Header(name = "Set-Cookie", description = "Куки с токенами: access_token, refresh_token"),
                                    @Header(name = "Access-Token", description = "Кастомный заголовок с access-токеном"),
                                    @Header(name = "Refresh-Token", description = "Кастомный заголовок с refresh-токеном")
                            }
                    ),
                    @ApiResponse(responseCode = "403", description = "Ошибка аутентификации - неверные учетные данные", content = @Content)
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
//        authService.login(authRequest, response);
        return ResponseEntity.ok().body(authService.login(authRequest, response));
    }

    @Operation(summary = "Обновление токенов аутентификации",
            description = "Генерирует новые токены аутентификации с использованием refresh-токена.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Токены успешно обновлены",
                            content = @Content,
                            headers = {
                                    @Header(name = "Set-Cookie", description = "Куки с токенами: access_token, refresh_token"),
                                    @Header(name = "Access-Token", description = "Кастомный заголовок с access-токеном"),
                                    @Header(name = "Refresh-Token", description = "Кастомный заголовок с refresh-токеном")
                            }

                    ),
                    @ApiResponse(responseCode = "403", description = "Ошибка аутентификации - неверный или просроченный refresh-токен", content = @Content)
            })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refresh_token", required = true) String refreshToken, HttpServletResponse response) {
        authService.refreshTokens(refreshToken, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Регистрация пользователя",
            description = "Регистрирует нового пользователя с ролью ПОЛЬЗОВАТЕЛЬ и возвращает токены аутентификации.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
                            headers = {
                                    @Header(name = "Set-Cookie", description = "Куки с токенами: access_token, refresh_token"),
                                    @Header(name = "Access-Token", description = "Кастомный заголовок с access-токеном"),
                                    @Header(name = "Refresh-Token", description = "Кастомный заголовок с refresh-токеном")
                            },
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "403", description = "Ошибка запроса - некорректные данные", content = @Content)
            })
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest, HttpServletResponse response) {
        authService.register(registerRequest, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
