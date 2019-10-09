@regression @VEinspection
Feature: 12b - Vehicle Examiner

  Scenario: MOT test search by vin
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate |

  Scenario: MOT test search by VRM
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_LATEST" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate |

  Scenario: MOT test search by site by date range
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (by date range)" with {siteNumber} from 2 months ago
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate |

  Scenario: MOT test search by tester by date range
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "TESTER_WITH_10_DAY_HISTORY" as {tester}
    And I click the "MOT tests" link
    When I search for an mot by "User (by date range)" with {tester} from 2 months ago
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate |

  Scenario: MOT test search by site recent tests
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 92 and parking brake 85
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (recent tests)" with {siteNumber}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate |

  Scenario: Vehicle Examiner masking a vehicle
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {reg}
    And I click the button with id "mask-vehicle"
    And I press the "Mask this vehicle" button

    And I login with 2FA using "MOT_TESTER_CLASS_4_WITH_ONLY_ONE_SITE" as {username1}, {site}
    And I get the slot count from the homepage for site {site}

    When I start an MOT test for {reg}, {vin}, {mileage}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage} plus 5000
    And I enter decelerometer results of service brake 49 and parking brake 15
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the registration plate {reg} is shown within the registration number span text
    And I check the VIN {vin} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake test summary section has "Brake results overall" of "Fail"
    And I check the defect section has "Dangerous failures" with value "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the defect section has "Minors" with value "None recorded"
    And I check the defect section has "PRS" with value "None recorded"
    And I check the defect section has "Advisory text" with value "None recorded"
    And I check the dangerous failures section of the brake test summary of the test summary has "None recorded"
    And I check the major failures section of the brake test summary of the test summary has "Service brake efficiency below requirements"
    And I check the major failures section of the brake test summary of the test summary has "Parking brake efficiency below requirements"
    And I press the "Save test result" button

    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                        |
      | {reg}                                       |
      | {vin}                                       |
      | Parking brake efficiency below requirements |
    And I click the "Back to user home" link
    And I check a slot was not used for site {site}

    And I login with 2FA using "MOT_TESTER_CLASS_1" as {username2}, {site2}
    And I search for certificates with reg {reg}
    And The page contains "Duplicate or replacement certificate"
    And I set today formatted using "d MMMM YYYY" as {dateOfSiteVisit}
    And I check the "Date of test" field column does not have the value {dateOfSiteVisit}