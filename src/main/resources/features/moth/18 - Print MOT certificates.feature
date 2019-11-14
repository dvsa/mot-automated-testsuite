@mothpp @mothint
Feature: 18b - Check whether MOT certificates can be downloaded after entering the correct V5C number on MOTH PP

  Scenario: A MOTH user searches for a vehicle with an pass MOT test and downloads the English MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_ENG_CERT_PASS" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled
    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | MOT test certificate                    |
      | Duplicate certificate issued by DVSA on |
      | Issued by DVSA                          |
      | VT20/2.0                                |
      | {registration}                          |
      | {testno}                                |
      | {model}                                 |
    Then I go to the next tab

  Scenario: A MOTH user searches for a vehicle with an pass MOT test and downloads the Welsh MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_WELSH_CERT_PASS" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled
    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | MOT test certificate                                    |
      | Tystysgrif prawf MOT                                    |

      | Duplicate certificate issued by DVSA on                 |
      | Dyblyg wedi ei gyhoeddi gan ASGC ar                     |

      | Issued by DVSA                                          |
      | Cyhoeddwyd gan ASGC                                     |

      | VT20/2.0                                                |
      | {registration}                                          |
      | {testno}                                                |
      | {model}                                                 |
    Then I go to the next tab

  Scenario: A MOTH user searches for a vehicle with an failed MOT test and downloads the English MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_ENG_CERT_FAIL" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled

    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | Refusal of MOT test certificate         |
      | Duplicate certificate issued by DVSA on |
      | Issued by DVSA                          |
      | VT30/2.0                                |
      | {registration}                          |
      | {testno}                                |
      | {model}                                 |
    Then I go to the next tab

  Scenario: A MOTH user searches for a vehicle with an failed MOT test and downloads the Welsh MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_WELSH_CERT_FAIL" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled

    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | MOT test certificate                            |
      | Gwrthodiad tystysgrif prawf MOT                 |

      | Duplicate certificate issued by DVSA on         |
      | Dyblyg wedi ei gyhoeddi gan ASGC ar             |

      | Issued by DVSA                                  |
      | Cyhoeddwyd gan ASGC                             |

      | VT30W/2.0                                       |
      | {registration}                                  |
      | {testno}                                        |
      | {model}                                         |
    Then I go to the next tab

  Scenario: A MOTH user searches for a vehicle with an PRS pass MOT test and downloads the English MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_ENG_CERT_PRS_PASS" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled

    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | MOT test certificate                    |
      | Duplicate certificate issued by DVSA on |
      | Issued by DVSA                          |
      | VT20/2.0                                |
      | {registration}                          |
      | {testno}                                |
      | {model}                                 |
    Then I go to the next tab

  Scenario: A MOTH user searches for a vehicle with an PRS fail MOT test and downloads the English MOT certificate
    Given I browse to /
    And I load "VEHICLE_REG_WITH_MOT_ENG_CERT_PRS_FAIL" as {registration}, {model}, {testno}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "{registration}"
    And The page contains "{model}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "{testno}"
    And I click the View test certificate link for test number "{testno}"
    Then The page contains "Enter latest V5C number"

    And I enter "{v5c}" in the v5c certificate field for test number "{testno}"
    And I press the Show test certificate button for test number "{testno}"
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled

    Given I click the button with id "cert-download-link"
    When I go to the next tab
    Then PDF is embedded in the page

    When I go to the next tab
    Then I click the certificate link and check the MOT certificate PDF contains:
      | MOT test certificate                    |
      | Fail                                    |
      | Duplicate certificate issued by DVSA on |
      | Issued by DVSA                          |
      | VT30/2.0                                |
      | {registration}                          |
      | {testno}                                |
      | {model}                                 |
    Then I go to the next tab

  Scenario: A MOTH user enters an incorrect V5C number and an appropriate error message is displayed
    Given I browse to /
    And I load "VEHICLE_REG_INVALID_CERT" as {registration}, {testnumber}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    When I click the accordion section with the id "mot-history-description"
    Then I click the View test certificate link for test number {testnumber}
    And I wait for certificate input field for tests number {testnumber} to be visible
    Then The page contains "Enter latest V5C number"

    Given I enter "00000000000" in the v5c certificate field for test number {testnumber}
    And I press the Show test certificate button for test number {testnumber}
    And I wait for certificate input field for tests number {testnumber} to be visible
    Then The page contains "Check that the V5C number you entered is correct"

    Given I enter " " in the v5c certificate field for test number {testnumber}
    And I press the Show test certificate button for test number {testnumber}
    And I wait for certificate input field for tests number {testnumber} to be visible
    Then The page contains "Enter the V5C number"

    Given I enter "123" in the v5c certificate field for test number {testnumber}
    And I press the Show test certificate button for test number {testnumber}
    And I wait for certificate input field for tests number {testnumber} to be visible
    Then The page contains "The V5C number must be 11 numbers"

    Given I enter "123123123AB" in the v5c certificate field for test number {testnumber}
    And I press the Show test certificate button for test number {testnumber}
    And I wait for certificate input field for tests number {testnumber} to be visible
    Then The page contains "The V5C number can only contain numbers"

    Given I enter {v5c} in the v5c certificate field for test number {testnumber}
    And I press the Show test certificate button for test number {testnumber}

    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled
    When I click the button with id "cert-download-link"
    And I go to the next tab
    Then The page contains "The certificate is currently unavailable"