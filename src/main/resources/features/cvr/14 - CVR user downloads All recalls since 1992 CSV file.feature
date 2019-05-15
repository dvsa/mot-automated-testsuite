@cvrpp @cvrint
Feature: 14 - CVR user downloads All recalls since 1992 CSV file

  Scenario: CVR user downloads All recalls since 1992 CSV file and checks that all the correct headings are displayed
    Given I browse to /
    And I click "All recalls since 1992 (CSV, 5mb)" and check the CSV contains:
      | Launch Date                  |
      | Recalls Number               |
      | Make                         |
      | Recalls Model Information    |
      | Concern                      |
      | Defect    					 |
      | Remedy						 |
      | Vehicle Numbers			 	 |
      | Manufacturer Ref             |
      | Model						 |
      | VIN Start					 |
      | VIN End						 |
      | Build Start					 |
      | Build End					 |