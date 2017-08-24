@regression
Feature: 13 - A01 user

  Scenario: AO1 user performs AE search, then changes AE status and Area Office
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "AO1_USER" as {ao1User}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I check the Authorised Examiner Business details AE ID is {aeReference}
    And I click the "Change" link for the "Status" field row
    And I select "Rejected" in the "Status" field
    And I press the "Change status" button
    Then The page contains "Status has been successfully changed"
    And I check the "Status" field row has value "Rejected"

    And I click the "Change" link for the "DVSA Area Office" field row
    And I select "08" in the "Area office" field
    And I press the "Change area office" button
    And The page contains "Area office has been successfully changed"
    And I check the "DVSA Area Office" field row has value "08"


  # AE search - View & record AE event
    # Log on as Area Office 1 with USER ID “NETO2855”.
    # Ensure home page is displayed
    # Click on “AE Information”
    # Search for AE number as an exp. “AE077806”
    #A new page is displayed with information’s.
    # Click on "Event history"
    # Ensure View page is displayed
    # Click link "Record an event"
    # in "Record an event" page
    # for Event field, select “Memo”
    # set days (must be in past) and press Continue button
    # On "Record an event outcome" page - Event outcome => "Closed" and typing notes, press Continue
    # Next page is displayed "Event Summary" and press "Record event" button.
    # returns to event history screen, check event on end of list is as entered above


  # AE search - Assign role
    # Log on as Area Office 1 with USER ID “NETO2855”.
    # Ensure home page is displayed
    # Click on “AE Information”
    # Search for AE number as an exp. “AE077806” (AE with no AEDM user already assigned)
    #A new page is displayed with information’s.
    # Click on “Assign a role”
    # enter Username such as “NIES1410” (any tester not already assigned to AE above), press "Search"
    # Choose a role "Authorised Examiner Designated Manager", and press Choose role button and then confirm button
    # check for: A role notification has been sent to Walker Judy Niess 'NIES1410'.
    # check for: user listed in Roles table with role "Authorised examiner designated manager"


  # AE search - remove role
    # Log on as Area Office 1 with USER ID “NETO2855”.
    # Ensure home page is displayed
    # Click on “AE Information”
    # Search for AE number as an exp. “AE077806” (AE with AEDM user already assigned)
    #A new page is displayed with information’s.
    # Click “Remove” link in Roles section
    # Press “Remove Role” button
    # Make sure selection has been removed.
    # check for: You have removed the role of Authorised examiner designated manager from Katharyn Yuki Mellado
    # check for: Roles table has no user with that role


  # AE search - Add Site association
    # Log on as Area Office 1 with USER ID “NETO2855”.
    # Ensure home page is displayed
    # Click on “AE Information”
    # Search for AE number as an exp. “AE077806” (approved AE)
    # Section “Vehicle testing stations” and click “Add a site association”.
    # Enter Site ID such as “VTS046462” (approved site not assigned to any AE) and press the button “Associate this site”.


  # AE search - remove Site association
    # Log on as Area Office 1 with USER ID “NETO2855”.
    # Ensure home page is displayed
    # Click on “AE Information”
    # Search for AE number as an exp. “AE077806”
    # Section “Vehicle testing stations” and click on “Remove” link
    # Choose a status such as “Surrendered” and press Remove association button
    # check for: Vehicle testing stations => doesn't include VTS


  # Site Information search - Change site details (name, status & class)
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # Click on "Site Information"
    # Searching for site information by…  “Site ID” exp VTS000948  etc…
    # click Class 1 to 4, then Search
    # click first search result
    # in VTS screen
    # Change site details (name, status & class)
    # Name => Change
    # enter site name, click Change
    # check for => Site name has been successfully changed.
    # check for -> Name row updated

    # status => Change
    # change to "Lapsed", click change
    # check for => Site status has been successfully changed.
    # check for => Status row updated

    # class => Change
    # select just 4..7, click Review
    # click Change
    # check for => Classes have been successfully changed.
    # check for => Classes row updated


  # Site Information search - Assign role
    # same logic as site mgr?


  # Site Information search - Remove role
    # same logic as site mgr?


  # View & record VTS event
    # same as AE event (see above)


  # User Search - Change tester Group status
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "User search"
    # enter any tester username (not Group A qualified, is Group B), Search
    # click on first result
    # click on "Change Group A qualification status"
    # set status to "Qualified", press change status
    # summary, press confirm
    # check for => Group A tester qualification status has been changed to Qualified
    # check for => Group A - has "Qualified" button

    # click on "Change Group B qualification status"
    # set status to "Suspended", press change status
    # summary, press confirm
    # check for => Group B tester qualification status has been changed to Suspended
    # check for => Group B - has "Suspended" button


  # User Search - Edit Driver Licence no.
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "User search"
    # enter any tester username (any), Search
    # click on first result
    # Driving License => Change
    # change license from MCEWI712056DP9TV to MCEWI712056DP9TA, click Review
    # click change
    # check for => Driving licence has been changed successfully.
    # check for => Driving license row updated


  # User Search - Remove Driver Licence no.
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "User search"
    # enter any tester username (any), Search
    # click on first result
    # Driving License => Change
    # click on "Remove driving licence" link
    # press "Remove driving license" button
    # check for => Driving licence has been successfully removed.
    # check for => Driving license table has "None recorded"


  # User Search - Add Driver Licence no.
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "User search"
    # enter any tester username (any), Search
    # click on first result
    # Driving License => Change
    # enter "Driving license number" => HIELS607129SH9QG, "Issuing country" => "Great Britain (England, Scotland and Wales)"
    # press "Review driving license" button
    # Press "Change driving license" button
    # check for => Driving licence has been changed successfully.
    # check for => Driving license table has number above


  # View & record person event
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "User search"
    # enter any tester username (any), Search
    # click on first result
    # Event history link
    # as above...
    # check for => A new event has been recorded.


  # Vehicle Information search (vehicle + test details)
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "Vehicle information"
    # select "VIN/Chassis", search icon
    # check get correct results, click on vehicle
    # check has correct details


  # MOT Tests - Site
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "MOT tests"
    # "Site (recent tests)", enter site id
    # get list of recent tests


  # MOT Tests - VRM / VIN
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "MOT tests"
    # "VIN/Chassis", enter VIN, click search icon
    # get list of recent tests


  Scenario: AO1 user creates a new Authorised Examiner
    Given I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "Create an Authorised Examiner" link
    And I enter "Example MOT Testers" in the "Business name" field
    And I enter "Example MOT Testers Ltd" in the "Trading name" field
    And I select "Sole trader" in the "Business type" field
    And I enter "10 Example Street" in the "Address line" field
    And I enter "Example Town" in the "Town or city" field
    And I enter "AB1 2CD" in the "Postcode" field
    And I enter "01234567890" in the "Phone number" field
    And I enter "no_one@example.com" in the "Email address" field
    And I enter "no_one@example.com" in the "Re-type email address" field
    And I click the "Yes" radio button in fieldset "Are they the same as the business contact details?"
    And I select "01" in the "DVSA Area Office" field
    And I press the "Continue" button
    And I press the "Create Authorised Examiner" button
    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value "Example MOT Testers"
    And I check the "Trading name" field row has value "Example MOT Testers Ltd"
    And I check the "Business type" field row has value "Sole trader"
    And I check the "DVSA Area Office" field row has value "01"
    And I check the "Status" field row has value "Applied"


  # Create VTS
    # Log on as Area Office 1 user (AO1) with USER ID “IRET3149”
    # "Create a site"
    # Site name => Chris Site
    # Site type => VTS
    # address, email, telephone
    # How many two-person test lanes (TPTL) does the site have? 2
    # How many one-person test lanes (OPTL) does the site have? 3
    # Classes 1..4
    # Continue
    # Create
    # Check for => details entered above are on Site Summary page
