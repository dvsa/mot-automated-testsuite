@regression
Feature: 05b - Tester does...

  Scenario: Tester enters a class 4 MOT test pass, with advisory and PRS defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "PRS" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test prs 1"
    And I search for a "PRS" defect of "Electrical wiring damaged, likely to cause a short" with comment "Test prs 2"
    And I browse for a "Advisory" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test advisory 1"
    And I search for a "Advisory" defect of "Body or chassis has excessive corrosion, seriously affecting its strength within 30cm of the body mountings" with comment "Test advisory 2"
    And I enter decelerometer service brake result of 60 and gradient parking brake result of "Pass"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the prs section of the test summary has "Adjustable towbar bracket excessively worn"
    And I check the prs section of the test summary has "Electrical wiring damaged, likely to cause a short"
    And I check the prs section of the test summary has "Test prs 1"
    And I check the prs section of the test summary has "Test prs 2"
    And I check the advisory section of the test summary has "Obligatory mirror obscured but not seriously affecting the rear view"
    And I check the advisory section of the test summary has "Body has slight corrosion"
    And I check the advisory section of the test summary has "Test advisory 1"
    And I check the advisory section of the test summary has "Test advisory 2"

    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 4 MOT test fail, add edit and remove advisory, PRS and failure defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000

    And I browse for a "Advisory" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test advisory 1"
    And I search for a "Advisory" defect of "Body or chassis has excessive corrosion, seriously affecting its strength within 30cm of the body mountings" with comment "Test advisory 2"
    And I edit the "Advisory" defect of "Body has slight corrosion" with comment "Edited advisory 2" and not dangerous
    And I remove the "Advisory" defect of "Obligatory mirror obscured but not seriously affecting the rear view"

    And I browse for a "PRS" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test prs 1"
    And I search for a "PRS" defect of "Electrical wiring damaged, likely to cause a short" with comment "Test prs 2"
    And I edit the "PRS" defect of "Adjustable towbar bracket excessively worn" with comment "Edited prs 1" and not dangerous
    And I remove the "PRS" defect of "Electrical wiring damaged, likely to cause a short"

    And I browse for a "Failure" defect of ("Steering", "Steering operation", "Steering system excessively tight") with comment "Test failure 1"
    And I search for a "Failure" defect of "Battery leaking electrolyte" with comment "Test failure 2"
    And I remove the "Failure" defect of "Steering system excessively tight"
    And I edit the "Failure" defect of "Battery leaking electrolyte" with comment "Edited failure 2" and is dangerous

    And I enter decelerometer service brake result of 61 and gradient parking brake result of "Pass"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"

    And I check the fails section of the test summary has "Battery leaking electrolyte"
    And I check the fails section of the test summary has "Edited failure 2"
    And I check the fails section of the test summary has "Dangerous"
    And I check the fails section of the test summary does not have "Steering system excessively tight"
    And I check the fails section of the test summary does not have "Test failure 1"

    And I check the prs section of the test summary has "Adjustable towbar bracket excessively worn"
    And I check the prs section of the test summary has "Edited prs 1"
    And I check the prs section of the test summary does not have "Electrical wiring damaged, likely to cause a short"
    And I check the prs section of the test summary does not have "Test prs 2"

    And I check the advisory section of the test summary has "Body has slight corrosion"
    And I check the advisory section of the test summary has "Edited advisory 2"
    And I check the advisory section of the test summary does not have "Obligatory mirror obscured but not seriously affecting the rear view"
    And I check the advisory section of the test summary does not have "Test advisory 1"

    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester aborts an MOT test
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I click the "Cancel test" link

    And The page title contains "Cancel test"
    And I click the "Aborted by VE" radio button
    And I press the "Cancel test" button
    Then The page title contains "MOT test aborted"


  @smoke
  Scenario: Tester enters a class 4 MOT retest pass, all failures repaired, no need to repeat brake test
    Given I load "VEHICLE_CLASS_4_AFTER_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Dangerous" defect of ("Body, structure and attachments", "Engine mountings", "Engine mounting/bracket missing") with comment "Test defect 1"
    And I browse for a "Major" defect of ("Brakes", "Anti-lock braking system", "Component missing") with comment "Test defect 2"
    And I browse for a "Major" defect of ("Axles, wheels, tyres and suspension", "Wheel bearings", "Wheel bearing has excessive play") with comment "Test defect 3"
    And I enter decelerometer results of service brake 61 and parking brake 16
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "Engine mounting/bracket missing"
    And I check the fails section of the test summary has "Dangerous"
    And I check the fails section of the test summary has "component missing"
    And I check the fails section of the test summary has "Wheel bearing has excessive play"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"

    When I click the "Back to user home" link
    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I mark the defect "Engine mounting/bracket missing" as repaired
    And I mark the defect " component missing" as repaired
    And I mark the defect "Wheel bearing has excessive play" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "None Recorded"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"


  Scenario: Tester enters a class 4 MOT retest fail, with brake test re-entry
    Given I load "VEHICLE_CLASS_4_AFTER_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Body, structure and general items", "Engine mountings", "Engine mounting missing") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Brakes", "ABS", "Anti-lock braking system component missing") with comment "Test defect 2"
    And I browse for a "Failure" defect of ("Road wheels", "Attachment", "Wheel insecure") with comment "Test defect 3"
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 100,50,100,50 and parking brake 60,50
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "engine mounting missing"
    And I check the fails section of the test summary has "Anti-lock braking system component missing"
    And I check the fails section of the test summary has "Wheel insecure"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"

    When I click the "Back to user home" link
    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I mark the defect "engine mounting missing" as repaired
    And I mark the defect "Anti-lock braking system component missing" as repaired

    And I check the "Review test" button is disabled
    And I edit class 4 roller results for vehicle weight of 1000 as service brake 160,160,160,160 and parking brake 80,80
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "Wheel insecure"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
