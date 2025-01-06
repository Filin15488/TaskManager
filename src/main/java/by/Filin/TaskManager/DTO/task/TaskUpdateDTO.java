package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.entity.enums.Priority;
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
    private String title;
    private String description;
    private Long categoryId;
    private Priority priority;
    private OffsetDateTime deadline;
}
