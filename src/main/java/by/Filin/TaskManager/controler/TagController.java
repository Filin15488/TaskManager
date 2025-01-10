package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.exception.ExceptionResponse;
import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.tag.TagRequestDTO;
import by.Filin.TaskManager.DTO.tag.TagUpdateDTO;
import by.Filin.TaskManager.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

@Tag(name = "Tag Controller", description = "Контроллер для управления тегами")
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(
            summary = "Получить все теги",
            description = "Возвращает список всех тегов",
            responses = {
                    @ApiResponse(
                            description = "Успешное получение всех тегов",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TagDTO.class))
                            )
                    )
            }
    )
    @GetMapping
    public List<TagDTO> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return tags;
    }

    @Operation(summary = "Получить тег по ID", description = "Возвращает данные тега по его уникальному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение тега",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Тег не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@Parameter(description = "ID тега", required = true)
                                             @PathVariable Long id) {
        TagDTO tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @Operation(summary = "Создать новый тег", description = "Создает новый тег на основе предоставленных данных",
    responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тег успешно создан",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, создающий тег, не найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            )
    }
    )
    @PostMapping
    public ResponseEntity<TagDTO> createTag(@Parameter(description = "Данные для создания тега", required = true)
                                                @RequestBody TagRequestDTO tagRequestDTO) {
        TagDTO newTag = tagService.createTag(tagRequestDTO);
        return ResponseEntity.status(201).body(newTag); // 201 Created
    }

    @Operation(summary = "Обновить данные тега", description = "Обновляет данные существующего тега по его ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Тег успешно обновлен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Тег не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@Parameter(description = "ID тега", required = true) @PathVariable Long id,
                                            @Parameter(description = "Данные для обновления тега", required = true)
                                            @RequestBody TagUpdateDTO tagUpdateDTO) {
        TagDTO updatedTag = tagService.updateTag(id, tagUpdateDTO);
        return ResponseEntity.ok(updatedTag);
    }

    @Operation(summary = "Удалить тег", description = "Удаляет тег по его ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Тег успешно удалён"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Тег не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@Parameter(description = "ID тега", required = true) @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
