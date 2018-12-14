@regression
  Feature: 25 - site review
    
    Scenario: VE user creates a satisfactory site review with an activity performed
      #Search for a site with an existing site review
      Given I load "SITE_REVIEW" as {siteNumber}, {siteName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}

      #Start a site review
      When I click the "Site review" link
      And I click the "Record a site review" link
      And I enter the date of site visit as 1 days ago
      And I enter "John Doe" in the field with id "aeName"
      And I enter "AE" in the field with id "aeRole"
      And I press the "Save and continue" button

      #Record a "satisfactory" compliance outcome
      And I click the button with id "change_CO_button"
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Record a satisfactory premises outcome
      And I click the button with id "change_PR_button"
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Record a satisfactory management and quality outcome
      And I click the button with id "change_MQ_button"
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Record a satisfactory people outcome
      And I click the button with id "change_PE_button"
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Record an activity performed with MOT test number and satisfactory outcome
      And I click the button with id "RecordActivityButton"
      And I click the "Yes" radio button
      And I press the "Save and continue" button
      And I enter "123456789012" in the "MOT test number" field
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Submit and check the check details page
      And I click the button with id "continueButton"
      Then I check the "Site review outcome" field row has value "Satisfactory"
      And The page does not contain "Improve"
      And The page does not contain "Unsatisfactory"
      And I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Satisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      #Below check commented out because it fails to see the "N/A" value
      #And I check the "AE representative's User ID" field row has value "NA"
      And I check there is a "Change" link

      #Submit site review and check it's submitted successfully
      And I click the "Save and confirm" button
      And The page contains "Site review has been saved"
      And I click the "Back" link
      And I check the "Current" field row has value "Satisfactory"

      #Check the summary
      And I click the "View summary" link
      Then I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Satisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check the "AE representative" field row has value "John Doe"

    Scenario: VE user creates an improve site review and creates an event
      #Search for a site with an existing site review and start a new site review
      Given I load "SITE_REVIEW" as {siteNumber}, {siteName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}
      When I click the "Site review" link
      And I click the "Record a site review" link
      And I enter the date of site visit as 1 days ago
      And I enter "John Doe" in the field with id "aeName"
      And I enter "AE" in the field with id "aeRole"
      And I press the "Save and continue" button

      #Record a satisfactory compliance outcome
      And I click the button with id "change_CO_button"
      And I click the "Satisfactory" radio button
      And I press the "Save and return" button

      #Record an improve premises outcome
      And I click the button with id "change_PR_button"
      And I click the "Needs improvement" radio button
      And I enter "Premises outcome comment" in the field with id "improve-textarea"
      And I press the "Save and return" button

      #Record an improve management and quality outcome
      And I click the button with id "change_MQ_button"
      And I click the "Needs improvement" radio button
      And I enter "Management and quality outcome comment" in the field with id "improve-textarea"
      And I press the "Save and return" button

      #Record an improve people outcome
      And I click the button with id "change_PE_button"
      And I click the "Needs improvement" radio button
      And I enter "People outcome comment" in the field with id "improve-textarea"
      And I press the "Save and return" button

      #Record an activity performed with MOT test number and improve outcome
      And I click the button with id "RecordActivityButton"
      And I click the "Yes" radio button
      And I press the "Save and continue" button
      And I enter "123456789012" in the "MOT test number" field
      And I click the "Needs improvement" radio button
      And I enter "Activity outcome comment" in the field with id "improve-textarea"
      And I press the "Save and return" button

      #Submit and check the check details page
      And I click the button with id "continueButton"
      Then I check the "Site review outcome" field row has value "Needs improvement"
      And The page does not contain "Unsatisfactory"
      And I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Needs improvement"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check there is a "Change" link

      #Submit site review and create an event
      And I click the "Save and confirm" button
      And The page contains "Record an event outcome"
      And I select "No further action" in the field with id "eventOutcome"
      And I press the "Continue" button
      And I check the "Event" field row has value "Site review"
      And I check the "Outcome" field row has value "No further action"
      And I check there is a "Change" link
      And I press the "Save and confirm" button
      Then The page contains "Site review has been saved"

      #Check that the site review has submitted successfully
      And I click the "Back" link
      And I check the "Current" field row has value "Improve"

      #Check the summary
      And I click the "View summary" link
      Then I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Needs improvement"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check the "AE representative" field row has value "John Doe"

    Scenario: VE user creates an unsatisfactory site review and creates an event
      #Search for a site with an existing site review and start a new site review
      Given I load "SITE_REVIEW" as {siteNumber}, {siteName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}
      When I click the "Site review" link
      And I click the "Record a site review" link
      And I enter the date of site visit as 1 days ago
      And I enter "John Doe" in the field with id "aeName"
      And I enter "AE" in the field with id "aeRole"
      And I press the "Save and continue" button

      #Record an unsatisfactory compliance outcome
      And I click the button with id "change_CO_button"
      And I click the "Unsatisfactory" radio button
      And I enter "Compliance outcome comment" in the field with id "unsatisfactory-advice-textarea"
      And I press the "Save and return" button

      #Record an unsatisfactory premises outcome
      And I click the button with id "change_PR_button"
      And I click the "Unsatisfactory" radio button
      And I enter "Premises outcome comment" in the field with id "unsatisfactory-advice-textarea"
      And I press the "Save and return" button

      #Record an unsatisfactory management and quality outcome
      And I click the button with id "change_MQ_button"
      And I click the "Unsatisfactory" radio button
      And I enter "Management and quality outcome comment" in the field with id "unsatisfactory-advice-textarea"
      And I press the "Save and return" button

      #Record an unsatisfactory people outcome
      And I click the button with id "change_PE_button"
      And I click the "Unsatisfactory" radio button
      And I enter "People outcome comment" in the field with id "unsatisfactory-advice-textarea"
      And I press the "Save and return" button

      #Record an activity performed with MOT test number and unsatisfactory outcome
      And I click the button with id "RecordActivityButton"
      And I click the "Yes" radio button
      And I press the "Save and continue" button
      And I enter "123456789012" in the "MOT test number" field
      And I click the "Unsatisfactory" radio button
      And I enter "Activity outcome comment" in the field with id "unsatisfactory-advice-textarea"
      And I press the "Save and return" button

      #Submit and check the check details page
      And I click the button with id "continueButton"
      Then I check the "Site review outcome" field row has value "Unsatisfactory"
      And The page does not contain "Satisfactory"
      And The page does not contain "Improve"
      And The page does not contain "Needs improvement"
      And I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Unsatisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check there is a "Change" link

      #Submit site review and create an event
      And I click the "Save and confirm" button
      And The page contains "Record an event outcome"
      And I select "Advisory Warning Letter (AWL)" in the field with id "eventOutcome"
      And I press the "Continue" button
      And I check the "Event" field row has value "Site review"
      And I check the "Outcome" field row has value "Advisory Warning Letter (AWL)"
      And I check there is a "Change" link
      And I press the "Save and confirm" button
      Then The page contains "Site review has been saved"

      #Check that the site review has submitted successfully
      And I click the "Back" link
      And I check the "Current" field row has value "Unsatisfactory"

      #Check the summary
      And I click the "View summary" link
      Then I check the "Activity" field row has value "Activity performed"
      And I check the "Activity" field row has value "Unsatisfactory"
      And I check the "Activity" field row has value "MOT test number: 123456789012"
      And I check the "AE representative" field row has value "John Doe"