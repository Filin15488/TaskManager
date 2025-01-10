package by.Filin.TaskManager.DTO.tag;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String name;
    @Schema(description = "ID пользователя, к которому привязывается тег", example = "1", required = true)
    private Long userId; // ID пользователя, к которому привязывается тег
}