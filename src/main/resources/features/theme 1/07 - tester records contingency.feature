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


  # This test can not be automated because the data in the anonymised dataset would not contain in the last 7 days even after database reset
  # Scenario: A tester records a retest as a contingency test

  # Given I load "LAST_FAILED_MOT_CLASS_4" as {username1}, {id}, {registration1}, {vin1}, {mileage}, {site}
  # And I login with 2FA using "LAST_FAILED_MOT_CLASS_4" as {username1}, {id}, {registration1}, {vin1}, {mileage}, {site}
  # And I click the "Record contingency test" link
  # And I set the site {site}
  # And I set today as {day}, {month}, {year}
  # And I enter {day} in the "Day" field
  # And I enter {month} in the "Month" field
  # And I enter {year} in the "Year" field
  # And I set time as {hour}, {minute}, {amPm}
  # And I enter {hour} in the field with id "contingency_time-hour"
  # And I enter {minute} in the field with id "contingency_time-minutes"
  # And I select "am" in the field with id "contingency_time-ampm"
  # And I click the "Other" radio button
  # And I enter "Automated Testing" in the "If other, please enter a reason" field
  # And I load "CONTINGENCY_CODE" as {code}, {startDate}, {endDate}
  # And I enter {code} in the "Contingency code" field
  # And I press the "Confirm contingency test" button

  # And I enter {registration1} in the "Registration mark" field
  # And I enter {vin1} in the "VIN" field
  # And I press the "Search" button
  # And I click the "Select vehicle" link
  # And I press the "Confirm and start retest" button
  # And I enter an odometer reading in miles of {mileage1} plus 5000
  # And I click the "Edit brake test" link
  # And I select "Decelerometer" in the field with id "serviceBrake1TestType"
  # And I select "Decelerometer" in the field with id "parkingBrakeTestType"
  # And I press the "Next" button
  # And I enter "70" in the field with id "serviceBrake1Efficiency"
  # And I enter "30" in the field with id "parkingBrakeEfficiency"
  # And I press the "Submit" button
  # And I click the "Done" link
  # And I press the "Review test" button
  # Then The page title contains "MOT re-test summary"
  # And I check the test information section of the test summary is "Pass"
  # And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
  # And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
  # And I check the brake results section of the test summary is "Pass"
  # And I press the "Save test result" button
  # And The page title contains "MOT re-test complete"


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
    And I click the "Finish" link


