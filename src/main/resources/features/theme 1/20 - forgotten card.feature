@regression
Feature: 20 - Existing user has forgotten 2FA card

  Scenario: Active 2FA card user can login via security questions
    # Login via security questions, check cookie is set
    Given I login and click forgotten card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "answer" in the {question1} field
    And I enter "answer" in the {question2} field
    And I press the "Continue" button
    Then The page title contains "Sign in successful"
    And The page contains "You have signed in without your security card"
    And I click the "Continue to home" link
    And The page title contains "Your home"
    And I check the "_hasLoggedInWithLostForgottenCardJourney" cookie is set

    # Subsequent logins that day do not ask for 2FA pin, because cookie is set
    And I click the "Sign out" link
    And I login without 2FA as {username}
    And The page title contains "Your security questions"
    And I enter "answer" in the {question1} field
    And I enter "answer" in the {question2} field
    And I press the "Continue" button
    And The page title contains "Sign in successful"
    And The page contains "You have signed in without your security card"
    And I click the "Continue to home" link
    And The page title contains "Your home"

    # Logins the following day after cookie expired (simulated by clearing the cookie) require PIN
    And I click the "Sign out" link
    And I delete the "_hasLoggedInWithLostForgottenCardJourney" cookie
    And I login without 2FA as {username}
    # FAnd The page title contains "Your security card PIN"
    And I generate 2FA PIN with previous drift {lastDrift} as {pin}
    And I enter {pin} in the "Security card PIN" field
    And I press the "Sign in" button
    And The page title contains "Your home"
