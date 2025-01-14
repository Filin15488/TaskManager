package by.Filin.TaskManager.DTO.user;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.role.RoleDTO;
import by.Filin.TaskManager.DTO.tag.TagDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<TagDTO> tags;
    private List<CategoryDTO> categories;
    private RoleDTO role;
}
