package net.serg.integration;

import net.serg.dto.IdResponse;
import net.serg.dto.SongMetadataDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_CLASS, scripts="classpath:init.sql")
@Disabled
public class SongControllerIT {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getSongMetadataByResourceId() {
        // when
        ResponseEntity<SongMetadataDto> response = testRestTemplate.getForEntity("/songs/3", SongMetadataDto.class);
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void saveSongMetadata() {
        //given
        SongMetadataDto songMetadataDto = SongMetadataDto.builder()
            .name("Test Song3")
            .artist("Test Artist3")
            .album("Test Album3")
            .length("3:30")
            .resourceId(2L)
            .year("2000")
            .genre("Test Genre")
            .build();
        
        //when
        ResponseEntity<IdResponse> response = testRestTemplate.postForEntity("/songs", songMetadataDto, IdResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void deleteSongMetadata() {
        // when
        ResponseEntity<Set<Long>> response = testRestTemplate.exchange(
            "/songs?id=4,5",
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}