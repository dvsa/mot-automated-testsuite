Feature: Login

  Scenario: Redirect to login page
    When I browse to /
    Then The page title contains Sign in

  Scenario: Login with 2FA as a tester
    When I login as username CALL0020 and password Password007
    Then The page title contains Your home
    #todo: autologout in after scenario hook

  Scenario: Browse to internal screen when not logged in
    When I browse to /your-profile
    Then The page title contains Sign in