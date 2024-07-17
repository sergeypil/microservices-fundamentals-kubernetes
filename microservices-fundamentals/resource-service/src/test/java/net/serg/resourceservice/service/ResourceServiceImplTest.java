package net.serg.resourceservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import net.serg.resourceservice.client.RabbitMqSender;
import net.serg.resourceservice.client.StorageClient;
import net.serg.resourceservice.dto.StorageDto;
import net.serg.resourceservice.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import net.serg.resourceservice.entity.AudioLocation;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock 
    private AmazonS3 amazonS3;
    @Mock 
    private ResourceRepository resourceRepository;
    @Mock 
    private RabbitMqSender rabbitMQSender;
    @Mock 
    private S3Object s3Object;
    @Mock
    private StorageClient storageClient;

    @InjectMocks
    ResourceServiceImpl service;

    @Test
    void shouldSaveAudio() {
        // Given
        MultipartFile file = new MockMultipartFile("file", "hello.wav", "audio/wav", "hello".getBytes());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        when(resourceRepository.save(any())).thenReturn(AudioLocation.builder().id(1L).build());
        when(storageClient.getStorages()).thenReturn(List.of(
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
                .build()
        ));
        
        // When
        service.saveAudio(file);

        // Then
        verify(amazonS3).putObject(any(), any(), any(), any());
        verify(rabbitMQSender).send(any(), any());
        verify(resourceRepository).save(any());
    }

    @Test
    void shouldReturnCorrectAudioUrlById(){
        // Given
        AudioLocation audioLocation = AudioLocation.builder().bucketName("permanent").path("/files").build();
        when(resourceRepository.findById(any())).thenReturn(Optional.of(audioLocation));

        // When
        String result = service.getAudioUrlById(1L);

        // Then
        assertThat(result).isEqualTo("https://s3.amazonaws.com/permanent/files/1");
        verify(resourceRepository).findById(any());
    }

    @Test
    void shouldReturnAudioById() {
        // Given  
        AudioLocation audioLocation = AudioLocation.builder().id(1L).bucketName("PERMANENT").path("files").build();
        when(resourceRepository.findById(any())).thenReturn(Optional.of(audioLocation));
        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(new ByteArrayInputStream(new byte[0]), null));

        // When
        Resource resource = service.getAudioById(1L);

        // Then
        assertInstanceOf(InputStreamResource.class, resource);
        verify(amazonS3).getObject(anyString(), anyString());
        verify(resourceRepository).findById(any());
    }
}