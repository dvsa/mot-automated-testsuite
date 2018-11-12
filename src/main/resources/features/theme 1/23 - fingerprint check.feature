@regression
Feature: 20 - Verify fingerprint toggle is working when toggled ON

  Scenario: Verify that when a user has signed in with 2FA they don't need to re-enter the security pin on following logins. The test scenario checks this with two different users.
    #Login as user 1 for the first time
    Given I login with 2FA using "2FA_CARD_USER" as {username1}, {site}, {q1}, {q2}
    And The page title contains "Your home - MOT testing service"
    And The page contains "Your home"
    And I click the "Sign out" link
    Then The page title contains "Sign in - MOT testing service"

    #Login as user 1 for the second time
    And I login without 2FA as {username1}
    And The page title contains "Your home - MOT testing service"
    And The page contains "Your home"
    And I click the "Sign out" link
    Then The page title contains "Sign in - MOT testing service"

    #Login as user 1 for the first time
    When I login with 2FA using "2FA_CARD_USER" as {username2}, {site}, {q1}, {q2}
    And The page title contains "Your home - MOT testing service"
    And The page contains "Your home"
    And I click the "Sign out" link
    Then The page title contains "Sign in - MOT testing service"

    #Login as user 1 for the first time
    And I login without 2FA as {username2}
    And The page title contains "Your home - MOT testing service"
    And The page contains "Your home"
    And I click the "Sign out" link
    Then The page title contains "Sign in - MOT testing service"


