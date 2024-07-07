package net.serg.resourceservice.e2e;

import io.restassured.http.ContentType;
import net.serg.resourceservice.dto.IdResponse;
import net.serg.resourceservice.dto.SongMetadataDto;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

// Real environment is used for this test
// To run this test run docker-compose -> resource-service -> resource-processor -> song-service
public class ResourceServiceE2ETest {
    
    @Test
    void shouldSaveAudioSuccessfully() {
        // Given
        File file = new File(getClass().getClassLoader().getResource("sample.mp3").getFile());

        // When
        IdResponse idResponse = given()
            .baseUri("http://localhost:8080")
            .multiPart("audioFile", file)
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
            .pollDelay(5, TimeUnit.SECONDS)
            .atMost(20, TimeUnit.SECONDS)
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
