Feature: Add Friend

  Scenario: add friend to friend
    Given I am on the Add Friend Tab
    And I insert a name into text field
    And I click the "GAME_HISTORY" menu
    When I click on an Game History entry
    Then I see the Game History screen
