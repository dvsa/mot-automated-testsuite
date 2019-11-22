@test27
Feature: 27 - Checking the correct error messages

  Scenario: Enter an incorrect mobile phone number and an incorrect code in text message
    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManger}
    And I click the button with id "continue"
    And I enter " " in the field with id "mobile-number"
    And I click the button with id "continue"
    And The page contains "Enter a mobile phone number"
    And I enter "1234" in the field with id "mobile-number"
    And I click the button with id "continue"
    And The page contains "Enter a mobile phone number, like 07700 900 982"
    And I enter unique mobile number in the field with id "mobile-number"
    And I click the button with id "continue"
    And I enter " " in the field with id "verification-code"
    And I click the button with id "continue"
    And The page contains "Enter the code in text message"
    And I enter "1234" in the field with id "verification-code"
    And I click the button with id "continue"
    And The page contains "Enter a valid code"
    And I enter "1234567" in the field with id "verification-code"
    And I click the button with id "continue"
    And The page contains "Enter a valid code"
    And I load immediately "MFA_GET_TEXT_CODE" as {verificationCode}
    And I enter {verificationCode} in the field with id "verification-code"
    And I click the button with id "continue"




#  Set up additional account security - <URL>/mfa/setup/authenticator
#  Scenario: Enter an invalid 6 digit access code
#    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManger}
#    And The page title contains "Create an account - MOT testing service"



#  This is the Sign in Your 6 digit security code <URL>/login-mfa
#  Scenario: Enter invalid digits on the Your 6 digit security code page
#    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManger}
#    And The page title contains "Your 6 digit security code - MOT testing service"
#    And I enter " " in the field with id "otp-code"
#    And I click the button with id "continue"
#    And The page contains "Enter the code in your authentication app"
#    And I enter "1234" in the field with id "otp-code"
#    And I click the button with id "continue"
#    And The page contains "Enter a 6 digit number"
#    And I enter "1234567" in the field with id "otp-code"
#    And I click the button with id "continue"
#    And The page contains "Enter a 6 digit number"
#     And I click the button with id "logout"


#  /mfa/setup/authenticator







