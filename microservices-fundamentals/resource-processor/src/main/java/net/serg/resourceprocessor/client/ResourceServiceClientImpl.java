package net.serg.resourceprocessor.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceClientImpl implements ResourceServiceClient {

    private final RestTemplate restTemplate;

    @Value("${clients.resource-service.url}")
    private String url;

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Override
    public byte[] getAudioById(Long id) {
        log.info("Calling song-service to get audio");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
            url + "/" + id, HttpMethod.GET, entity, byte[].class);

        return response.getBody();
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Override
    public InputStream getAudioStreamById(Long id) throws IOException {
        log.info("Calling song-service to get audio stream");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
            url + "/" + id, HttpMethod.GET, entity, byte[].class);

        byte[] responseBody = response.getBody();
        if (responseBody != null) {
            return new ByteArrayInputStream(responseBody);
        } else {
            throw new IOException("Resource service returned empty response");
        }
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public void notifyResourceService(Long id) {
        log.info("Calling song-service to move audio to another bucket");
        restTemplate.exchange(url + "/" + id, HttpMethod.PUT, null, Void.class);
    }
}
