package net.serg.resourceservice.service.client;

import net.serg.resourceservice.dto.SongMetadataDto;

import java.util.Set;

public interface SongServiceClient {

    void saveMetadata(SongMetadataDto songMetadataDto);
    
    void deleteMetadataByResourceId(String idsSeparatedByComma);

}
