package by.Filin.TaskManager.DTO.task;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.entity.enums.Priority;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Long UserId;
    private OffsetDateTime deadline;
    private OffsetDateTime createAt;
    private OffsetDateTime updateAt;
    private CategoryDTO category;
    private List<TagDTO> tags;
}
