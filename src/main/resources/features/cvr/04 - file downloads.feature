@cvr
Feature: o4 - CVR user downloads files

  Scenario: CVR user downloads All recalls since 1992 CSV file and checks that all the correct headings are displayed
    Given I browse to /
    And I click the button with id "csv-data-link"
#    And I click "All recalls since 1992 (CSV, 5mb)" and check the CSV contains:
#      | Launch Date                  |
#      | Recalls Number               |
#      | Make                         |
#      | Recalls Model Information    |
#      | Concern                      |
#      | Defect    					 |
#      | Remedy						 |
#      | Vehicle Numbers			 	 |
#      | Manufacturer Ref             |
#      | Model						 |
#      | VIN Start					 |
#      | VIN End						 |
#      | Build Start					 |
#      | Build End					 |

  Scenario: CVR user downloads CVR user downloads Recalls data guide DOC file
    Given I browse to /
    And I click the button with id "data-guide-link"