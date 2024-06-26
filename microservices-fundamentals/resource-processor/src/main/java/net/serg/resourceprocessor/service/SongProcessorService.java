package net.serg.resourceprocessor.service;

import net.serg.resourceprocessor.dto.SongMetadataDto;

import java.io.InputStream;

public interface SongProcessorService {

    SongMetadataDto extractAudioMetadata(byte[] audioData, Long audioId);
    SongMetadataDto extractAudioMetadataFromStream(InputStream inputStream, Long audioId);
}
