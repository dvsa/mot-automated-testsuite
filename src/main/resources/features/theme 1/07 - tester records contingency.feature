@regression
Feature: 07 - Tester records Contingency Test

   Scenario: A tester record a pass contingency test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
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
     And The page title contains "MOT test complete"
     And I click "Print documents" and check the PDF contains:
       | VT20            |
       | {registration1} |
       | {vin1}          |
       | {site}          |
     And I click the "Back to user home" link


  Scenario: A tester records a fail contingency test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I enter "18" in the "Day" field
    And I enter "09" in the "Month" field
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
    And I click "Print documents" and check the PDF contains:
      | VT30            |
      | {registration1} |
      | {vin1}          |
      | {site}          |
    And I click the "Back to user home" link


  Scenario: A tester records a fail contingency test then a contingency retest pass 10 working days later
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I enter "01" in the "Day" field
    And I enter "02" in the "Month" field
    And I enter "2017" in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
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
    And I browse for a "Pre EU Failure" defect of ("Exhaust, fuel and emissions", "Exhaust system", "Exhaust has a major leak of exhaust gases") with comment "Test defect 1"
    And I search for a "Pre EU Advisory" defect of "Body or chassis has excessive corrosion, seriously affecting its strength within 30cm of the body mountings" with comment "Test advisory 2"
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "Exhaust has a major leak of exhaust gases"
    And I check the fails section of the test summary has "Test defect 1"
    And I check the advisory section of the test summary has "Body has slight corrosion"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                      |
      | {registration1}                           |
      | {vin1}                                    |
      | {site}                                    |
      | Exhaust has a major leak of exhaust gases |
      | Test defect 1                             |
      | Body has slight corrosion                 |
      | Test advisory 2                           |
    And I click the "Back to user home" link

    Then I start a contingency MOT test at site {site}
    And I enter "15" in the "Day" field
    And I enter "02" in the "Month" field
    And I enter "2017" in the "Year" field
    And I enter "11" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
    And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link

    And I press the "Confirm and start retest" button

    And I enter an odometer reading in miles of {mileage1} plus 5005
    And I mark the defect "Exhaust has a major leak of exhaust gases" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "None Recorded"
    And I check the fails section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "Body has slight corrosion"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                      |
      | {registration1}           |
      | {vin1}                    |
      | {site}                    |
      | Body has slight corrosion |
      | Test advisory 2           |

  Scenario: A tester records a fail contingency test four days ago and performs retest pass now
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load immediately "CONTINGENCY_DATE" as {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
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
    And I browse for a "Major" defect of ("Identification of the vehicle", "Vehicle identification number", "Incomplete") with comment "Test defect 1"
    And I search for a "Advisory" defect of "Standard fitment seat belt missing" with comment "Test advisory 2"
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the major failures section of the test summary has "Vehicle Identification Number incomplete"
    And I check the major failures section of the test summary has "Test defect 1"
    And I check the advisory section of the test summary has "Standard fitment seat belt missing"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                      |
      | {registration1}                           |
      | {vin1}                                    |
      | {site}                                    |
      | Vehicle identification number incomplete  |
      | Test defect 1                             |
      | Standard fitment seat belt missing        |
      | Test advisory 2                           |
    And I click the "Back to user home" link

    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5005
    And I mark the defect "Vehicle Identification Number incomplete" as repaired
    And I mark the defect " Standard fitment seat belt missing" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "None Recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                      |
      | {registration1}           |
      | {vin1}                    |
      | {site}                    |

  Scenario: A tester record contingency test and then cancels the contingency test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I set today as {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "09" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I select "am" in the field with id "contingency_time-ampm"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load uniquely "VEHICLE_CLASS_4_BEFORE_2010" as {registration}, {vin}, {mileage}
    And I enter {registration} in the "Registration mark" field
    And I enter {vin} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button

    And I enter an odometer reading in miles of {mileage} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 10
    And I click the "Cancel test" link

    And I click the "Aborted by VE" radio button
    And I press the "Cancel test" button
    And I click "Print documents" and check the PDF contains:
      | VT30            |
      | {registration}  |
      | {vin}           |
      | {site}          |
    And I click the "Finish" link

  @smoke
  Scenario: A tester prints CT certificates
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_ONLY_ONE_SITE" as {username}, {site}, {reg}
    And I click "CT20 Pass Certificate" and check the PDF contains:
      | Contingency MOT Test Certificate     |
      | CT20                                 |
      | {site}                               |
    And I click "CT30 Refusal Notice" and check the PDF contains:
      | Contingency Refusal of an MOT Test Certificate   |
      | CT30                                             |
      | {site}                                           |

