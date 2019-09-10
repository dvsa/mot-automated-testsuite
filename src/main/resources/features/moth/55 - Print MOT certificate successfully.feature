@mothprint @mothint
Feature: 55 - Check whether MOT certificates can be downloaded after entering the V5C number

  Scenario: A MOTH user searches for a vehicle with an MOT pass test and downloads the English MOT certificate
    Given I browse to /
    And I enter "CGSENOK" in the registration field
    When I press the "Continue" button

    And I click the accordion section with the id "mot-history-description"
    And I click the first "View test certificate" text
    Then The page contains "Enter latest V5C number"

    Given I enter "12312312312" in the field with id "v5c-print-cert-input"
    And I press the first "Show test certificate" button
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
      | CGSENOK                                 |
      | 99 9999 9900                            |

  @karl1
  Scenario: A MOTH user searches for a vehicle with an MOT pass test and downloads the English MOT certificate
    # VRM: CGSENOK - vehicle with valid EN PASS documents (1 page and 2 page)
# VRM: CGSCYOK - vehicle with valid CY PASS documents
# VRM: CGSENFP - vehicle with valid EN FAIL document, and a PRS document combo (PASS+FAIL)
# VRM: CGSCYFP - vehicle with valid CY FAIL document, and a PRS document combo (PASS+FAIL)
    Given I browse to /
    And I enter "CGSCYOK" in the registration field
    When I press the "Continue" button

    And I click the accordion section with the id "mot-history-description"
    And I click the first "View test certificate" text
    Then The page contains "Enter latest V5C number"

    Given I enter "12312312312" in the field with id "v5c-print-cert-input"
    And I press the first "Show test certificate" button
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

      | Body not fully secure [6.2.2 (a) (i)]                   |
      | Air/vacuum reservoir slightly corroded [1.1.9 (a) (ii)] |
      | Corff: nid yw'n gwbl ddiogel [6.2.2 (a) (i)]            |
      | Cronfa aer/gwactod: wedi rhydu ychydig [1.1.9 (a) (ii)] |

      | Issued by DVSA                                          |
      | Cyhoeddwyd gan ASGC                                     |

      | VT20/2.0                                                |
      | CGSCYOK                                                 |
      | 99 9999 9902                                            |

  Scenario: A MOTH user searches for a vehicle with an MOT test and downloads the MOT certificate
    Given I browse to /
    And I enter "CGSCYFP" in the registration field
    When I press the "Continue" button

    And I click the accordion section with the id "mot-history-description"
    And I click the first "View test certificate" text
    Then The page contains "Enter latest V5C number"

    Given I enter "12312312312" in the field with id "v5c-print-cert-input"
    And I press the first "Show test certificate" button
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

      | Fuel hose damaged [6.1.3 (c) (ii)]              |
      | Peipen danwydd hyblyg: difrodi [6.1.3 (c) (ii)] |

      | Issued by DVSA                                  |
      | Cyhoeddwyd gan ASGC                             |

      | VT30W/2.0                                       |
      | CGSCYFP                                         |

      # MOT test number
      | 99 9999 9921                                    |

