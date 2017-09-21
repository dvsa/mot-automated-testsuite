@regression
Feature: 19 - New trade user orders and activates 2FA card

  # Scenario 1
  # Created new user ZZZZ0001.
  # AO1 user assigns role of AED to user.
  # Log in as ZZZZ0001.
  # Notification present "Nomination for Authorised Examiner Delegate - order a security card" so order card.
  # Go to Your profile and select "Activate your security card"
  # Entered serial "ZZZZ000000000015" and pin with drift zero
  # Card activated.
  # Check your profile screen has correct security card details (serial and activation date)
  # Log out and log in with pin drift zero
  # Notification present "Authorised Examiner Delegate nomination" accept role, log out and back in to check you are now AED.

  Scenario Outline: New tester adds training certificate then activates security card with drift <drift>
    # Create new user TEST<nnnn>
    Given I generate a unique email address as {email}
    And I browse to /login
    And I click the text "Don't have an account"
    And I click the "Create a new account" link
    And I click the "Continue" link
    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Continue" button
    And I enter "Fred" in the "First name" field
    And I enter "Tester" in the "Last name" field
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
    And I press the "Continue" button
    And I enter "MyPassword1234" in the "Create a password" field
    And I enter "MyPassword1234" in the "Re-type your password" field
    And I press the "Continue" button
    And I press the "Create your account" button

    # Add Group A training certificate, then order card
    When I browse to /login
    And I load immediately "LATEST_TEST_USER" as {username}
    And I load "SITE" as {siteName}, {siteNumber}
    And I set today as {certDay}, {certMonth}, {certYear}
    And I enter {username} in the "User ID" field
    And I enter "MyPassword1234" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your home"
    And I click the "Your profile" link
    And The page title contains "Your profile"
    And I click the "MOT tester training certificates" link
    And I click the first "Add certificate and request a demo test" link
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
    And I load immediately "2FA_CARD_UNUSED" as {serialNumber}
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