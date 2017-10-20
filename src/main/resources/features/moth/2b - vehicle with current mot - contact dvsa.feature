@moth2
Feature: 02b - Check vehicle with current MOT - contact DVSA


  Scenario: A MOTH user searches for a vehicle with a current MOT and clicks Contact DVSA
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "MOT valid until"
    And The page contains "Incorrect details?"

    When I click the text "Incorrect details?"
    Then The page contains "if you think the MOT expiry date or any of the vehicle details"

    And I click the "Contact DVSA" link
    And I go to the next tab

    Then The page title contains "Contact DVSA"
    And I close extra tabs

