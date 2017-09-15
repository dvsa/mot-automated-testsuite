@regression
Feature: 18 -  2FA: Logging in with 2FA card

  Scenario Outline: User logs in successfully using 2FA card with drift <drift>
    When I login with 2FA and drift <drift> using "2FA_CARD_USER" as {username}, {lastDrift}
    Then The page title contains "Your home"

    Examples:
      | drift |
      | -2    |
      | -1    |
      | +0    |
      | +1    |
      | +2    |


  Scenario Outline: User is rejected when using 2FA card with drift <drift>
    When I login with 2FA and drift <drift> using "2FA_CARD_USER" as {username}, {lastDrift}
    Then The page title contains "Your security card PIN"

    Examples:
      | drift |
      | -5    |
      | -4    |
      | -3    |
      | +3    |
      | +4    |
      | +5    |
