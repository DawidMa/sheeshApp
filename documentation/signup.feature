Feature: SignUp

  Scenario: Invalid Email
    Given I am at the SignUp Screen
    Then  I enter an invalid email into textfield with id "tfMail"
    And   I click the SignUp Button with id "btSignup"
    Then  I see a message "Your Mail is invalid"

  Scenario: Wrong Password
    Given I am at the SignUp Screen
    Then  I enter a password into textfield with id "tfPassword"
    And   I enter not the same password into textfield with id "tfPassword2"
    And   I click the SignUp Button with id "btSignup"
    Then  I see a message "Your Password is wrong"

  Scenario: Successful SignUp
    Given I am at the SignUp Screen
    Then  I enter a valid email into textfield with id "tfMail"
    And   I enter a password into textfield with id "tfPassword"
    And   I enter the same password into textfield with id "tfPassword2"
    And   I click the SignUp Button with id "btSignup"
    Then  I see a message "SignUp successful! Check Your Mails"
