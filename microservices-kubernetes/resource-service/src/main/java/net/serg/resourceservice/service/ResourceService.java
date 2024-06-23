package net.serg.resourceservice.service;

import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    @Transactional
    Long saveAudio(MultipartFile mp3File);

    byte[] getAudio(long id);

    void deleteAudio(Set<Long> ids);
}
