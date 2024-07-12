package net.serg.resourceservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceservice.dto.StorageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class StorageClient {
    
    private final RestTemplate restTemplate;

    @Value("${clients.storage-service.url}")
    private String url;

    @CircuitBreaker(name = "storageService", fallbackMethod ="getStubStorages")
    public List<StorageDto> getStorages() {
        log.info("Calling storage-service to get storages");
        ResponseEntity<List<StorageDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    private List<StorageDto> getStubStorages(Exception e) {
        return List.of(
            StorageDto.builder()
                .id(1L)
                .type(StorageDto.StorageType.STAGING)
                .bucket("staging-bucket")
                .path("/files")
                .build(),
            StorageDto.builder()
                .id(2L)
                .type(StorageDto.StorageType.PERMANENT)
                .bucket("permanent-bucket")
                .path("/files")
                .build());
    }
}
