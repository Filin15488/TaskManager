package by.Filin.TaskManager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"task", "tag"})
@ToString(of = {"id", "task", "tag"})
@Entity
@DynamicInsert
@Table(name = "task_tag", schema = "public", uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id", "tag_id"})})
public class TaskTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public TaskTag(@NonNull Task task,@NonNull Tag tag) {
        this.tag = tag;
        this.task = task;
    }
}
