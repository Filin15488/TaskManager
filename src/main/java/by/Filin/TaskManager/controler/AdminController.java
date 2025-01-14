package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.service.AdminService;
import by.Filin.TaskManager.service.CustomUserDetailsService;
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

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    private final AdminService adminService;

    @PutMapping("/set/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setAdmin(@PathVariable("id") Long id){
        logger.info("Attempting to set ADMIN role for user with ID: " + id);
        adminService.setAdmin(id);
        logger.info("ADMIN role set successfully for user with ID: " + id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        logger.info("Fetching all users with ADMIN access.");
        List<UserDTO> users = adminService.getAllusers();
        logger.info("Successfully fetched users: " + users.size() + " found.");
        return ResponseEntity.ok(users);
    }
}
