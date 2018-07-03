@mothpp
@mothint
Feature: 02a - Check vehicle with current MOT - reminder email tab

  Scenario: A MOTH user searches for a vehicle with a current MOT and clicks Get a reminder email
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "MOT valid until"
    And The page contains "Get an MOT reminder"
    When I click the "Get an MOT reminder" link
    And I go to the next tab

    Then The page title contains "Get MOT reminders - GOV.UK"
    And I close extra tabs

  Scenario: A MOTH user searches for a vehicle with a current MOT and clicks Contact DVSA
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "MOT valid until"
    And The page contains "If you think the MOT expiry date or any of the vehicle details are wrong,"
    And I click the "contact" link
    And I go to the next tab

    Then The page title contains "Contact DVSA"
    And I close extra tabs

    When I press the "Back" button
    And The page title contains "Check MOT history"
