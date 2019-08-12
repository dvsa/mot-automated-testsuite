@regression
Feature: 01 - new user registration

  Scenario: Create new user with unique details
    Given I generate a unique email address as {email}
    And I browse to /login

    When I click the text "Don't have an account"
    And I click the "Create a new account" link
    And I click the "Continue" link

    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Continue" button

    And I enter "John" in the "First name" field
    And I enter "Bob" in the "Middle name (optional)" field
    And I enter "Varcher" in the "Last name" field
    And I enter "20" in the "Day" field
    And I enter "07" in the "Month" field
    And I enter "1969" in the "Year" field
    And I press the "Continue" button

    And I enter "1 Some Street" in the "Home address" field
    And I enter "Some Area" in the "Address line 2" field
    And I enter "Somecity" in the "Town or city" field
    And I enter "SW1 1AA" in the "Postcode" field
    And I enter "01234 567890" in the "Enter your home, mobile or work number" field
    And I press the "Continue" button

    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I select "In what city or town did you spend your honeymoon?" in the field with id "question3"
    And I enter "MOT Testing Centre" in the field with id "answer3"
    And I press the "Continue" button

    And I enter "MyPassword1234" in the "Create a password" field
    And I enter "MyPassword1234" in the "Re-type your password" field
    And I press the "Continue" button

    And I press the "Create your account" button
    And The page title contains "Your account has been created - MOT testing service"

    And I browse to /login
    And I load immediately "LATEST_TEST_USER" as {username}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button

    Then The page title contains "Your home"