@regression
Feature: 22 - order card and check grace period exists

  Scenario: User logs in with temporary login and checks grace period is created, orders a card and checks that grace period is still active.

    #Login with temporary login link and answer the security questions.
    Given I login and click "temporary" card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    And I click the "Continue" link
    And I enter "answer" in the field with id "answer"
    And I press the "Continue" button
    And The page title contains "Change security questions - Reset your account security - MOT testing service"
    And I press the "Continue" button
    And The page title contains "MOT testing service"
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I select "What is the name of the street where you grew up?" in the field with id "question3"
    And I enter "MOT Testing Centre" in the field with id "answer3"
    And I press the "Continue" button
    And The page title contains "Review security question changes - MOT testing service"
    And I press the "Save Changes" button
    And The page title contains "Your security questions have been changed - MOT testing service"
    And I click the "Continue to home" link
    And The page title contains "Your home"

    #Update security answers to meet new security policy requirements, continue to user homepage and then signout.
    Then The page title contains "Change security questions - Reset your account security - MOT testing service"
    And I press the "Continue" button
    When I enter "answer" in the field with id "answer"
    And I press the "Continue" button
    And The page title contains "Review security question changes - MOT testing service"
    And I press the "Save Changes" button
    And I click the "Continue to home" link
    And I click the "Sign out" link

    #Login again with temporary sign in and verify that grace period is active.
    And I login without 2FA as {username}
    And I click the "Temporary sign in" link
    And The page title contains "Sign in without your security card"
    And I click the "Continue" link
    And I enter the correct answer to the security question
    And I press the "Continue" button
    Then The page contains "You have signed in without your security card"
    Then The page contains "You have 6 days remaining before you will be unable to sign in without a security card."

    #Order a new security card from the grace period page and then sign out.
    And I click the "order a new security card" link
    And I click the "Continue" link
    And The page title contains "Choose a delivery address"
    And I click the "Home" radio button
    And I press the "Continue" button
    And The page title contains "Review delivery address - MOT testing service"
    And I press the "Order security card" button
    Then The page contains "Your security card has been ordered"
    Then The page contains "Your card will be delivered by second class post and should arrive within 5 working days. You do not have to sign for the delivery."
    And I click the "Sign out" link

    #Login again and verify user is shown an initial page regarding their ordered security card.
    And I login without 2FA as {username}
    And The page title contains "Sign in - MOT testing service"
    And The page contains "You have ordered a new card. Until you receive and activate the card, sign in with your security questions."

    #User continues to login with their security answers.
    And I click the "Continue" link
    And The page title contains "Your security questions - MOT testing service"
    And I enter "answer" in the field with id "answer"
    And I press the "Continue" button

    #Verify that the page shows text regarding signing in without security card sign in and grace period.
    Then The page contains "You have signed in without your security card"
    Then The page contains "You have 6 days remaining before you will be unable to sign in without a security card."

