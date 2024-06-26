package net.serg.resourceprocessor.client;

import net.serg.resourceprocessor.dto.SongMetadataDto;

public interface SongServiceClient {

    void saveMetadata(SongMetadataDto songMetadataDto);
    
    void deleteMetadataByResourceId(String idsSeparatedByComma);

}
