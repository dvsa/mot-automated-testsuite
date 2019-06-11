@cvrpp @cvrint
<<<<<<< HEAD
Feature: 13 - CVR user downloads All recalls since 1992 CSV file
=======
Feature: 14 - CVR user downloads All recalls since 1992 CSV file
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

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