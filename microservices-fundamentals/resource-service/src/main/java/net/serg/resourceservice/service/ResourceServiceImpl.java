package net.serg.resourceservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceservice.client.RabbitMqSender;
import net.serg.resourceservice.entity.AudioLocation;
import net.serg.resourceservice.exception.ResourceNotFoundException;
import net.serg.resourceservice.exception.ResourceServiceException;
import net.serg.resourceservice.repository.ResourceRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private static final String BUCKET_NAME = "audiofiles";
    private final AmazonS3 amazonS3;
    private final ResourceRepository resourceRepository;
    private final RabbitMqSender rabbitMQSender;

    public Long saveAudio(MultipartFile audioFile) {
        try {
            InputStream inputStream = audioFile.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(audioFile.getContentType());
            metadata.setContentLength(audioFile.getSize());

            if (!amazonS3.doesBucketExistV2(BUCKET_NAME)) {
                amazonS3.createBucket(BUCKET_NAME);
            }

            String fileName = audioFile.getOriginalFilename();
            fileName = fileName.replace(" ", "_");
            amazonS3.putObject(BUCKET_NAME, fileName, inputStream, metadata);
            String url = "https://s3.amazonaws.com/audiofiles/" + fileName;
            var audioLocation = resourceRepository.save(AudioLocation.builder().url(url).build());

            rabbitMQSender.send("resourceIds", String.valueOf(audioLocation.getId()));
            return audioLocation.getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceServiceException("Could not save audio. " + e.getMessage());
        }
    }

    @Override
    public byte[] getAudioByFilename(String fileName) {
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, fileName);

        try (S3ObjectInputStream s3is = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] readBuf = new byte[1024];
            int readLen = 0;
            while ((readLen = s3is.read(readBuf)) > 0) {
                outputStream.write(readBuf, 0, readLen);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResourceServiceException("Could not get audio. " + e.getMessage());
        }
    }

    @Override
    public String getAudioUrlById(Long id) {
        var audioLocation = resourceRepository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException(String.format("Audio location with ID %d is not found", id)));
        return audioLocation.getUrl();

    }

    @Override
    public void deleteAudio(Set<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource getAudioById(Long id) {
        var audioUrl = getAudioUrlById(id);
        String[] parts = audioUrl.split("/");
        String fileName = parts[parts.length - 1];
        S3Object s3object = amazonS3.getObject(BUCKET_NAME, fileName);
        InputStream is = s3object.getObjectContent();

        return new InputStreamResource(is);
    }
}
