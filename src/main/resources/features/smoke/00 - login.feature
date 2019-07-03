Feature: 00 - Login
  @smoke
  # Note: if run on CI Green, the user is actually redirected to CI Blue
  Scenario: Redirect to login page from home page
    When I browse to /
    Then The page title contains "Sign in"
  @smoke
  Scenario: Login with 2FA as a tester
    When I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then The page title contains "Your home"
  @smoke
  Scenario: Redirect to login page if go to an internal screen when not logged in
    When I browse to /your-profile
    Then The page title contains "Sign in"