package net.serg.resourceservice.controller;

import lombok.RequiredArgsConstructor;
import net.serg.resourceservice.client.StorageClient;
import net.serg.resourceservice.dto.StorageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/resources")
@RequiredArgsConstructor
public class StorageController {
    
    private final StorageClient storageClient;
    
    @GetMapping("/storages")
    public ResponseEntity<List<StorageDto>> pingStorage() {
        return ResponseEntity.ok(storageClient.getStorages());
        
    }
}
