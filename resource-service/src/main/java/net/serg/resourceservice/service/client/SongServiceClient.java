package net.serg.resourceservice.service.client;

import net.serg.resourceservice.dto.SongMetadataDto;

public interface SongServiceClient {

    void saveMetadata(SongMetadataDto songMetadataDto);

}
