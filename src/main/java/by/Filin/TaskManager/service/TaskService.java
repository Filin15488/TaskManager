package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.task.TaskDTO;
import by.Filin.TaskManager.DTO.task.TaskRequestDTO;
import by.Filin.TaskManager.DTO.task.TaskUpdateDTO;
import by.Filin.TaskManager.entity.*;
import by.Filin.TaskManager.mapper.TaskMapper;
import by.Filin.TaskManager.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final TaskMapper taskMapper;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TaskService(CategoryRepository categoryRepository, TaskRepository taskRepository, TaskTagRepository taskTagRepository, TaskMapper taskMapper, TagRepository tagRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
        this.taskTagRepository = taskTagRepository;
        this.taskMapper = taskMapper;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found")
        );
        return taskMapper.toDTO(task);
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> taskMapper.toDTO(task)).toList();
    }

    @Transactional
    public void addTagToTask (Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + taskId + " not found")
        );

        Tag tag = tagRepository.findById(tagId).orElseThrow(
                () -> new EntityNotFoundException("Tag with id " + tagId + " not found")
        );

        TaskTag taskTag = new TaskTag();
        taskTag.setTask(task);
        taskTag.setTag(tag);

        try {
            taskTagRepository.save(taskTag);
        }
        catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                throw new DataIntegrityViolationException(
                        "The tag has already been added to the task earlier",
                        e);
            }
            throw e;
        }

    }
    @Transactional
    public void removeTagFromTask (Long taskId, Long tagId) {
        TaskTag taskTagToRemove = taskTagRepository.findByTaskIdAndTagId(taskId, tagId).orElseThrow(
                () -> new EntityNotFoundException("Combination task " + taskId +" tag " + tagId +" not found")
        );
        taskTagRepository.delete(taskTagToRemove);
    }

    @Transactional
    public TaskDTO createTask(@Valid TaskRequestDTO requestDTO) {
        // Проверка существования пользователя
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + requestDTO.getUserId()));

        // Проверка существования категории, если она указана
        Category category = null;
        if (requestDTO.getCategoryId() != null) {
            category = categoryRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + requestDTO.getCategoryId()));
        }

        // Создание новой задачи
        Task task = new Task();
        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());
        task.setPriority(requestDTO.getPriority());
        task.setDeadline(requestDTO.getDeadline());
        task.setUser(user);
        task.setCategory(category);

        // Сохранение задачи в базе данных
        Task savedTask = taskRepository.save(task);

        if (requestDTO.getTags() != null && !requestDTO.getTags().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(requestDTO.getTags());
            if (tags.size() != requestDTO.getTags().size()) {
                throw new IllegalArgumentException("One or more tags not found");
            }
            List<TaskTag> taskTags = tags.stream()
                    .map(tag -> new TaskTag(savedTask, tag))
                    .toList();
            taskTagRepository.saveAll(taskTags);
            task.setTaskTags(taskTags);
        }

        // Преобразование в DTO и возврат
        return taskMapper.toDTO(savedTask);
    }

    @Transactional
    public TaskDTO updateTask(Long id, @Valid TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found")
        );

        updateIfNotNull(taskUpdateDTO.getTitle(), task::setTitle);
        updateIfNotNull(taskUpdateDTO.getDescription(), task::setDescription);
        updateIfNotNull(taskUpdateDTO.getPriority(), task::setPriority);
        updateIfNotNull(taskUpdateDTO.getDeadline(), task::setDeadline);

        if (taskUpdateDTO.getCategoryId() != null) {
            task.setCategory(categoryRepository.findById(taskUpdateDTO.getCategoryId()).orElseThrow(
                    () -> new IllegalArgumentException("Category not found with id: " + taskUpdateDTO.getCategoryId())
            ));
        }

//        task.setUpdatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.parse(OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MICROS).format(DateTimeFormatter.ISO_INSTANT)));

        return taskMapper.toDTO(taskRepository.save(task));
    }

    private <T> void updateIfNotNull (T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    @Transactional
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found")
        );

        List<TaskTag> taskTagsToRemove = taskTagRepository.findAllByTaskId(id).orElse(new ArrayList<>());

        if (!taskTagsToRemove.isEmpty()) {
            taskTagRepository.deleteAll(taskTagsToRemove);
        }

        taskRepository.delete(task);
    }
}
