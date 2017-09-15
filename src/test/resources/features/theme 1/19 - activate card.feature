@regression
Feature: 19 - 2FA: Activate Card

  # Scenario: Security Card section on profile page displays 2fa card details.
    # 2FA Security card details displayed within the profile page for a 2FA active user
    # Pre-req: 2FA active trade user
    # Log in with user
    # Navigate to the profile page
    # Expected result: 2FA security cards details displayed

  # Using users from number 22
  # Scenario 1
  # Logged in as ROHR4241
  # Go to Your profile and select "Activate your security card"
  # Entered serial "ZZZZ000000000003" and pin with drift zero
  # Card activated.
  # Check your profile screen has correct security card details (serial and activation date)
  # Log out and log in with pin drift zero

  # Scenario 2
  # Logged in as SHOL8522
  # Go to Your profile and select "Activate your security card"
  # Entered serial "ZZZZ000000000009" and pin with drift 20
  # Card activated.
  # Check your profile screen has correct security card details (serial and activation date)
  # Log out and log in with pin drift 20. Drift zero should fail.