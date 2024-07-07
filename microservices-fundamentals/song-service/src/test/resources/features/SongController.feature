Feature: Test SongController

  Scenario: Get song metadata by resource Id
    Given A song with resource ID "3"
    When I send GET request to "/songs/3"
    Then The response code should be 200