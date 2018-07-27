@mothb2b
Feature: 05 - Check vehicle with MOT that expires today

  Scenario: A B2B MOTH user searches for a vehicle with an MOT that expires today
    Given I load "VEHICLE_REG_EXPIRES_TODAY_B2B" as {registration}, {make}, {model}, {first_used_date}, {expiry_date}, {number}
    And I invoke the MOTH B2B API with registration {registration} and make {make}

    Then The first response contains "vehicle-registration" equals {registration}
    And The first response contains "vehicle-make" equals {make}
    And The first response contains "vehicle-model" equals {model}
    And The first response contains "vehicle-first-used-date" equals {first_used_date}
    And The first response contains "test-expiry-date" equals {expiry_date}
    And The first response contains "test-number" equals {number}