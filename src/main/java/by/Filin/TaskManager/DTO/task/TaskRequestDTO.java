package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.entity.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String title;
    @Schema(required = true)
    private String description;
    @Schema(required = true)
    private Long categoryId;
    @Schema(required = true)
    private Priority priority;
    @Schema(required = false)
    private OffsetDateTime deadline;
    @Schema(required = false, description = "Массив id-шек тегов с которыми может создаться таска", example = "[1,2,3]")
    private List<Long> tags;
    @Schema(required = true)
    private Long userId;
}
