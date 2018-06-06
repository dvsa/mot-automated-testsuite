@failures
Feature: Failures

  @brake
  Scenario Outline: Tester enters a class "<Class>" MOT test fail with brake failure
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 30 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |1|AUBR9480|VORAN GARAGE LIMITED|VEHICLE_CLASS_1|
      |2|CAFA4558|STAMFORD GROUP LTD|VEHICLE_CLASS_2|

  @failure
  Scenario Outline: Tester enters a class "<Class>" MOT test fail with defects
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Motorcycle sidecar", "Wheel bearing", "Sidecar wheel bearings are excessively tight") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Motorcycle driving controls", "Throttle", "Throttle operating incorrectly") with comment "Test defect 2"
    And I browse for a "Failure" defect of ("Motorcycle fuel and exhaust", "Exhaust system", "Exhaust mounting missing") with comment "Test defect 3"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |1|CHAI8234|DILENA GROUP|VEHICLE_CLASS_1|
      |2|CHEE4588|ASENG GROUP|VEHICLE_CLASS_2|

  @advisory
  Scenario Outline: Tester enters a class "<Class>" MOT test pass with advisories
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Motorcycle body and structure", "Condition of structure", "Motorcycle structure excessively damaged") with comment "Test advisory 1"
    And I browse for a "Advisory" defect of ("Motorcycle drive system", "Drive chain", "Drive chain excessively tight") with comment "Test advisory 2"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |1|CHEG6973|CELSIUS MOTORS|VEHICLE_CLASS_1|
      |2|DALB8075|DILENA GROUP|VEHICLE_CLASS_2|

  @prs
  Scenario Outline: Tester enters a class "<Class>" MOT test pass with prs
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "PRS" defect of ("Motorcycle body and structure", "Condition of structure", "Motorcycle structure excessively damaged") with comment "Test prs 1"
    And I browse for a "PRS" defect of ("Motorcycle drive system", "Drive chain", "Drive chain excessively tight") with comment "Test prs 2"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |1|DERE3460|DEZZUTTI GARAGE|VEHICLE_CLASS_1|
      |2|DIER3512|WINCANTON MOTOR ENGINEERS|VEHICLE_CLASS_2|


  @mix
  Scenario Outline: Tester enters a class "<Class>" MOT test failure with advisory, defect, prs and brake failure
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Motorcycle body and structure", "Condition of structure", "Motorcycle structure excessively damaged") with comment "Test advisory 1"
    And I browse for a "Failure" defect of ("Motorcycle sidecar", "Wheel bearing", "Sidecar wheel bearings are excessively tight") with comment "Test defect 1"
    And I browse for a "PRS" defect of ("Motorcycle body and structure", "Condition of structure", "Motorcycle structure excessively damaged") with comment "Test prs 1"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 30 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |1|DRON4002|CELSIUS MOTORS|VEHICLE_CLASS_1|
      |2|GALA6049|ATKISON MOTORS LTD|VEHICLE_CLASS_2|

  @brake
  Scenario Outline: Tester enters a class "<Class>" MOT test fail with brake failure
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 30 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |3|EPPL5360|SANDOWN MOTORS LIMITED|VEHICLE_CLASS_3|
      |4|ERMA4550|FEAR GARAGE LTD|VEHICLE_CLASS_4|
      |5|BROG7653|LETHAM MOTOR WORKS LTD|VEHICLE_CLASS_5|
      |7|BUHL7969|TIFFNER AUTOSALES|VEHICLE_CLASS_7|

  @failure
  Scenario Outline: Tester enters a class "<Class>" MOT test fail with defects
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

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
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |3|DOUN3610|KILLELEA GARAGE LTD|VEHICLE_CLASS_3|
      |4|AVEN3456|OSWALDTWISTLE MOTOR WORKS|VEHICLE_CLASS_4|
      |5|DOLE5394|VTS061530|VEHICLE_CLASS_5|
      |7|DYKE8547|RAUHUFF AUTOSALES LIMITED|VEHICLE_CLASS_7|

  @advisory
  Scenario Outline: Tester enters a class "<Class>" MOT test pass with advisories
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test advisory 1"
    And I browse for a "Advisory" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test advisory 2"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |3|HARL8471|LOCHGILPHEAD SERVICE STATION LTD|VEHICLE_CLASS_3|
      |4|BAXI4477|HESSLE & PARTNER LIMITED|VEHICLE_CLASS_4|
      |5|EAGL4076|FEAR MOTOR ENGINEERS LTD|VEHICLE_CLASS_5|
      |7|COWE4406|WICKHAM AUTOSALES LIMITED|VEHICLE_CLASS_7|

  @prs
  Scenario Outline: Tester enters a class "<Class>" MOT test pass with prs
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "PRS" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test prs 1"
    And I browse for a "PRS" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test prs 2"
    And I search for a "PRS" defect of "Body or chassis has excessive corrosion, seriously affecting its strength within 30cm of the body mountings" with comment "Test prs 3"
    And I search for a "PRS" defect of "Electrical wiring damaged, likely to cause a short" with comment "Test prs 4"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |3|FREG0867|VTS004062|VEHICLE_CLASS_3|
      |4|CRUS4632|BURY GARAGE|VEHICLE_CLASS_4|
      |5|GOER4228|RAUHUFF AUTOSALES LIMITED|VEHICLE_CLASS_5|
      |7|FLIE2626|WICKHAM AUTOSALES LIMITED|VEHICLE_CLASS_7|


  @mix
  Scenario Outline: Tester enters a class "<Class>" MOT test failure with advisory, defect, prs and brake failure
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as "<Username>", "<Site>"

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Drivers view of the road", "Mirrors", "Obligatory mirror seriously obscured affecting the rear view") with comment "Test advisory 1"
    And I browse for a "Failure" defect of ("Steering", "Steering operation", "Steering system excessively tight") with comment "Test defect 1"
    And I browse for a "PRS" defect of ("Towbars", "Adjustable towbar", "Adjustable towbar bracket excessively worn") with comment "Test prs 1"
    And I add a manual advisory of "Test manual advisory"
    And I enter decelerometer results of service brake 30 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    Examples:
      |Class|Username|Site|Vehicle|
      |3|JAST0183|LOCHGILPHEAD SERVICE STATION LTD|VEHICLE_CLASS_3|
      |4|GRAU2449|RIPLEY GROUP|VEHICLE_CLASS_4|
      |5|KLUS3211|VTS076410|VEHICLE_CLASS_5|
      |7|KUCE6924|VTS079774|VEHICLE_CLASS_7|
