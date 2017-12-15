Feature: Add Friend

  Scenario: add friend to list
    Given App is launched
    And I am signed up
    And I am logged in
    Then I start AddFriendActivity
    And I insert a name into text field
    And I click the FAB
    Then I see friend in list at MainActivity


  Scenario: friend cant be added twice
    Given App is launched
    And I am signed up
    And I am logged in
    Then I start AddFriendActivity
    And I insert a name into text field
    And I click the FAB
    Then I see friend in list at MainActivity
    Then I start AddFriendActivity
    And I insert a name into text field
    And I click the FAB
    Then text field is deleted
    And I stay in the AddFriendActivity



