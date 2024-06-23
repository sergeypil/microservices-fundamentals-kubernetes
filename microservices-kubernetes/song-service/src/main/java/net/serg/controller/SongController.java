package net.serg.controller;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.dto.IdResponse;
import net.serg.dto.SongMetadataDto;
import net.serg.entity.SongMetadata;
import net.serg.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/songs")
@RequiredArgsConstructor
@Slf4j
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<IdResponse> saveSongMetadata(@RequestBody SongMetadataDto songMetadataDto) {
        log.info("Calling song-service to save metadata");
        var songMetadata = songMetadataDtoToSongMetadata(songMetadataDto);
        var id = songService.saveSongMetadata(songMetadata);
        var response = new IdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{resourceId}")
    public ResponseEntity<SongMetadataDto> getSongMetadataByResourceId(@PathVariable Long resourceId) {
        var songMetaData = songService.getSongMetadataByResourceId(resourceId);
        var songMetadataDto = songMetadataToSongMetadataDto(songMetaData);
        return ResponseEntity.ok(songMetadataDto);
    }

    @DeleteMapping
    public ResponseEntity<Set<Long>> deleteSongMetadata(@RequestParam(name = "id") String idsSeparatedByComma) {
        log.info("Calling song-service to delete metadata");
        var ids = Arrays.stream(idsSeparatedByComma.split(","))
            .map(String::trim)
            .map(Long::valueOf)
            .collect(Collectors.toSet());
        songService.deleteSongMetadata(ids);
        return ResponseEntity.ok(ids);
    }

    private SongMetadata songMetadataDtoToSongMetadata(SongMetadataDto songMetadataDto) {
        return SongMetadata.builder()
            .name(songMetadataDto.name())
            .artist(songMetadataDto.artist())
            .album(songMetadataDto.album())
            .length(songMetadataDto.length())
            .resourceId(songMetadataDto.resourceId())
            .year(songMetadataDto.year())
            .genre(songMetadataDto.genre())
            .build();
    }

    private SongMetadataDto songMetadataToSongMetadataDto(SongMetadata songMetaData) {
        return SongMetadataDto.builder()
            .name(songMetaData.getName())
            .artist(songMetaData.getArtist())
            .album(songMetaData.getAlbum())
            .length(songMetaData.getLength())
            .resourceId(songMetaData.getResourceId())
            .year(songMetaData.getYear())
            .genre(songMetaData.getGenre())
            .build();
    }
}
