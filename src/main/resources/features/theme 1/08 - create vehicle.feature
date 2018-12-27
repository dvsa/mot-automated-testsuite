@regression
Feature: 08 - Create Vehicle

  Scenario Outline: Tester Creates a new vehicle with class <class>
    Given I login with 2FA using "<User>" as {username1}, {site}

    When I search for a vehicle with {site}
    And I click the "create a new vehicle record" link
    And The page title contains "Make a new vehicle record"

    And I click the "Start now" link
    And I enter reg "<reg>" and vin "<vin>"
    And I select make "<make>" and model "<model>"

    And I select fuel type "<fuelType>" and cylinder capacity <cylinderCapacity>
    And I select Vehicle Class <class>
    And I select primary colour "<primaryColour>" and secondary colour "<secondaryColour>"

    And I select country of registration "<Country of reg>"
    And I enter the date of first use as today minus <years> years
    And The page title contains "Confirm new record"

    And I check the registration "<reg>" and vin "<vin>" is correct
    And I check the make "<make>" and model "<model>" is correct
    And I check the fuel type "<fuelType>" and cylinder capacity <cylinderCapacity> is correct

    And I check the vehicle class <class> is correct
    And I check the primary colour "<primaryColour>" and secondary colour "<secondaryColour>" is correct
    And I check the date of first use from <years> years ago is correct

    And I press the "Confirm and start test" button
    Then The page title contains "MOT test started"
    And I cancel the mot test after creating the vehicle
    And I click "Print certificate" and check the PDF contains:
      | VT30            |
      | <reg>           |
      | <vin>           |

    Examples:
      | User               | reg         | vin                | make    | model | fuelType | cylinderCapacity | class | primaryColour | secondaryColour | Country of reg                             | years |
      | MOT_TESTER_CLASS_5 | N3WR5G      | N3WV1N54TOIEOD1234 | FORD    | GT    | Steam    | 0                | 5     | Blue          | No other colour | GB, NI (UK) - Northern Ireland             | 3     |
      | MOT_TESTER_CLASS_7 | N3WR7G      | N3WV1N7TGL045AFW41 | Other   | Other | Petrol   | 3400             | 7     | Silver        | No other colour | GB, UK, ENG, CYM, SCO (UK) - Great Britain | 5     |
      | MOT_TESTER_CLASS_3 | NOTPROVIDED | N3WV1N3FLLE321ASDC | FORD    | Other | Electric | 0                | 3     | Pink          | Yellow          | F (FR) - France                            | 3     |
      | MOT_TESTER_CLASS_2 | N3WR2G      | N3WV1N2FLOP42ADWE9 | DUCATI  | M900  | Other    | 1200             | 2     | Blue          | No other colour | D (DE) - Germany                           | 2     |
      | MOT_TESTER_CLASS_1 | N3WR1G      | N3WV1N1L0RT42LKO3D | APRILIA | 125   | Gas      | 1400             | 1     | Gold          | Silver          | GBM (IM) - Isle of Man                     | 7     |


  @regressiondata
  Scenario Outline: Tester Creates a new vehicle with class <class> and completes a test pass
    Given I login with 2FA using "<User>" as {username1}, {site}

    When I search for a vehicle with {site}
    And I click the "create a new vehicle record" link
    And The page title contains "Make a new vehicle record"

    And I click the "Start now" link
    And I enter reg "<reg>" and vin "<vin>"
    And I select make "<make>" and model "<model>"

    And I select fuel type "<fuelType>" and cylinder capacity <cylinderCapacity>
    And I select Vehicle Class <class>
    And I select primary colour "<primaryColour>" and secondary colour "<secondaryColour>"

    And I select country of registration "<Country of reg>"
    And I enter the date of first use as today minus <years> years
    And The page title contains "Confirm new record"

    And I check the registration "<reg>" and vin "<vin>" is correct
    And I check the make "<make>" and model "<model>" is correct
    And I check the fuel type "<fuelType>" and cylinder capacity <cylinderCapacity> is correct

    And I check the vehicle class <class> is correct
    And I check the primary colour "<primaryColour>" and secondary colour "<secondaryColour>" is correct
    And I check the date of first use from <years> years ago is correct

    And I press the "Confirm and start test" button
    Then The page title contains "MOT test started"
    And I click the "Continue to home" link
    And The page title contains "Your home"
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of 5000
    And I enter decelerometer results of service brake 71 and parking brake 81
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of "<reg>"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of "<vin>"
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I record the MOT test number
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20  |
      | <reg> |
      | <vin> |

    Examples:
      | User               | reg     | vin                | make | model | fuelType | cylinderCapacity | class | primaryColour | secondaryColour | Country of reg                             | years |
      | MOT_TESTER_CLASS_4 | N3WD4AT | N3WV1N74356045AGW4 | AUDI | A5    | Petrol   | 3600             | 4     | Red           | Black           | GB, UK, ENG, CYM, SCO (UK) - Great Britain | 4     |