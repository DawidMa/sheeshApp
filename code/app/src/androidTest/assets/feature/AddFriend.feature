Feature: Add Friend

  Scenario: add friend to list
    Given I am on the Add Friend Tab
    And I insert a name into text field
    And I click the FAB
    Then Tab closes and I see MainActivity overview


  Scenario: friend not added beacause already exists
    Given I am on the Add Friend Tab
    And I insert a name into text field
    And I click the FAB
    Then text field is empty
    And I stay in the AddFriendActivity
