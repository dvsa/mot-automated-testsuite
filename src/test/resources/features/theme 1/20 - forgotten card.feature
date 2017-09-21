@regression
Feature: 20 - Existing user has forgotten 2FA card

  Scenario: Active 2FA card user can login via security questions
    Given I login and click forgotten card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "answer" in the {question1} field
    And I enter "answer" in the {question2} field
    And I press the "Continue" button
    And The page title contains "Sign in successful"
    And The page contains "You have signed in without your security card"
    And I click the "Continue to home" link

    # then... Subsequent logins that day do not ask for 2FA pin (goes straight from password -> sec. questions)
    # 2FA active user can login directly via security questions within a 24 hour period
    # Log in via lost/forgotten journey above
    # Log out
    # Log in with username/password
    # Expected result: User directed straight to enter security question page

    # then... User prompted to logon via PIN next day & able to login with existing card [expire cookie to simulate this]
    # ..continue and login with PIN..
