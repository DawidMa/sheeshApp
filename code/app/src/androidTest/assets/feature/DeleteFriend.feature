Feature: Delete Friend

  Scenario: delete friend from list
    Given I am on the friend list tab
    And I click on the delete button
    Then Friend is deleted
    And friend disappears from the list