package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.entity.enums.Priority;
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
    private String title;
    private String description;
    private Long categoryId;
    private OffsetDateTime deadline;
    private Priority priority;
    private List<Long> tags;
    private Long userId;
}
