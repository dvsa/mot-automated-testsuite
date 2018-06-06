@RFR
Feature: 01 - RFR Check

  @RFR1-2
  Scenario Outline: Tester checks all "<Type>" RFRs for class "<Class>"
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}

    And I load "<Component Sheet>" workbook sheet from "<File>". I map columns by ID's as:
      | Alias       | ID Column Num | Data Column Num |
      | Parent ID   | 0             | 1               |
      | L0          | 0             | 2               |
      | L1          | 0             | 3               |
      | L2          | 0             | 4               |
      | L3          | 0             | 5               |
      | L4          | 0             | 6               |
      | L5          | 0             | 7               |
      | Description | 0             | 8               |

    And I load "<RFR Sheet>" workbook sheet from "<File>". I map columns by ID's as:
      | Alias               | ID Column Num | Data Column Num |
      | Component ID        | 0             | 6               |
      | Component           | 0             | 4               |
      | Class 1             | 0             | 16              |
      | Class 2             | 0             | 17              |
      | Manual Ref          | 0             | 11              |
      | Failure Text        | 0             | 9               |
      | Advisory Text       | 0             | 12              |
      | Deficiency Category | 0             | 15              |

    And I login with 2FA using "<User>" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I click the "Add a defect" link

    And I check every "<Type>" RFR for class "<Class>" using loaded RFR sheet "<RFR Sheet>" and Component Tree sheet "<Component Sheet>" in "<File>" file

    Examples:
      | User               | Vehicle         | Class | Type     | File               | Component Sheet | RFR Sheet |
      | MOT_TESTER_CLASS_1 | VEHICLE_CLASS_1 | 1     | positive | RFRDocName1-2.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_2 | VEHICLE_CLASS_2 | 2     | positive | RFRDocName1-2.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_1 | VEHICLE_CLASS_1 | 1     | negative | RFRDocName1-2.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_2 | VEHICLE_CLASS_2 | 2     | negative | RFRDocName1-2.xlsx | Component Tree  | RFR       |

  @RFR3-7
  Scenario Outline: Tester checks all "<Type>" RFRs for class "<Class>"
    Given I load "<Vehicle>" as {registration1}, {vin1}, {mileage1}

    And I load "<Component Sheet>" workbook sheet from "<File>". I map columns by ID's as:
      | Alias       | ID Column Num | Data Column Num |
      | Parent ID   | 3             | 4               |
      | L0          | 3             | 5               |
      | L1          | 3             | 6               |
      | L2          | 3             | 7               |
      | L3          | 3             | 8               |
      | L4          | 3             | 9               |
      | L5          | 3             | 10              |
      | Description | 3             | 11              |

    And I load "<RFR Sheet>" workbook sheet from "<File>". I map columns by ID's as:
      | Alias               | ID Column Num | Data Column Num |
      | Component ID        | 1             | 4               |
      | Sub Component       | 1             | 26              |
      | Condition           | 1             | 27              |
      | Advisory Condition  | 1             | 28              |
      | Manual Ref          | 1             | 12              |
      | Class 3             | 1             | 17              |
      | Class 4             | 1             | 18              |
      | Class 5             | 1             | 19              |
      | Class 7             | 1             | 20              |
      | Failure Text        | 1             | 9               |
      | Advisory Text       | 1             | 14              |
      | Deficiency Category | 1             | 11              |

    And I login with 2FA using "<User>" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I click the "Add a defect" link

    And I check every "<Type>" RFR for class "<Class>" using loaded RFR sheet "<RFR Sheet>" and Component Tree sheet "<Component Sheet>" in "<File>" file

    Examples:
      | User               | Vehicle         | Class | Type     | File               | Component Sheet | RFR Sheet |
      | MOT_TESTER_CLASS_3 | VEHICLE_CLASS_3 | 3     | positive | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_4 | VEHICLE_CLASS_4 | 4     | positive | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_5 | VEHICLE_CLASS_5 | 5     | positive | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_7 | VEHICLE_CLASS_7 | 7     | positive | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_3 | VEHICLE_CLASS_3 | 3     | negative | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_4 | VEHICLE_CLASS_4 | 4     | negative | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_5 | VEHICLE_CLASS_5 | 5     | negative | RFRDocName3-7.xlsx | Component Tree  | RFR       |
      | MOT_TESTER_CLASS_7 | VEHICLE_CLASS_7 | 7     | negative | RFRDocName3-7.xlsx | Component Tree  | RFR       |