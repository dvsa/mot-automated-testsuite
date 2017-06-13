Feature: Login

  Scenario: Redirect to login page from home page
    When I browse to /
    Then The page title contains "Sign in"

  Scenario: Login with 2FA as a tester
    #When I login with 2FA as a valid tester
    When I login with 2FA as username DERE3460
    Then The page title contains "Your home"

  Scenario: Redirect to login page if go to an internal screen when not logged in
    When I browse to /your-profile
    Then The page title contains "Sign in"