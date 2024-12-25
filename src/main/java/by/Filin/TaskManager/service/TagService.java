package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.tag.TagRequestDTO;
import by.Filin.TaskManager.DTO.tag.TagUpdateDTO;
import by.Filin.TaskManager.entity.Tag;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.TagMapper;
import by.Filin.TaskManager.repository.TagRepository;
import by.Filin.TaskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, UserRepository userRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.tagMapper = tagMapper;
    }

    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> tagMapper.toDTO(tag)).toList();
    }

    @Transactional(readOnly = true)
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id " + id + " not found"));
        return tagMapper.toDTO(tag);
    }

    @Transactional
    public TagDTO createTag(TagRequestDTO tagRequestDTO) {
        User user = userRepository.findById(tagRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + tagRequestDTO.getUserId() + " not found"));

        Tag tag = new Tag();
        tag.setName(tagRequestDTO.getName());
        tag.setUser(user);

        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }

    @Transactional
    public TagDTO updateTag(Long id, TagUpdateDTO tagUpdateDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id " + id + " not found"));

        if (tagUpdateDTO.getName() != null) {
            tag.setName(tagUpdateDTO.getName());
        }

        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id " + id + " not found"));

        tagRepository.delete(tag);
    }
}
