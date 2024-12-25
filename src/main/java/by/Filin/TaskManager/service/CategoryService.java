package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.category.CategoryRequestDTO;
import by.Filin.TaskManager.DTO.category.CategoryUpdateDTO;
import by.Filin.TaskManager.entity.Category;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.CategoryMapper;
import by.Filin.TaskManager.repository.CategoryRepository;
import by.Filin.TaskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> categoryMapper.toDTO(category)).toList();
    }
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        return categoryMapper.toDTO(category);
    }
    @Transactional
    public CategoryDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        User user = userRepository.findById(categoryRequestDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + categoryRequestDTO.getUserId() + " not found")
        );
        Category category = new Category();
        category.setUser(user);
        category.setName(categoryRequestDTO.getName());

        categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        if (categoryUpdateDTO.getName() != null) {
            category.setName(categoryUpdateDTO.getName());
        }
        categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(category);
    }

}