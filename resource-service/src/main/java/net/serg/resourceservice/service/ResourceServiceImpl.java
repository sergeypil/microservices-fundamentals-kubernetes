package net.serg.resourceservice.service;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serg.resourceservice.dto.SongMetadataDto;
import net.serg.resourceservice.entity.Audio;
import net.serg.resourceservice.exception.ResourceNotFoundException;
import net.serg.resourceservice.exception.ResourceServiceException;
import net.serg.resourceservice.repository.ResourceRepository;
import net.serg.resourceservice.service.client.SongServiceClient;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.LyricsHandler;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private final SongServiceClient songServiceClient;

    @Override
    @Transactional
    public Long saveAudio(MultipartFile mp3File) {
        try {
            var audio = resourceRepository.save(Audio.builder().audioData(mp3File.getBytes()).build());

            //detecting the file type
            var handler = new BodyContentHandler();
            var metadata = new Metadata();
            var inputStream = mp3File.getInputStream();
            var parseContext = new ParseContext();

            //Mp3 parser
            var mp3Parser = new Mp3Parser();
            mp3Parser.parse(inputStream, handler, metadata, parseContext);
            var lyrics = new LyricsHandler(inputStream, handler);

            while (lyrics.hasLyrics()) {
                log.debug(lyrics.toString());
            }

            log.debug("Contents of the document: {}", handler);
            log.debug("Metadata of the document:");
            String[] metadataNames = metadata.names();

            for (String name : metadataNames) {
                log.debug(name + ": " + metadata.get(name));
            }
            var songMetadataDto = SongMetadataDto.builder().name(metadata.get("dc:title"))
                .artist(metadata.get("xmpDM:artist"))
                .album(metadata.get("xmpDM:album"))
                .length(metadata.get("xmpDM:duration"))
                .resourceId(audio.getId())
                .year(metadata.get("xmpDM:releaseDate"))
                .build();
            songServiceClient.saveMetadata(songMetadataDto);
            return audio.getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceServiceException("Could not save audio. " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getAudio(long id) {
        var audio = resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Audio file with ID %d is not found", id)));
        return audio.getAudioData();
    }

    @Override
    @Transactional
    public void deleteAudio(Set<Long> ids) {
        resourceRepository.deleteAllById(ids);
        songServiceClient.deleteMetadataByResourceId(convertSetToCommaSeparatedString(ids));
    }

    private String convertSetToCommaSeparatedString(Set<Long> ids) {
        return ids.stream()
                  .map(String::valueOf)
                  .collect(Collectors.joining(","));
    }
}
