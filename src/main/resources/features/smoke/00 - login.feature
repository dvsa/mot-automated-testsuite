Feature: 00 - Login
  @smoke
  Scenario: Redirect to login page if go to an internal screen when not logged in
    When I browse to /your-profile
    Then The page title contains "Sign in"
    And I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    Then The page title contains "Your home"