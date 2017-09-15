@regression
Feature: 20 - New user orders and activates card

  # Scenario: New user assigned trade role orders and activates card
    # Log on as new user with no roles
    # Go to Your Profile page
    # Select MOT Tester training certificates
    # Select to add certificate for Group B
    # Enter certificate number, date awarded (check validation)
    # Review certificate details
    # Check details match what was entered and confirm details
    # Ensure message to order card is visible and select it
    # Select new address
    # Enter address (check validation) and continue. Ensure address matches
    # Click Order card
    # Continue to home
    # Select Your Profile and ensure activate card is visible
    # Log out
    # Log back in with same user and ensure that message to activate card is visible
    # Next day log on as CAT user and select Security card order list. Ensure card ordered above is visible on download
    # (ordered cards appear on "Security Card order list")

  # Scenario 1
  # Created new user ZZZZ0001.
  # DVSA user assigns role of AED to user.
  # Log in as ZZZZ0001.
  # Notification present "Nomination for Authorised Examiner Delegate - order a security card" so order card.
  # Go to Your profile and select "Activate your security card"
  # Entered serial "ZZZZ000000000015" and pin with drift zero
  # Card activated.
  # Check your profile screen has correct security card details (serial and activation date)
  # Log out and log in with pin drift zero
  # Notification present "Authorised Examiner Delegate nomination" accept role, log out and back in to check you are now AED.

  # Scenario 2
  # Created new user ZZZZ0002.
  # Go to your profile and enter "MOT tester training certificates" then "Add certificate and request a demo test Certificate"
  # Order security card
  # Go to Your profile and select "Activate your security card"
  # Entered serial "ZZZZ000000000021" and pin with drift zero
  # Card activated.
  # Check your profile screen has correct security card details (serial and activation date)
  # Log out and log in with pin drift zero. Demo mode banner should be present.