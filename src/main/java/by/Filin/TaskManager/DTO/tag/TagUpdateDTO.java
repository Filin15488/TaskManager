package by.Filin.TaskManager.DTO.tag;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateDTO {
    private String name; // Новое имя тега (опционально)
}
