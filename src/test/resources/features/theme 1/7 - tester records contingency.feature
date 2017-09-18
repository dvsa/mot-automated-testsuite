@regression
Feature: 07 - Tester records Contingency Test

   Scenario: Record contingency test (Pass)
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I click the "Record contingency test" link
    And I enter "01" in the "Day" field
    And I enter "01" in the "Month" field
    And I enter "2017" in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the Test Information summary section of the test summary has "Issue date" of "1 January 2017"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button


  Scenario: Record contingency test (Fail)
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I click the "Record contingency test" link
    And I enter "01" in the "Day" field
    And I enter "03" in the "Month" field
    And I enter "2017" in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  # Scenario: Record contingency test (Retest)
    # Pre-req: Date in past/Contingency code specified correctly(select number from emergency_log eg. CC14862C)
    # Conduct a RETEST test and check that the VT30 has the date the test was conducted (specified by tester - note only a retest if it is within the 10 day retest period since the FAIL)
    # Validation
    #   - Date/time not in the past
    #   - No contingency code specified
    #   - Generate CT blank certs (CT20, CT30, CT32)

  Scenario: Record contingency test (Cancel)
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I click the "Record contingency test" link
    And I set today as {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "09" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration}, {vin}, {mileage}
    And I enter {registration} in the "Registration mark" field
    And I enter {vin} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    

    And I enter an odometer reading in miles of {mileage} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 10
    And I click the "Cancel test" link

    And I click the "Aborted by VE" radio button
    And I press the "Confirm and cancel test" button
    And I click the "Finish" link




