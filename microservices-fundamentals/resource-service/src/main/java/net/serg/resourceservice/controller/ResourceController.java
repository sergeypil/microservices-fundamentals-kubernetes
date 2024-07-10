package net.serg.resourceservice.controller;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.serg.resourceservice.dto.IdResponse;
import net.serg.resourceservice.service.ResourceService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<IdResponse> saveAudio(@RequestPart MultipartFile audioFile) {
        var id = resourceService.saveAudio(audioFile);
        var response = new IdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/location/{id}")
    public ResponseEntity<String> getAudioUrlById(@PathVariable Long id) {
        var audioUrl = resourceService.getAudioUrlById(id);
        return ResponseEntity.ok(audioUrl);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> getAudioById(@PathVariable Long id) {
        var resource = resourceService.getAudioById(id);
        return ResponseEntity.ok(resource);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> moveAudioById(@PathVariable Long id) {
        resourceService.moveAudioById(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Set<Long>> deleteAudioByIds(@RequestParam(name = "id") String idsSeparatedByComma) {
        var ids = Arrays.stream(idsSeparatedByComma.split(","))
            .map(String::trim)
            .map(Long::valueOf)
            .collect(Collectors.toSet());
        resourceService.deleteAudio(ids);
        return ResponseEntity.ok(ids);
    }
}
