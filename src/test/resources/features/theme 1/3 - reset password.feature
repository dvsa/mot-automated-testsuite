@regression
Feature: 03 - reset password

  Scenario: Tester reset their password
    Given I generate a unique email address as {email}
    And I browse to /login

    When I click the text "Don't have an account"
    And I click the "Create a new account" link
    And I click the "Continue" link

    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Continue" button

    And I enter "Harry" in the "First name" field
    And I enter "Billy" in the "Middle name (optional)" field
    And I enter "Tester" in the "Last name" field
    And I enter "01" in the "Day" field
    And I enter "01" in the "Month" field
    And I enter "1988" in the "Year" field
    And I press the "Continue" button

    And I enter "1 Fake Street" in the "Home address" field
    And I enter "Fake Area" in the "Address line 2" field
    And I enter "Fakecity" in the "Town or city" field
    And I enter "NG1 1AA" in the "Postcode" field
    And I enter "01234 567890" in the "Enter your home, mobile or work number" field
    And I press the "Continue" button

    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "Answer" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "Answer" in the field with id "answer2"
    And I press the "Continue" button

    And I enter "MyPassword123" in the "Create a password" field
    And I enter "MyPassword123" in the "Re-type your password" field
    And I press the "Continue" button

    And I press the "Create your account" button

    And I browse to /login
    And I load immediately "LATEST_TEST_USER" as {username}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword123" in the "Password" field
    And I press the "Sign in" button

    Then The page title contains "Your home"
    And I click the "Your profile" link
    Then I click the "Change your password" link

    And I enter "MyPassword123" in the "Current password" field
    And I enter "MyPassword321" in the "New password" field
    And I enter "MyPassword321" in the "Re-type your new password" field
    And I press the "Change password" button

    And I click the "Sign out" link
    Then I browse to /login
    And I load immediately "LATEST_TEST_USER" as {username}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword321" in the "Password" field
    And I press the "Sign in" button
