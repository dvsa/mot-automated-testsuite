@regression @test19
Feature: 19 - New trade user orders and activates 2FA card

  Scenario: New non-tester orders and activates security card, then logs in
    # Create a new user
    Given I generate a unique email address as {email}
    And I browse to /login
    And I click the text "Don't have an account"
    And I click the "Create a new account" link
    And I click the "Continue" link
    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Continue" button
    And I enter "Bert" in the "First name" field
    And I enter "William" in the "Middle name (optional)" field
    And I enter "Varcher" in the "Last name" field
    And I enter "20" in the "Day" field
    And I enter "07" in the "Month" field
    And I enter "1969" in the "Year" field
    And I press the "Continue" button
    And I enter "1 Some Street" in the "Home address" field
    And I enter "Some Area" in the "Address line 2" field
    And I enter "Somecity" in the "Town or city" field
    And I enter "SW1 1AA" in the "Postcode" field
    And I enter "01234 567890" in the "Enter your home, mobile or work number" field
    And I press the "Continue" button
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I select "In what city or town did you spend your honeymoon?" in the field with id "question3"
    And I enter "MOT Testing Centre" in the field with id "answer3"
    And I press the "Continue" button
    And I enter "MyPassword1234" in the "Create a password" field
    And I enter "MyPassword1234" in the "Re-type your password" field
    And I press the "Continue" button
    And I press the "Create your account" button

    # AO1 user assigns role of AEDM to the new user
    And I load immediately "LATEST_TEST_USER" as {username}
    And I load "AE_WITH_NO_AEDM" as {aeReference}, {aeName}, {unused1}, {unused2}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "AE information" link
    And I enter {aeReference} in the "Authorised Examiner ID" field
    And I press the "Search" button
    And I click the "Assign a role" link
    And I enter {username} in the field with id "userSearchBox"
    And I press the "Search" button
    And I click the "Authorised Examiner Designated Manager" radio button in fieldset "Select a role"
    And I press the "Choose role" button
    And I press the "Confirm" button
    And The page contains "A role notification has been sent to Bert William Varcher '{username}'."
    And I click the "Sign out" link

    # Login as the new user and order a card
    And I browse to /login
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your home"
    And I click the link "notifications" with id "notification-alert"
    And I click the "Nomination for Authorised Examiner Designated Manager - order a security card" link
    And The page contains "You have been nominated for Authorised Examiner Designated Manager at {aeName}."
    And I click the "Order a security card" link
    And The page title contains "Order a security card"
    And I click the "Continue" link
    And The page title contains "Choose a delivery address"
    And I click the "Home" radio button
    And I press the "Continue" button
    And The page title contains "Review delivery address"
    And I press the "Order security card" button
    And The page contains "Your security card has been ordered"

    # Activate the card
    And I click the "Continue to home" link
    And I click the "View all messages" link
    And I click the "Nomination for Authorised Examiner Designated Manager - activate your security card" link
    And The page contains "You will receive the role of Authorised Examiner Designated Manager at {aeName} once you have activated the card."
    And I load "2FA_CARD_UNUSED" as {serialNumber}
    And I click the "Activate your security card" link
    And I generate 2FA PIN with drift +0 as {pin}
    And I enter {serialNumber} in the "Serial number" field
    And I enter {pin} in the "Security card PIN" field
    And I press the "Activate your security card" button
    And The page contains "Your security card has been activated"
    And I click the "Continue to home page" link
    And I click the "Your profile" link
    And I set today formatted using "d MMMM YYYY" as {activationDate}
    And I check the "Activation date" field row has value {activationDate}
    And I check the "Serial number" field row has value {serialNumber}

    # Log out then log in with drift zero and accepts AEDM role
    And I click the "Sign out" link
    And I browse to /login
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with drift +0 as {newPin}
    And I enter {newPin} in the "Security card PIN" field
    And I press the "Sign in" button
    And The page title contains "Your home"
    And I click the "View all messages" link
    And I click the "Authorised Examiner Designated Manager role notification" link
    And The page contains "You have been assigned a role of Authorised Examiner Designated Manager for {aeName} by DVSA administration."

    # Log out and back in to check new user is now an AEDM
    And I click the "Sign out" link
    And I browse to /login
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with drift +0 as {newPin}
    And I enter {newPin} in the "Security card PIN" field
    And I press the "Sign in" button
    And The page title contains "Your home"
    And I click the "Your profile" link
    And I click the "Roles and associations" link
    And The page contains "Authorised examiner designated manager"


  Scenario Outline: New tester adds training certificate, orders and activates security card with drift <drift>, then logs in
    # Create new user
    Given I generate a unique email address as {email}
    And I browse to /login
    And I click the text "Don't have an account"
    And I click the "Create a new account" link
    And I click the "Continue" link
    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Continue" button
    And I enter "Fred" in the "First name" field
    And I enter "Varcher" in the "Last name" field
    And I enter "20" in the "Day" field
    And I enter "07" in the "Month" field
    And I enter "1969" in the "Year" field
    And I press the "Continue" button
    And I enter "1 Some Street" in the "Home address" field
    And I enter "Some Area" in the "Address line 2" field
    And I enter "Somecity" in the "Town or city" field
    And I enter "SW1 1AA" in the "Postcode" field
    And I enter "01234 567890" in the "Enter your home, mobile or work number" field
    And I press the "Continue" button
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I select "In what city or town did you spend your honeymoon?" in the field with id "question3"
    And I enter "MOT Testing Centre" in the field with id "answer3"
    And I press the "Continue" button

    And I enter "MyPassword1234" in the "Create a password" field
    And I enter "MyPassword1234" in the "Re-type your password" field
    And I press the "Continue" button
    And I press the "Create your account" button

    # Add Group A training certificate, then order card
    When I browse to /login
    And I load immediately "LATEST_TEST_USER" as {username}
    And I load "SITE_ALL" as {siteNumber}, {siteName}
    And I set today as {certDay}, {certMonth}, {certYear}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your home"
    And I click the "Your profile" link
    And The page title contains "Your profile"
    And I click the "View or add initial training qualifications" link
    And I click the first "Add your certificate" link
    And I enter "XYZ54321" in the "Certificate number" field
    And I enter {certDay} in the "Day" field
    And I enter {certMonth} in the "Month" field
    And I enter {certYear} in the "Year" field
    And I enter {siteNumber} in the "VTS ID" field
    And I press the "Review certificate details" button
    And I press the "Confirm details" button
    And The page contains "Your Group A certificate has been successfully added"
    And I click the "order your security card" link
    And The page title contains "Order a security card"
    And I click the "Continue" link
    And The page title contains "Choose a delivery address"
    And I click the "Home" radio button
    And I press the "Continue" button
    And The page title contains "Review delivery address"
    And I press the "Order security card" button
    And The page contains "Your security card has been ordered"
    And I click the "Continue to home" link

    # Activate the security card
    And I load "2FA_CARD_UNUSED" as {serialNumber}
    And I click the "Your profile" link
    And The page title contains "Your profile"
    And I click the "Activate your security card" link
    And I generate 2FA PIN with drift <drift> as {pin}
    And I enter {serialNumber} in the "Serial number" field
    And I enter {pin} in the "Security card PIN" field
    And I press the "Activate your security card" button
    And The page contains "Your security card has been activated"
    And I click the "Continue to home page" link
    And I click the "Your profile" link
    And I set today formatted using "d MMMM YYYY" as {activationDate}
    And I check the "Activation date" field row has value {activationDate}
    And I check the "Serial number" field row has value {serialNumber}

    # Log out then log in with drift zero (i.e. no offset from previous drift). Demo mode banner should be present.
    And I click the "Sign out" link
    And I browse to /login
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with drift <drift> as {newPin}
    And I enter {newPin} in the "Security card PIN" field
    And I press the "Sign in" button
    Then The page title contains "Your home"
    And The page contains "Your access is restricted to the demo mode. Only fully qualified testers employed at a VTS have access to the full system."

    Examples:
      | drift |
      | -20   |
      | -10   |
      | +0    |
      | +10   |
      | +20   |