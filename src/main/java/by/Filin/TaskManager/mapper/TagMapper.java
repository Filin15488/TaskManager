package by.Filin.TaskManager.mapper;

import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.tag.TagRequestDTO;
import by.Filin.TaskManager.entity.Tag;
import by.Filin.TaskManager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        tagDTO.setUserID(tag.getUser().getId());
        return tagDTO;
    }

    public Tag toEntity(TagRequestDTO tagRequestDTO, User user) {
        Tag tag = new Tag();
        tag.setName(tagRequestDTO.getName());
        tag.setUser(user);
        return tag;
    }
}