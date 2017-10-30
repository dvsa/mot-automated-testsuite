@regression
Feature: 06a - duplicate and replacement certificates

  Scenario Outline: <user> user issues duplicate certificate
    Given I login without 2FA using "<dataScript>" as {<user>}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I check the PDF certificate contains correct data

    Examples:
      |user        |dataScript       |
      |AO1         |AO1_USER         |
      |DVLA Manager|DVLA_MANAGER_USER|