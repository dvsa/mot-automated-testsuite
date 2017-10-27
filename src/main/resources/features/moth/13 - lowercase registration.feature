@moth2 @moth-int
Feature: 13 - Check vehicle reg with lowercase letters

  Scenario: A MOTH user searches for a vehicle reg with lowercase letters
    Given I browse to /
    And I load "VEHICLE_REG_LOWERCASE" as {registration}, {reglcase}, {model}
    And I enter {reglcase} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"