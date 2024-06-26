package net.serg.resourceprocessor.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceprocessor.dto.SongMetadataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongMetadataClientImpl implements SongServiceClient{

    private final RestTemplate restTemplate;

    @Value("${clients.song-service.url}")
    private String url;

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Override
    public void saveMetadata(SongMetadataDto songMetadataDto) {
        log.info("Calling song-service to save metadata");
        var httpEntity = new HttpEntity<>(songMetadataDto);
        restTemplate.postForEntity(url, httpEntity, SongMetadataDto.class);
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Override
    public void deleteMetadataByResourceId(String idsSeparatedByComma) {
        log.info("Calling song-service to delete metadata");
        String deleteUrl = url + "?id=" + idsSeparatedByComma;
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Set.class);
    }
    
}
