package by.Filin.TaskManager.DTO.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 30, message = "The name length must be between 2 and 30 characters.")
    @Schema(description = "Новое название категории", example = "string", required = true)
    public String name;
}
