@regression
Feature: 05 - Tester does...

  @smoke
  Scenario: Tester enters a class 4 MOT test pass, with no defects and slot is consumed
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I get the slot count from the homepage for site {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link
    And I check a slot was successfully used for site {site}


  Scenario: Tester enters a class 4 MOT test fail, with brake test failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 50 and parking brake 15
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  @smoke
  Scenario: Tester enters a class 4 MOT test fail, with failure defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Steering", "Steering operation", "Steering system excessively tight") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Exhaust, fuel and emissions", "Exhaust system", "Exhaust has a major leak of exhaust gases") with comment "Test defect 2"
    And I browse for a "Failure" defect of ("Lamps, reflectors and electrical equipment", "Battery", "Battery leaking electrolyte") with comment "Test defect 3"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "Steering system excessively tight"
    And I check the fails section of the test summary has "Exhaust has a major leak of exhaust gases"
    And I check the fails section of the test summary has "Battery leaking electrolyte"
    And I check the fails section of the test summary has "Test defect 1"
    And I check the fails section of the test summary has "Test defect 2"
    And I check the fails section of the test summary has "Test defect 3"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 4 MOT test fail, with brake performance not tested
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter odometer not present
    And I search for a "Failure" defect of "Brake performance not tested" with comment "test defect"
    And I check the "Add brake test" link is hidden
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Not tested"
    And I check the fails section of the test summary has "Brake performance not tested"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 4 MOT test pass, with advisory defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test advisory 1"
    And I browse for a "Advisory" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test advisory 2"
    And I search for a "Advisory" defect of "Body or chassis has excessive corrosion, seriously affecting its strength within 30cm of the body mountings" with comment "Test advisory 3"
    And I search for a "Advisory" defect of "Electrical wiring damaged, likely to cause a short" with comment "Test advisory 4"
    And I add a manual advisory of "Test manual advisory"
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the advisory section of the test summary has "Obligatory mirror obscured but not seriously affecting the rear view"
    And I check the advisory section of the test summary has "Adjustable towbar bracket slightly worn"
    And I check the advisory section of the test summary has "Body has slight corrosion"
    And I check the advisory section of the test summary has "Electrical wiring damaged, but not likely to cause a short"
    And I check the advisory section of the test summary has "Test advisory 1"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I check the advisory section of the test summary has "Test advisory 3"
    And I check the advisory section of the test summary has "Test advisory 4"
    And I check the advisory section of the test summary has "Test manual advisory"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


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

    And The page title contains "Cancel MOT test"
    And I click the "Aborted by VE" radio button
    And I press the "Confirm and cancel test" button
    Then The page title contains "MOT test aborted"


  @smoke
  Scenario: Tester enters a class 4 MOT retest pass, all failures repaired, no need to repeat brake test
    Given I load "VEHICLE_CLASS_4_AFTER_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Body, structure and general items", "Engine mountings", "Engine mounting missing") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Brakes", "ABS", "Anti-lock braking system component missing") with comment "Test defect 2"
    And I browse for a "Failure" defect of ("Road wheels", "Attachment", "Wheel insecure") with comment "Test defect 3"
    And I enter decelerometer results of service brake 61 and parking brake 16
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
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
    And I mark the defect "Wheel insecure" as repaired
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


  Scenario: Tester enters a class 4 MOT retest pass, with brake test re-entry
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Body, structure and general items", "Engine mountings", "Engine mounting missing") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Brakes", "ABS", "Anti-lock braking system component missing") with comment "Test defect 2"
    And I search for a "Failure" defect of "Brake performance not tested" with comment "Test defect 3"
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Not tested"
    And I check the fails section of the test summary has "engine mounting missing"
    And I check the fails section of the test summary has "Anti-lock braking system component missing"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"

    When I click the "Back to user home" link
    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I mark the defect "engine mounting missing" as repaired
    And I mark the defect "Anti-lock braking system component missing" as repaired
    And I mark the defect "Brake performance not tested" as repaired

    And I check the "Review test" button is disabled
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"


  Scenario: Tester enters a class 1 MOT test pass
    Given I load "VEHICLE_CLASS_1" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_1" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter group a plate results for weights of 100,120,80 as 120,120,120,120
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 2 MOT test fail, with colour change
    Given I load "VEHICLE_CLASS_2_RED" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_2" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site} with colour changed to "Blue"
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Motorcycle driving controls", "Throttle", "Throttle operating incorrectly") with comment "Test defect 1"
    And I enter decelerometer results of efficiency 15
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the vehicle summary section of the test summary has "Colour" of "Blue"
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Throttle operating incorrectly"
    And I check the fails section of the test summary has "Test defect 1"
    And I check the fails section of the test summary has "Decelerometer brake test neither brake control achieves an efficiency of 25%"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 5 MOT test pass, with engine capacity change
    Given I load "VEHICLE_CLASS_5_DIESEL" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site} with engine changed to "Petrol" with capacity 2700
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Non-component advisories", "Nail in tyre") with comment "Test advisory 1"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the vehicle summary section of the test summary has "Fuel type" of "Petrol"
    And I check the brake results section of the test summary is "Pass"
    And I check the advisory section of the test summary has "Nail in tyre"
    And I check the advisory section of the test summary has "Test advisory 1"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 7 MOT test fail
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in kilometres of 30000
    And I search for a "Failure" defect of "Steering system excessively tight" with comment "Test failure 1"
    And I edit the "Failure" defect of "Steering system excessively tight" with comment "Edited failure 1" and is dangerous
    And I remove the "Failure" defect of "Steering system excessively tight"
    And I enter class 7 roller results for vehicle weight of 1000 as service brake 350,50,50,350 and parking brake 350,50
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary does not have "Steering system excessively tight"
    And I check the fails section of the test summary does not have "Test failure 1"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester abandons an MOT test, for a class 3 vehicle
    Given I load "VEHICLE_CLASS_3" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_3" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Non-component advisories", "Spare tyre defective") with comment "Test advisory 1"
    And I click the "Cancel test" link

    And The page title contains "Cancel MOT test"
    And I click the "Inspection may be dangerous or cause damage" radio button
    And I enter "Test reason" in the "How might this be dangerous or cause damage?" field
    And I press the "Confirm and cancel test" button
    Then The page title contains "MOT test abandoned"


  Scenario: Tester enters a class 4 MOT test pass, for a DVLA vehicle
    Given I load "DVLA_VEHICLE" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 4 MOT test pass, for a new vehicle
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I search for a vehicle with {site}
    And I click the "create a new vehicle record" link

    And The page title contains "Make a new vehicle record"
    And I click the "Start now" link
    And I enter reg "ANDY1" and vin "111111"
    And I select make "AUDI" and model "A1"
    And I select fuel type "Petrol" and cylinder capacity 1200
    And I select Vehicle Class 4
    And I select primary colour "Blue" and secondary colour "No other colour"
    And I select country of registration "GB, UK, ENG, CYM, SCO (UK) - Great Britain"
    And I enter the date of first use as today minus 4 years

    And The page title contains "Confirm new record and start test"
    And I press the "Confirm and start test" button
    And The page title contains "MOT test started"
    And I click the "Continue to home" link

    And The page title contains "Your home"
    When I click the "Enter test results" link
    And I enter an odometer reading in kilometres of 12345
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of "ANDY1"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of "111111"
    And I check the vehicle summary section of the test summary has "Colour" of "Blue"
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
