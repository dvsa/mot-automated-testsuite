@mothb2b
Feature: 01 - Check vehicle with expired MOT

  Scenario: A MOTH user searches for a vehicle with an expired MOT
    Given I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I invoke the b2b endpoint with registration {registration} and model {model}

    Then The response contains "vehicle-registration" equals {registration}
    And The vehicle make and model is {model}
