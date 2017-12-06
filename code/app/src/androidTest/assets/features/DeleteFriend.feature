Feature: Delete Friend

  Scenario: delete friend from list
    Given App is launched
    And I am signed up
    And I am logged in
    Then I am on the friend list tab
    And I see a friend in the list
    Then I click on the delete button
    And Friend disappears from the list