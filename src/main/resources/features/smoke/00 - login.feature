Feature: 00 - Login
  @smoke @karl
  Scenario: Redirect to login page if go to an internal screen when not logged in, then log in
    When I browse to /your-profile
    Then The page title contains "Sign in"
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then The page title contains "Your home"