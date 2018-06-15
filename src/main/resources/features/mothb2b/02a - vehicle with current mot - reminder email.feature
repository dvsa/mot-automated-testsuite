@mothb2b
Feature: 02a - Check vehicle with current MOT - reminder email tab


  Scenario: A MOTH user searches for a vehicle with a current MOT and clicks Get a reminder email
    Given I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {makemodel}
    And I invoke the b2b endpoint with registration {registration} and model {makemodel}

    Then The response contains "vehicle-registration" equals {registration}
    And The vehicle make and model is {makemodel}

