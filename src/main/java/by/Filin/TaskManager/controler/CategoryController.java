package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.category.CategoryDTO;
import by.Filin.TaskManager.DTO.category.CategoryRequestDTO;
import by.Filin.TaskManager.DTO.category.CategoryUpdateDTO;
import by.Filin.TaskManager.DTO.exception.ExceptionResponse;
import by.Filin.TaskManager.entity.Category;
import by.Filin.TaskManager.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@Tag(name = "Category controller", description = "Контроллер для управления категориями")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "Получение списка всех категорий", description = "Возвращает массив всех категорий",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение списка всех категорий",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
            )
            }
    )
    @GetMapping
    public List<?> getAllCategories() {
        return categoryService.getAllCategories();
    };

    @Operation(summary = "Получение категории по её ID", description = "Возвращает информацию о категории по её ID",
    responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение категории",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория с указанным ID не найдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@Parameter(description = "ID категории", required = true) @PathVariable Long id) {
        CategoryDTO result = categoryService.getCategoryById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Создание новой категории",
    responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Категория была успешно создана",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с указанным ID не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )

    }
    )
    @PostMapping
    public  ResponseEntity<?> createCategory(@Parameter(description = "создание новой категории на основе предоставленных данных", required = true)
                                             @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO newCategory = categoryService.createCategory(categoryRequestDTO);
        newCategory.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.status(201).body(newCategory);
    }

    @Operation(summary = "Обновление названия категории по её ID",
    responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Название категории успешно обновлено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория по указанному ID не найдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @Parameter(description = "ID категории", required = true)
            @PathVariable Long id,
            @Parameter(description = "Данные для обновления", required = true)
            @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        CategoryDTO result = categoryService.updateCategory(id, categoryUpdateDTO);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по её ID",
    responses = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Категория успешно удалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория с указанным ID не найдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@Parameter(description = "ID категории", required = true)
                                                @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
