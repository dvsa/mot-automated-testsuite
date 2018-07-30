@tradeapi
Feature: 02 - Searching by registration number

  Scenario: A Trade API user searches MOT details by registration number
    Given I load "VEHICLE_REG_MOT_EXPIRED_B2B" as {registration}, {make}, {model}, {first_used_date}, {expiry_date}, {number}
    And I invoke the Trade API search by registration number with {registration}

    Then The first response contains "registration" equals {registration}
    And The first response contains "make" equals {make}
    And The first response contains "model" equals {model}
    #And The first response contains "firstUsedDate" equals {first_used_date}
    #And The response contains "motTests.expiryDate" equals {expiry_date}
    And The response contains "motTests.motTestNumber[0][0]" equals {number}
