package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.exception.ExceptionResponse;
import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.DTO.user.UserRequestDTO;
import by.Filin.TaskManager.DTO.user.UserUpdateDTO;
import by.Filin.TaskManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Контроллер для управления пользователями. Доступен только пользователям с ролью администратор")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его уникальному идентификатору. Если пользователя нет, то возвращает 404",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id) {
        return
                ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Создание нового пользователя", description = "создаёт нового пользователя на основе предоставленных данных",
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            )
            }

    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(
            @Parameter(description = "Данные для создания пользователя", required = true)
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDTO));
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет данные существующего пользователя по его ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно обновлен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id,
                                              @Parameter(description = "Данные для обновления пользователя", required = true)
                                              @RequestBody @Valid UserUpdateDTO updateDTO) {
        UserDTO updatedUser = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его ID",
            responses = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удалён"
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}