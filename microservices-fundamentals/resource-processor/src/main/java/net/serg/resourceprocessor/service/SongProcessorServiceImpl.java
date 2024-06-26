package net.serg.resourceprocessor.service;

import lombok.extern.slf4j.Slf4j;
import net.serg.resourceprocessor.dto.SongMetadataDto;
import net.serg.resourceprocessor.exception.ResourceProcessorException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.LyricsHandler;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class SongProcessorServiceImpl implements SongProcessorService {
    

    @Override
    public SongMetadataDto extractAudioMetadata(byte[] audioData, Long audioId) {
        var inputStream = new ByteArrayInputStream(audioData);
        return extractAudioMetadataFromStream(inputStream, audioId);
    }

    @Override
    public SongMetadataDto extractAudioMetadataFromStream(InputStream inputStream, Long audioId) {
        try {
            //detecting the file type
            var handler = new BodyContentHandler();
            var metadata = new Metadata();
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
            var songMetadataDto = SongMetadataDto
                .builder().name(metadata.get("dc:title"))
                .artist(metadata.get("xmpDM:artist"))
                .album(metadata.get("xmpDM:album"))
                .length(metadata.get("xmpDM:duration"))
                .year(metadata.get("xmpDM:releaseDate"))
                .resourceId(audioId)
                .build();

            return songMetadataDto;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceProcessorException("Could not process audio. " + e.getMessage());
        }
    }
}
