Feature: Tutorial

  Scenario: Read the tutorial while logged in
    Given   I am a logged in user
    Then    I see the start screen
    And     I have to search the tutorial icon
    And     Tap it
    Then    I should see the tutorial screen

  Scenario: Read the tutorial as guest
    Given   I am not logged in
    Then    I see the start screen
    And     I have to search the tutorial icon
    And     Tap it
    Then    I should see the tutorial screen
    