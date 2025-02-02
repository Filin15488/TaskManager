package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.service.AdminService;
import by.Filin.TaskManager.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "AdminController", description = "API для администрирования")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    private final AdminService adminService;

    @Operation(summary = "Назначение роли АДМИНИСТРАТОР пользователю",
            description = "Назначает пользователю с указанным ID роль АДМИНИСТРАТОР. Доступно только пользователям с ролью АДМИНИСТРАТОР.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "202", description = "Роль АДМИНИСТРАТОР успешно назначена", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Запрещено - недостаточно прав", content = @Content)
            })
    @PutMapping("/set/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setAdmin(@PathVariable("id") Long id){
        logger.info("Attempting to set ADMIN role for user with ID: " + id);
        adminService.setAdmin(id);
        logger.info("ADMIN role set successfully for user with ID: " + id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Получение списка пользователей",
            description = "Возвращает список всех пользователей. Доступно только пользователям с ролью АДМИНИСТРАТОР.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен",
                            content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Запрещено - недостаточно прав", content = @Content)
            })
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        logger.info("Fetching all users with ADMIN access.");
        List<UserDTO> users = adminService.getAllusers();
        logger.info("Successfully fetched users: " + users.size() + " found.");
        return ResponseEntity.ok(users);
    }
}
