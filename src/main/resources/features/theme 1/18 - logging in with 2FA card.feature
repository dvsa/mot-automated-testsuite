@regression
Feature: 18 -  Existing user logging in with 2FA card

  Scenario Outline: User logs in successfully using 2FA card with drift <drift>
    When I login with 2FA and drift <drift> using "2FA_CARD_USER" as {username}, {lastDrift}, {q1}, {q2}
    Then The page title contains "Your home"

    Examples:
      | drift  |
      | -20    |
      | -10    |
      | +0     |
      | +10    |
      | +20    |


  @browserstack
  Scenario Outline: User is rejected when using 2FA card with drift <drift>
    When I login with 2FA and drift <drift> using "2FA_CARD_USER" as {username}, {lastDrift}, {q1}, {q2}
    Then The page title contains "Your security card PIN"

    Examples:
      | drift  |
      | -30    |
      | -25    |
      | -21    |
      | +21    |
      | +25    |
      | +30    |
