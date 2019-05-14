@mothaccessibility
Feature: MOT History accessibility reports

  Scenario: A MOTH user searches for a vehicle with a current MOT
    Given I browse to /
    And I create an accessibility report "moth-home"
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "MOT valid until"
    And I create an accessibility report "moth-vehicle-history"

  Scenario: A MOTH user searches for a vehicle with an expired MOT
    Given I browse to /
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT has expired"
    And I create an accessibility report "moth-vehicle-expired"

  Scenario: A MOTH user searches for an MOT test with dangerous failures
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page contains "What are failures?"
    And I create an accessibility report "moth-vehicle-dangerous-failure"

  Scenario: A MOTH user searches for a vehicle with an invalid reg
    Given I browse to /
    And I enter "AB12ABC" in the registration field
    When I press the "Continue" button
    Then The page contains "There was a problem"
    And The page contains "Check that the registration you entered is correct"
    And The page title contains "What is the vehicle's registration number"
    And I create an accessibility report "moth-invalid-reg"