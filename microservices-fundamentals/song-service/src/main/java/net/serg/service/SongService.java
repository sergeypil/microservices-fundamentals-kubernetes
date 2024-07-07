package net.serg.service;

import java.util.Set;
import net.serg.entity.SongMetadata;
import org.springframework.transaction.annotation.Transactional;

public interface SongService {
    
    SongMetadata getSongMetadataByResourceId(long id);
    
    Long saveSongMetadata(SongMetadata songMetadata);
    
        void deleteSongMetadata(Set<Long> ids);
}
