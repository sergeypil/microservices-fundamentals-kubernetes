package net.serg.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.serg.entity.SongMetadata;
import net.serg.exception.ResourceNotFoundException;
import net.serg.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;


    @Override
    @Transactional(readOnly = true)
    public SongMetadata getSongMetadataByResourceId(long resourceId) {
        var songMetadata = songRepository.findAllByResourceId(resourceId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Song Metadata for resource with ID %d is not found", resourceId)));
        return songMetadata;
    }

    @Override
    @Transactional
    public Long saveSongMetadata(SongMetadata songMetadata) {
        songRepository.save(songMetadata);
        log.debug("saving metadata for resource ID {}", songMetadata.getResourceId());
        return songMetadata.getId();
    }

    @Override
    @Transactional
    public void deleteSongMetadata(Set<Long> ids) {
        songRepository.deleteByResourceIdIn(ids);
    }
}
