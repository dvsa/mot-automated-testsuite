@regression
Feature: 03 - reset password

  Scenario: User creates a new account and then reset their password
    Given I login with 2FA using "MOT_TESTER_CLASS_2" as {username}, {site}

    When I click the "Your profile" link
    Then The page title contains "Your profile"
    And I click the "Change your password" link
    And The page title contains "Change your password"

    # Check password length validation
    And I enter "Password1" in the "Current password" field
    And I enter "Pass1" in the "New password" field
    And I enter "Pass1" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Change your password"
    And The page contains "must be at least 8 characters long"

    # Check numerical password validation
    And I enter "Password1" in the "Current password" field
    And I enter "Password" in the "New password" field
    And I enter "Password" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Change your password"
    And The page contains "Must contain 1 or more numbers"

    # Check upper case and lower case validation
    And I enter "Password1" in the "Current password" field
    And I enter "password1" in the "New password" field
    And I enter "password1" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Change your password"
    And The page contains "Must contain both upper and lower case characters"

    # Check common password validation
    And I enter "Password1" in the "Current password" field
    And I enter "qwerty123" in the "New password" field
    And I enter "qwerty123" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Change your password"
    And The page contains "must not be a common password"

    # Change the password properly
    And I enter "Password1" in the "Current password" field
    And I enter "Password2" in the "New password" field
    And I enter "Password2" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Your profile"
    And The page contains "Your password has been changed"
    And I click the "Sign out" link

    # Check old password fails
    Then I browse to /login
    And The page title contains "Sign in"
    And I enter {username} in the "User ID" field
    And I enter "Password1" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Sign in"
    And The page contains "There was a problem with your User ID or password"

    # Check the new password works and can log in
    Then I browse to /login
    And The page title contains "Sign in"
    And I enter {username} in the "User ID" field
    And I enter "Password2" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with drift +0 as {newPin}
    And I enter {newPin} in the "Security card PIN" field
    And I press the "Sign in" button
    Then The page title contains "Your home"

    When I click the "Your profile" link
    Then The page title contains "Your profile"
    And I click the "Change your password" link
    And The page title contains "Change your password"

    # Check password history validation
    And I enter "Password2" in the "Current password" field
    And I enter "Password2" in the "New password" field
    And I enter "Password2" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Change your password"
    And The page contains "password was found in the password history"

    # Change the password properly
    And I enter "Password2" in the "Current password" field
    And I enter "Password3" in the "New password" field
    And I enter "Password3" in the "Re-type your new password" field
    And I press the "Change password" button
    And The page title contains "Your profile"
    And The page contains "Your password has been changed"
    And I click the "Sign out" link

    # Check the new password works and can log in
    Then I browse to /login
    And The page title contains "Sign in"
    And I enter {username} in the "User ID" field
    And I enter "Password3" in the "Password" field
    And I press the "Sign in" button
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with drift +0 as {newPin}
    And I enter {newPin} in the "Security card PIN" field
    And I press the "Sign in" button
    Then The page title contains "Your home"