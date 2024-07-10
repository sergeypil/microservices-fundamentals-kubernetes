package net.serg.resourceservice.service;

import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {
    
    Long saveAudio(MultipartFile audioFile);
    String getAudioUrlById(Long id);
    void deleteAudio(Set<Long> ids);
    Resource getAudioById(Long id);
    void moveAudioById(Long id);
}
