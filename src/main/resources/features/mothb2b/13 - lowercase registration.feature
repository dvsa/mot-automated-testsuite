@mothb2b
Feature: 13 - Check vehicle reg with lowercase letters

  Scenario: A B2B MOTH user searches for a vehicle reg with lowercase letters
    Given I load "VEHICLE_REG_LOWERCASE_B2B" as {reg_lower}, {registration}, {make}, {model}, {expiry_date}, {number}
    And I invoke the MOTH B2B API with registration {reg_lower} and make {make}

    Then The first response contains "vehicle-registration" equals {registration}
    And The first response contains "vehicle-make" equals {make}
    And The first response contains "vehicle-model" equals {model}
    And The first response contains "test-expiry-date" equals {expiry_date}
    And The first response contains "test-number" equals {number}