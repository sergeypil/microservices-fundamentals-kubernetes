package net.serg.resourceservice.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceservice.client.RabbitMqSender;
import net.serg.resourceservice.client.StorageClient;
import net.serg.resourceservice.dto.StorageDto;
import net.serg.resourceservice.entity.AudioLocation;
import net.serg.resourceservice.exception.ResourceNotFoundException;
import net.serg.resourceservice.exception.ResourceServiceException;
import net.serg.resourceservice.repository.ResourceRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    
    private static final String STATUS_STAGING = "STAGING";
    private static final String STATUS_PERMANENT = "PERMANENT";
    private final AmazonS3 amazonS3;
    private final ResourceRepository resourceRepository;
    private final RabbitMqSender rabbitMQSender;
    private final StorageClient storageClient;

    public Long saveAudio(MultipartFile audioFile) {
        List<StorageDto> storages = storageClient.getStorages();
        var storage = storages
            .stream()
            .filter(s -> STATUS_STAGING.equals(s.getType().name()))
            .findFirst().orElseThrow();
        var bucketName = storage.getBucket();
        
        try {
            InputStream inputStream = audioFile.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(audioFile.getContentType());
            metadata.setContentLength(audioFile.getSize());
            var audioLocation = resourceRepository.save(AudioLocation.builder()
                                                            .bucketName(bucketName)
                                                            .path(storage.getPath())
                                                            .build());
            
            if (!amazonS3.doesBucketExistV2(bucketName)) {
                amazonS3.createBucket(bucketName);
            }
            var key = storage.getPath() + "/" + audioLocation.getId();
            amazonS3.putObject(bucketName, key, inputStream, metadata);
            
            rabbitMQSender.send("resourceIds", String.valueOf(audioLocation.getId()));
            return audioLocation.getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceServiceException("Could not save audio. " + e.getMessage());
        }
    }

    @Override
    public String getAudioUrlById(Long id) {
        var audioLocation = resourceRepository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException(String.format("Audio location with ID %d is not found", id)));
        return "https://s3.amazonaws.com/" + audioLocation.getBucketName() + audioLocation.getPath() + "/" + id;

    }

    @Override
    public void deleteAudio(Set<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource getAudioById(Long id) {
        var audioLocation = resourceRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException(String.format("Audio location with ID %d is not found", id)));
        S3Object s3object = amazonS3.getObject(audioLocation.getBucketName(), audioLocation.getPath() + "/" + id);
        InputStream is = s3object.getObjectContent();

        return new InputStreamResource(is);
    }
    
    @Override
    @Transactional
    public void moveAudioById(Long id) {
        var storages = storageClient.getStorages();
        var storage = storages
            .stream()
            .filter(s -> STATUS_PERMANENT.equals(s.getType().name()))
            .findFirst().orElseThrow();
        var bucketName = storage.getBucket();
        
        var audioLocation = resourceRepository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException(String.format("Audio location with ID %d is not found", id)));
        var sourceKey = audioLocation.getPath() + "/" + audioLocation.getId();
        var targetKey = storage.getPath() + "/" + audioLocation.getId();
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
        amazonS3.copyObject(audioLocation.getBucketName(), sourceKey, bucketName, targetKey);
        amazonS3.deleteObject(audioLocation.getBucketName(), sourceKey);
        audioLocation.setBucketName(bucketName);
        audioLocation.setPath(storage.getPath());
        resourceRepository.save(audioLocation);
    }
}
