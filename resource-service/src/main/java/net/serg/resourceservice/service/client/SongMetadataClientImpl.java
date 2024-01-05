package net.serg.resourceservice.service.client;

import lombok.RequiredArgsConstructor;
import net.serg.resourceservice.dto.SongMetadataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SongMetadataClientImpl implements SongServiceClient{

    private final RestTemplate restTemplate;

    @Value("${clients.song-service.url}")
    private String url;

    @Override
    public void saveMetadata(SongMetadataDto songMetadataDto) {

        //var song = SongMetadataDto.builder().album("test-album").name("test-name").build();

        var httpEntity = new HttpEntity<>(songMetadataDto);

        restTemplate.postForEntity(url, httpEntity, SongMetadataDto.class);
    }
}
