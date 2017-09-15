@regression
Feature: 21 - 2FA: Forgotten Card

  # Scenario: 2FA active user can login via security questions
    # Pre-req: 2FA user
    # Log in with username/password
    # Select Lost, forgotten or damaged security card?
    # Continue to sign in without security card
    # Enter security question answers
    # Expected result: Sign in successful page displayed, Continue to home page

  # Scenario: Subsequent logins that day do not ask for 2FA pin
    # 2FA active user can login directly via security questions within a 24 hour period
    # Log in via lost/forgotten journey above
    # Log out
    # Log in with username/password
    # Expected result: User directed straight to enter security question page

  # Scenario: User prompted to logon via PIN next day & able to login with existing card
    # Not worth automating?  Unless we can delete cookies - the above test sets a cookie with an expiry date of midnight of same day
    # (yes we can delete/set test cookies)

  # Logged in as THOB5731 logging in with security questions.
  # Signed out and signed back in to be automatically presented security questions instead of security card pin.
  # To check PIN prompt next day sign out and clear all cookies and then refresh page then tried to log in and was presented security card pin again.
  # Will also check again properly by letting cookies expire naturally overnight.