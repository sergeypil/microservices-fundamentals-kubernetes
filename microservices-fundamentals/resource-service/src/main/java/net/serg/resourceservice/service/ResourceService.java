package net.serg.resourceservice.service;

import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {
    
    Long saveAudio(MultipartFile audioFile);

    byte[] getAudioByFilename(String fileName);
    
    String getAudioUrlById(Long id);
    void deleteAudio(Set<Long> ids);
}
