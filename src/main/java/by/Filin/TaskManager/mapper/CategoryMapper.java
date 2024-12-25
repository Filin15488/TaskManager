package by.Filin.TaskManager.mapper;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.entity.Category;
import by.Filin.TaskManager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toDTO(Category category) {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(category.getId());
    categoryDTO.setName(category.getName());
    categoryDTO.setCreatedAt(category.getCreatedAt());
    categoryDTO.setUserId(category.getUser().getId());
    return categoryDTO;
    }
    public Category toEntity(CategoryDTO categoryDTO, User user) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUser(user);
        return category;
    }
}
