package net.serg.component.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.serg.dto.SongMetadataDto;
import net.serg.entity.SongMetadata;
import net.serg.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SongControllerSteps {

    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private SongRepository songRepository;

    private ResponseEntity<SongMetadataDto> response;

    @Given("A song with resource ID {string}")
    public void a_song_with_resource_ID(String resourceId) {
        SongMetadata songMetadata = SongMetadata.builder()
            .resourceId(Long.valueOf(resourceId))
            .name("Test Song")
            .artist("Test Artist")
            .album("Test Album")
            .length("3:30")
            .year("2000")
            .genre("Test Genre")
            .build();
        songRepository.save(songMetadata);
    }

    @When("I send GET request to {string}")
    public void i_send_GET_request_to(String url) {
        response = testRestTemplate.getForEntity(url, SongMetadataDto.class);
    }

    @Then("The response code should be 200")
    public void the_response_code_should_be_200() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}