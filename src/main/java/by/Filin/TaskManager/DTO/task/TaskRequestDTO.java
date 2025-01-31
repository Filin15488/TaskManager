package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.entity.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {
    @Schema(required = true)
    @NotBlank(message = "Task title cannot be empty.")
    @Size(min = 2, max = 100, message = "Task title length must be between 2 and 100 characters. ")
    private String title;
    @Schema(required = true)
    @NotBlank(message = "Task description cannot be empty")
    private String description;
    @Schema(required = true)
    private Long categoryId;
    @Schema(required = false)
    private Priority priority;
    @Schema(required = false)
    private OffsetDateTime deadline;
    @Schema(required = false, description = "Массив id-шек тегов с которыми может создаться таска", example = "[1,2,3]")
    private List<Long> tags;
    @Schema(required = true)
    @NotNull(message = "Task userId cannot be empty")
    private Long userId;
}
