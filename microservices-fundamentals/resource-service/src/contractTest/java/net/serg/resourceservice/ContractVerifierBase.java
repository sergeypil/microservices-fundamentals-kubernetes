package net.serg.resourceservice;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.serg.resourceservice.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMessageVerifier
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"})
public class ContractVerifierBase {

    @MockBean
    private ResourceService resourceService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        setupStubsForResourceService();
    }

    private void setupStubsForResourceService() {
        given(resourceService.saveAudio(any(MultipartFile.class)))
            .willReturn(123L);
        
        given(resourceService.getAudioUrlById(any(Long.class)))
            .willReturn("\"http://localhost:8585/resources/1\"");
        
        doNothing().when(resourceService).deleteAudio(any(Set.class));
        
        given(resourceService.getAudioById(any(Long.class)))
            .willReturn(new InputStreamResource(new ByteArrayInputStream(new byte[1024])));
        doNothing().when(resourceService).moveAudioById(anyLong());
    }
}