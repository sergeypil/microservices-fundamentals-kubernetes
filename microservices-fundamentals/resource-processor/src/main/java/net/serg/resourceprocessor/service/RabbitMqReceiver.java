package net.serg.resourceprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceprocessor.client.ResourceServiceClientImpl;
import net.serg.resourceprocessor.client.SongServiceClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqReceiver {
    private final SongProcessorService songProcessorService;
    private final ResourceServiceClientImpl resourceServiceClient;
    private final SongServiceClient songServiceClient;
    

    @RabbitListener(queues = "resourceIds")
    public void receive(Long audioId) throws IOException, InterruptedException {
        log.info("Received message with audioId: {}", audioId);
//        byte[] audioData = resourceServiceClient.getAudioById(audioId);// read all bytes at once
//        var songMetadata = songProcessorService.extractAudioMetadata(audioData, audioId);
        InputStream audioStreamById = resourceServiceClient.getAudioStreamById(audioId);
        var songMetadata = songProcessorService.extractAudioMetadataFromStream(audioStreamById, audioId);
        log.info("Saving metadata to song-service");
        songServiceClient.saveMetadata(songMetadata);
    }
}