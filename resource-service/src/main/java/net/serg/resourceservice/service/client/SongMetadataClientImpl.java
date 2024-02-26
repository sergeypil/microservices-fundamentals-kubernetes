package net.serg.resourceservice.service.client;

import lombok.RequiredArgsConstructor;
import net.serg.resourceservice.dto.SongMetadataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SongMetadataClientImpl implements SongServiceClient{

    private final RestTemplate restTemplate;

    @Value("${clients.song-service.url}")
    private String url;

    @Override
    public void saveMetadata(SongMetadataDto songMetadataDto) {
        var httpEntity = new HttpEntity<>(songMetadataDto);
        restTemplate.postForEntity(url, httpEntity, SongMetadataDto.class);
    }

    @Override
    public void deleteMetadataByResourceId(String idsSeparatedByComma) {
        
        String deleteUrl = url + "?id=" + idsSeparatedByComma;
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Set.class);
    }
    
}
