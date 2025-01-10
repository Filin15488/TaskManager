package by.Filin.TaskManager.mapper;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.DTO.user.UserRequestDTO;
import by.Filin.TaskManager.entity.Category;
import by.Filin.TaskManager.entity.Tag;
import by.Filin.TaskManager.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if (user.getTags() != null) {
            dto.setTags(user.getTags().stream()
                    .map(this::toTagDTO)
                    .collect(Collectors.toList()));
        }

        if (user.getCategories() != null) {
            dto.setCategories(user.getCategories().stream()
                    .map(this::toCategoryDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private TagDTO toTagDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setUserID(tag.getId());
        return dto;
    }

    private CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUserId(category.getUser().getId());
        return dto;
    }

    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

}

