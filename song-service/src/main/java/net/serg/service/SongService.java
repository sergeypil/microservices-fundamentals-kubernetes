package net.serg.service;

import java.util.Set;
import net.serg.entity.SongMetadata;
import org.springframework.transaction.annotation.Transactional;

public interface SongService {

    @Transactional(readOnly = true)
    SongMetadata getSongMetadata(long id);

    @Transactional
    Long saveSongMetadata(SongMetadata songMetadata);

    @Transactional
    void deleteSongMetadata(Set<Long> ids);
}
