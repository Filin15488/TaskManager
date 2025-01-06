package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.response.ResponseBody;
import by.Filin.TaskManager.DTO.task.TagTaskRequest;
import by.Filin.TaskManager.DTO.task.TaskDTO;
import by.Filin.TaskManager.DTO.task.TaskRequestDTO;
import by.Filin.TaskManager.DTO.task.TaskUpdateDTO;
import by.Filin.TaskManager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/addTag")
    public ResponseEntity<?> addTagToTask(@RequestBody TagTaskRequest request) {
        taskService.addTagToTask(request.getTaskId(), request.getTagId());
        return ResponseEntity.ok(
                ResponseBody.builder()
                        .message("Tag added successfully")
                        .build()
        );
    }

    @DeleteMapping("/removeTag")
    public ResponseEntity<?> removeTagFromTask (@RequestBody TagTaskRequest request) {
        taskService.removeTagFromTask(request.getTaskId(), request.getTagId());
        return new ResponseEntity<>(ResponseBody.builder()
                .message("Tag removed successfully")
                .build(), HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskRequestDTO requestDTO) {
        TaskDTO createdTask = taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskUpdateDTO);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

}
