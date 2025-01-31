package by.Filin.TaskManager.DTO.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 30, message = "The name length must be between 2 and 30 characters.")
    private String name;
    @NotNull(message = "User id cannot be empty")
    @Schema(description = "ID пользователя, к которому привязывается создаваемая категория", example = "1", required = true)
    private Long userId;
}
