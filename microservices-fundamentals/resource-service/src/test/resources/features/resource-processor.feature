Feature: Resource processing feature

  @Disabled
  Scenario: Mp3 file processed successfully
    Given file with name file_example_MP3_2MG.mp3
    When data is sent resource service
    Then song will be created
