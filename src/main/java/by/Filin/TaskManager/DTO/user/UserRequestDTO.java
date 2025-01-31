package by.Filin.TaskManager.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "Name cannot be null.")
    @Size(min = 2, max = 30, message = "The name length must be between 2 and 30 characters.")
    private String username;

    @NotBlank(message = "Password cannot be null.")
    private String password;

    @NotBlank(message = "Email address cannot be empty.")
    @Email(message = "Incorrect email address.")
    @Size(max = 50, message = "The email length cannot be more than 50 characters.")
    private String email;
}