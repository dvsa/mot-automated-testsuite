@regression
  Feature: 25a - site review for white sites

    Scenario: VE user creates a first site review

      #Search for a site which has no previous site reviews
      Given I load "WHITE_SITE" as {siteNumber}, {siteName}, {organisationName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}

      #Start a site review
      When I click the "Add site review" link
      And I enter the date of site visit as 1 days ago
      And I enter "John Doe" in the field with id "aeName"
      And I enter "AE" in the field with id "aeRole"
      And I press the "Save and continue" button

      #Record a satisfactory compliance outcome
      And I click the button with id "change_CO_button"
      And I click the "Satisfactory" radio button
      And I enter "Compliance outcome comment" in the field with id "satisfactory-textarea"
      And I press the "Save and return" button

      #Record a satisfactory premises outcome
      And I click the button with id "change_PR_button"
      And I click the "Satisfactory" radio button
      And I enter "Premises outcome comment" in the field with id "satisfactory-textarea"
      And I press the "Save and return" button

      #Record a satisfactory management and quality outcome
      And I click the button with id "change_MQ_button"
      And I click the "Satisfactory" radio button
      And I enter "Management and quality outcome comment" in the field with id "satisfactory-textarea"
      And I press the "Save and return" button

      #Record a satisfactory people outcome
      And I click the button with id "change_PE_button"
      And I click the "Satisfactory" radio button
      And I enter "People outcome comment" in the field with id "satisfactory-textarea"
      And I press the "Save and return" button

      #Record an activity performed with MOT test number and satisfactory outcome
      And I click the button with id "RecordActivityButton"
      And I click the "Yes" radio button
      And I press the "Save and continue" button
      And I enter "123456789012" in the "MOT test number" field
      And I click the "Satisfactory" radio button
      And I enter "Activity outcome comment" in the field with id "satisfactory-textarea"
      And I press the "Save and return" button

      #Submit and check the check details page
      And I click the button with id "continueButton"
      Then I check the "Site review outcome" field row has value "Satisfactory"
      And The page does not contain "Improve"
      And The page does not contain "Unsatisfactory"
      And I check the "Compliance" field row has value "Satisfactory"
      And I check the "Premises" field row has value "Satisfactory"
      And I check the "Management and quality" field row has value "Satisfactory"
      And I check the "People" field row has value "Satisfactory"
      And I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Satisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check the "Compliance" field row has value "Comment: Compliance outcome comment"
      And I check the "Premises" field row has value "Comment: Premises outcome comment"
      And I check the "Management and quality" field row has value "Comment: Management and quality outcome comment"
      And I check the "People" field row has value "Comment: People outcome comment"
      And I check the "Activity" field row has value "Comment: Activity outcome comment"
      #Below check commented out because it fails to see the "N/A" value
      #And I check the "AE representative's User ID" field row has value "NA"
      And I check there is a "Change" link

      #Submit site review and check it's submitted successfully
      And I click the "Save and confirm" button
      Then The page contains "Site review has been saved"
      And I click the "Back" link
      And I check the "Current" field row has value "Satisfactory"

      #Check the summary
      And I click the "View summary" link
      Then I check the "Compliance" field row has value "Satisfactory"
      And I check the "Premises" field row has value "Satisfactory"
      And I check the "Management and quality" field row has value "Satisfactory"
      And I check the "People" field row has value "Satisfactory"
      And I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Satisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check the "Compliance" field row has value "Comment: Compliance outcome comment"
      And I check the "Premises" field row has value "Comment: Premises outcome comment"
      And I check the "Management and quality" field row has value "Comment: Management and quality outcome comment"
      And I check the "People" field row has value "Comment: People outcome comment"
      And I check the "Activity" field row has value "Comment: Activity outcome comment"
      And I check the "AE representative" field row has value "John Doe"
      And I click the "Back" link

      #Check that the previous site review is shown when recording a new one
      And I click the "Site review" link
      Then I check there is a "Record a site review" link
      And I check the "Site review outcome" field row has value "Satisfactory"
      And I click the "Return to VTS overview" link

      #Check that the site review outcome is shown on the organisation's service reports
      And I click the first {organisationName} link
      And I click the "Service reports" link
      And I find the {siteName} site in the service reports
      And I check the row with first cell value {siteNumber} has value "Satisfactory" in third cell
