package by.Filin.TaskManager.repository;

import by.Filin.TaskManager.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    Optional<List<TaskTag>> findAllByTaskId(Long taskId);
    Optional<List<TaskTag>> findAllByTagId(Long tagId);
    Optional<TaskTag> findByTaskIdAndTagId(Long taskId, Long tagId);
    Optional<TaskTag> findByTagId(Long tagId);
}
