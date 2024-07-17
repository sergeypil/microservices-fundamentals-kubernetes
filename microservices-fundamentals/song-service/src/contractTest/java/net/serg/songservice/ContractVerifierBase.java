package net.serg.songservice;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.serg.entity.SongMetadata;
import net.serg.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMessageVerifier
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContractVerifierBase {
    
    @MockBean
    private SongService songService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        setupStubsForSongService();
    }

    private void setupStubsForSongService() {
        SongMetadata mockSongMetadata = new SongMetadata();
        mockSongMetadata.setId(1L);
        mockSongMetadata.setName("Test Song");
        mockSongMetadata.setArtist("Test Artist");
        mockSongMetadata.setAlbum("Test Album");
        mockSongMetadata.setLength("3:30");
        mockSongMetadata.setResourceId(1L);
        mockSongMetadata.setYear("2000");
        mockSongMetadata.setGenre("Test Genre");
        
        given(songService.getSongMetadataByResourceId(anyLong()))
            .willReturn(mockSongMetadata);
        given(songService.saveSongMetadata(any()))
            .willReturn(1L);
        willDoNothing().given(songService).deleteSongMetadata(any());   
    }
}