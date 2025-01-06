package by.Filin.TaskManager.DTO.task;

import lombok.Data;

@Data
public class TagTaskRequest {
    private Long taskId;
    private Long tagId;
}
