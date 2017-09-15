@regression
Feature: 22 - 2FA: Lost Card

  # Scenario: User can order a new card via login screens (after logging on via sec. ques.)
  # User can order a new security card after logging in via lost and forgotten journey
    # Log in with username/password
    # Select Lost, forgotten or damaged security card?
    # Continue to sign in without security card
    # Enter security question answers
    # Order a new card via order new security card link
    # Expected results: New security card ordered

  # Scenario: User can order new card via profile page (if logged on via sec. ques and not already ordered one)
  # User who has logged in via lost and forgotten journey can order a new card via profile page
    # Log in with username/password
    # Select Lost, forgotten or damaged security card?
    # Continue to sign in without security card
    # Enter security question answers
    # Continue to home page
    # Navigate to your profile
    # Select Order a security card and complete order card journey
    # Expected results: New security card ordered

  # Scenario: User logs on via security questions until replacement card activated
  # 2FA active user who has ordered a replacement card can login via security questions until card activated
    # Log in via lost/forgotten journey
    # Order card
    # Log out
    # Log in with username/password
    # Expected results: User directed straight to enter security question page


  # Scenario 1
  # Logged in as user ROHR4241 using security questions ordered new security question from login screen.
  # Checked notification present "You have ordered a security card"
  # Checked Your profile - "Activate your security card" link is available and security card section is no longer displayed
  # Logged out and logged back in to see notification about signing in without card and answered security questions.

  # Scenario 2
  # Logged in as SHOL8522 using security questions and went to homepage
  # Checked Your profile had "Order a security card" link and existing security card details are still present.
  # Selected "Order a security card" and ordered a new card
  # Checked notification present "You have ordered a security card"
  # Checked Your profile - "Activate your security card" link is available and security card section is no longer displayed.
  # Logged out and logged back in to see notification about signing in without card and answered security questions.