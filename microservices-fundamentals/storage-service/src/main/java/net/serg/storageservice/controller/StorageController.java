package net.serg.storageservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.storageservice.dto.RemovedStoragesDto;
import net.serg.storageservice.dto.StorageDto;
import net.serg.storageservice.service.StorageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@RestController
@RequestMapping("storages")
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StorageDto saveStorageObject(@RequestBody StorageDto storageDto) {
        log.info("Saving storage: {}", storageDto);
        return storageService.saveStorage(storageDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<StorageDto> getStorages(@RequestParam(required = false) StorageDto.StorageType type) {
        log.info("Getting storages by type: {}", type);
        if (Objects.isNull(type)) {
            return storageService.getAllStorages();
        }
        return storageService.getStorageByType(type.name());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RemovedStoragesDto deleteStorage(@RequestParam String id) {
        log.info("Deleting storages by id: {}", id);
        if (id == null || id.length() > 200) {
            throw new RuntimeException("ids length exceed!");
        }
        return RemovedStoragesDto
            .builder()
            .ids(storageService.removeStorages(Stream
                                                   .of(id.split(","))
                                                   .map(String::strip)
                                                   .map(Long::valueOf)
                                                   .toArray(Long[]::new)))
            .build();
    }
}
