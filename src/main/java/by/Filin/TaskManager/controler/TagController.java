package by.Filin.TaskManager.controler;

import by.Filin.TaskManager.DTO.tag.TagDTO;
import by.Filin.TaskManager.DTO.tag.TagRequestDTO;
import by.Filin.TaskManager.DTO.tag.TagUpdateDTO;
import by.Filin.TaskManager.entity.Tag;
import by.Filin.TaskManager.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDTO> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return tags;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        TagDTO tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO) {
        TagDTO newTag = tagService.createTag(tagRequestDTO);
        return ResponseEntity.status(201).body(newTag); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagUpdateDTO tagUpdateDTO) {
        TagDTO updatedTag = tagService.updateTag(id, tagUpdateDTO);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
