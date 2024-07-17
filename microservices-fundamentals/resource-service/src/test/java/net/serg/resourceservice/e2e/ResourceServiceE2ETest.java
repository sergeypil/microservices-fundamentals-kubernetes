package net.serg.resourceservice.e2e;

import io.restassured.http.ContentType;
import net.serg.resourceservice.dto.IdResponse;
import net.serg.resourceservice.dto.SongMetadataDto;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

// Real environment is used for this test
// To run this test run docker-compose -> resource-service -> resource-processor -> song-service
@Disabled
public class ResourceServiceE2ETest {
    
    @Test
    void shouldSaveAudioSuccessfully() {
        // When
        IdResponse idResponse = given()
            .baseUri("http://localhost:8080")
            .multiPart("audioFile", "sample.mp3", "test content".getBytes())
            .when()
            .post("/resources")
            .then()
            .log()
            .all()
            .statusCode(OK.value())
            .contentType(APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .as(IdResponse.class);
        
        // Then
        Awaitility
            .await()
            .pollInterval(10, TimeUnit.SECONDS)
            .pollDelay(15, TimeUnit.SECONDS)
            .atMost(45, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                var songMetadataDto =
                    given()
                        .baseUri("http://localhost:8081")
                        .contentType(ContentType.JSON)
                        .when()
                        .get("songs/" + idResponse.id())
                        .then()
                        .extract()
                        .body()
                        .as(SongMetadataDto.class);
                System.out.println(songMetadataDto);
                Assertions
                    .assertThat(songMetadataDto).isNotNull();
            });
    
    }
    
}
