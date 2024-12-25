package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.category.CategoryRequestDTO;
import by.Filin.TaskManager.DTO.category.CategoryUpdateDTO;
import by.Filin.TaskManager.entity.Category;
import by.Filin.TaskManager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<?> getAllCategories() {
        return categoryService.getAllCategories();
    };
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryDTO result = categoryService.getCategoryById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public  ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO newCategory = categoryService.createCategory(categoryRequestDTO);
        newCategory.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        CategoryDTO result = categoryService.updateCategory(id, categoryUpdateDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
