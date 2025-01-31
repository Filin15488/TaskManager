package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.entity.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateDTO {
    @Schema(required = true)
    @NotBlank(message = "Task title cannot be empty.")
    @Size(min = 2, max = 100, message = "Task title length must be between 2 and 100 characters. ")
    private String title;
    @Schema(required = true)
    @NotBlank(message = "Task description cannot be empty")
    private String description;
    private Long categoryId;
    private Priority priority;
    private OffsetDateTime deadline;
}
