package by.Filin.TaskManager.DTO.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskLinkTagDTO {
    @NotNull(message = "Id tag not be empty")
    private Long tagId;
}
