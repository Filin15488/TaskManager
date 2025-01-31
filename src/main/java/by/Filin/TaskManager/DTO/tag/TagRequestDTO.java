package by.Filin.TaskManager.DTO.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagRequestDTO {

    @Schema(description = "Название тега", example = "Important", required = true)
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 30, message = "The name length must be between 2 and 30 characters.")
    private String name;

    @NotNull(message = "User id cannot be empty")
    @Schema(description = "ID пользователя, к которому привязывается тег", example = "1", required = true)
    private Long userId; // ID пользователя, к которому привязывается тег
}