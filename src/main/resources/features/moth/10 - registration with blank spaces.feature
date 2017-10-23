@moth2
Feature: 10 - Check vehicle reg with blank spaces

  Scenario: A MOTH user searches for a vehicle reg with blank spaces
    Given I browse to /
    And I load "VEHICLE_REG_WITH_SPACE" as {registration}, {regspace}, {model}
    And I enter {regspace} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"
