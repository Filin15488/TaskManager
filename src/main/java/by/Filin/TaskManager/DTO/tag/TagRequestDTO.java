package by.Filin.TaskManager.DTO.tag;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagRequestDTO {
    private String name;
    private Long userId; // ID пользователя, к которому привязывается тег
}