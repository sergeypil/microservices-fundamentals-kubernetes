package net.serg.resourceprocessor.service;

import net.serg.resourceprocessor.dto.SongMetadataDto;
import org.springframework.web.multipart.MultipartFile;

public interface SongProcessorService {

    SongMetadataDto extractAudioMetadata(MultipartFile audioFile);
}
