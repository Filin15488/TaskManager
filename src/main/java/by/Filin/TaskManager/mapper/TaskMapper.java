package by.Filin.TaskManager.mapper;

import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.task.TaskDTO;
import by.Filin.TaskManager.entity.Task;
import by.Filin.TaskManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    @Autowired
    public TaskMapper(CategoryMapper categoryMapper, TagMapper tagMapper) {
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setDeadline(task.getDeadline());
        dto.setCreateAt(task.getCreatedAt());
        dto.setUpdateAt(task.getUpdatedAt());
        dto.setUserId(task.getUser().getId());
        dto.setCategory(categoryMapper.toDTO(task.getCategory()));

        if (task.getTaskTags() != null) {
            List<TagDTO> tagDTOs = task.getTaskTags().stream()
                    .map(taskTag -> tagMapper.toDTO(taskTag.getTag()))
                    .collect(Collectors.toList());

            dto.setTags(tagDTOs);
        }

        return dto;
    }

//    public Task toEntity(TaskDTO dto) {
//        Task task = new Task();
//
//        return task;
//
//    }
}
