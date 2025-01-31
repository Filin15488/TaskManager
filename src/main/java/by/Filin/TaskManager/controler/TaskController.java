package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.exception.ExceptionResponse;
import by.Filin.TaskManager.DTO.response.ResponseBody;
import by.Filin.TaskManager.DTO.task.*;
import by.Filin.TaskManager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task Controller", description = "Контроллер для управления тасками")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Получение таски по её ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Таск успешно найден",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = TaskDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Таск с указанным ID не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @GetMapping("/{id}")
    public TaskDTO getTaskById(@Parameter(description = "ID тега", required = true) @PathVariable Long id) {
        return taskService.getTaskById(id);
    }


    @Operation(description = "Получение полного списка тасок", summary = "Возвращает список тасок",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список всех тасок успешно получен",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class)))
                    )
            }

    )
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation( summary = "Добавление тега к созданной таске",
            description = "Комбинация таск-тег должна быть уникальна",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Тег успешно добавлен к таске",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Таска или тег с указанными id не найдена",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Комбинация таск-тег уже присутствует",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @PostMapping("/{id}/addTag")
//    public ResponseEntity<?> addTagToTask(@RequestBody TagTaskRequest request) {
    public ResponseEntity<?> addTagToTask(@Parameter(description = "Id таски", required = true) @PathVariable Long id,
                                          @Parameter(description = "Id тега", required = true) @Valid @RequestBody TaskLinkTagDTO request) {
        taskService.addTagToTask(id, request.getTagId());
        return ResponseEntity.ok(
                ResponseBody.builder()
                        .message("Tag added successfully")
                        .build()
        );
    }

    @Operation(summary = "Удаление тега у таски",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Тег успешно удалён"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Комбинация таска-тег не найдена",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @DeleteMapping("/{id}/removeTag")
//    public ResponseEntity<?> removeTagFromTask (@RequestBody TagTaskRequest request) {
    public ResponseEntity<?> removeTagFromTask (@PathVariable Long id, @RequestBody @Valid TaskLinkTagDTO request) {
        taskService.removeTagFromTask(id, request.getTagId());
        return new ResponseEntity<>(ResponseBody.builder()
                .message("Tag removed successfully")
                .build(), HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Создание Таски",
            description = "Создаёт новую таску. Для создания необходим минимальный набор параметров: title, description, categoryId, priority, userId. Остальные параметры являются необязательными ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Таска успешно создана",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Один из обязательных параметров не найден",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            })

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(      @Parameter(description = "Сущность таски на создание ", required = true)
                                                    @RequestBody @Valid TaskRequestDTO requestDTO) {
        TaskDTO createdTask = taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @Operation( summary = "Обновление таски по её id",
            description = "Все поля DTO являются необязательными",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Таск успешно обновлён",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = TaskDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "В случае если значение указано (ID таски или ID категории), но оно не найдено",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }

    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@Parameter(description = "Id таски", required = true) @PathVariable Long id,
                                        @Parameter(description = "Обновляемые поля", required = true)
                                        @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskUpdateDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Удаление таски",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Таска успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Таски с указанным Id не найдено",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
                    )

            }

    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

}
