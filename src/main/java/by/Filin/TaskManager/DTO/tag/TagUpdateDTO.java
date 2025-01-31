package by.Filin.TaskManager.DTO.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateDTO {
    @Size(min = 2, max = 30, message = "The name length must be between 2 and 30 characters.")
    private String name; // Новое имя тега (опционально)
}
