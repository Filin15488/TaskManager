package by.Filin.TaskManager.DTO.category;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String name;
    private OffsetDateTime createdAt;
    private Long userId;
}
