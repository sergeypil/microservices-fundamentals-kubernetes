package net.serg.resourceservice.controller;

import jakarta.servlet.ServletContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.serg.resourceservice.dto.IdResponse;
import net.serg.resourceservice.service.ResourceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path = "/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<IdResponse> saveAudio(@RequestPart MultipartFile mp3File) {
        var id = resourceService.saveAudio(mp3File);
        var response = new IdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<byte[]> getAudio(@PathVariable Long id, ServletContext servletContext) {
        var audioData = resourceService.getAudio(id);
        return ResponseEntity.ok(audioData);
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
