@regression @test21
Feature: 21 - Existing user has lost 2FA card

  Scenario: Existing user orders new card via security questions screens, then activates it, then logs in
    # Login via security questions, change insecure answers to questions
    Given I login and click "Lost, forgotten or damaged security" card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "answer" in the field with id "answer"
    And I press the "Continue" button
    And The page title contains "Change security questions - Reset your account security - MOT testing service"
    And I press the "Continue" button
    And The page title contains "MOT testing service"
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I select "What is the name of the street where you grew up?" in the field with id "question3"
    And I enter "Secure answer44" in the field with id "answer3"
    And I press the "Continue" button
    And The page title contains "Review security question changes - MOT testing service"
    And I press the "Save Changes" button
    And The page title contains "Your security questions have been changed - MOT testing service"
    And I click the "Continue to home" link
    And The page title contains "Your home"


#    # Login via security questions, order a new card
#    And I click the "Sign out" link
#    And I login without 2FA as {username}
#    And The page title contains "Your security card PIN - MOT testing service"
#    And I click the "Lost, forgotten or damaged security card?" link
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
#    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
#    And I press the "Continue" button
#    And The page title contains "Sign in successful"
#    And The page contains "You have signed in without your security card"
#    When I click the "order a new security card" link
#    And The page title contains "Order a security card"
#    And I click the "Continue" link
#    And The page title contains "Choose a delivery address"
#    And I click the "Home" radio button
#    And I press the "Continue" button
#    And The page title contains "Review delivery address"
#    And I press the "Order security card" button
#    And The page contains "Your security card has been ordered"
#    And I click the "Sign out" link
#
#    # Login via security questions, activate the new card
#    And I load "2FA_CARD_UNUSED" as {serialNumber}
#    And I login without 2FA as {username}
#    And The page contains "You have ordered a new card. Until you receive and activate the card, sign in with your security questions."
#    And I click the "Continue" link
#    And The page title contains "Your security questions"
#    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
#    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
#    And I press the "Continue" button
#    And The page title contains "Sign in successful"
#    And The page contains "You have signed in without your security card"
#    And I click the "Continue to home" link
#    And The page title contains "Your home"
#    And I click the "Your profile" link
#    And The page title contains "Your profile"
#    And I click the "Activate your security card" link
#    And I generate 2FA PIN with drift +0 as {pin}
#    And I enter {serialNumber} in the "Serial number" field
#    And I enter {pin} in the "Security card PIN" field
#    And I press the "Activate your security card" button
#    And The page contains "Your security card has been activated"
#    And I click the "Continue to home page" link
#    And I click the "Your profile" link
#    And I set today formatted using "d MMMM YYYY" as {activationDate}
#    And I check the "Activation date" field row has value {activationDate}
#    And I check the "Serial number" field row has value {serialNumber}
#    And I click the "Sign out" link
#
#    # Login with new card
#    And I login with 2FA as {username}
#    Then The page title contains "Your home"
#
#
#  Scenario: Existing user orders new card via profile screen, then activates it, then logs in
#    # Login via security questions, change insecure answers to questions
#    Given I login and click "Lost, forgotten or damaged security" card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
#    And The page title contains "Sign in without your security card"
#    When I click the "Continue" link
#    And I enter "answer" in the {question1} field
#    And I enter "answer" in the {question2} field
#    And I press the "Continue" button
#    And The page title contains "Change security questions - Reset your account security - MOT testing service"
#    And I press the "Continue" button
#    And The page title contains "First security question - MOT testing service"
#    And I select "What did you want to be when you grew up?" in the field with id "question1"
#    And I enter "MOT Tester" in the field with id "answer1"
#    And I press the "Continue" button
#    And The page title contains "Second security question - MOT testing service"
#    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
#    And I enter "MOT Test Centre" in the field with id "answer2"
#    And I press the "Continue" button
#    And The page title contains "Review security question changes - MOT testing service"
#    And I press the "Save Changes" button
#    And The page title contains "Your security questions have been changed - MOT testing service"
#    And I click the "Continue to home" link
#    And The page title contains "Your home"
#
#    # Login via security questions, order a new card
#    And I click the "Sign out" link
#    And I login without 2FA as {username}
#    And The page title contains "Your security card PIN - MOT testing service"
#    And I click the "Lost, forgotten or damaged security card?" link
#    And The page title contains "Sign in without your security card"
#    And I click the "Continue" link
#    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
#    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
#    And I press the "Continue" button
#    And The page title contains "Sign in successful"
#    And The page contains "You have signed in without your security card"
#    And I click the "Continue to home" link
#    And The page title contains "Your home"
#    And I click the "Your profile" link
#    And The page title contains "Your profile"
#    And I click the "Order a security card" link
#    And The page title contains "Order a security card"
#    And I click the "Continue" link
#    And The page title contains "Choose a delivery address"
#    And I click the "Home" radio button
#    And I press the "Continue" button
#    And The page title contains "Review delivery address"
#    And I press the "Order security card" button
#    And The page contains "Your security card has been ordered"
#    And I click the "Sign out" link
#
#    # Login via security questions, activate the new card
#    And I load "2FA_CARD_UNUSED" as {serialNumber}
#    And I login without 2FA as {username}
#    And The page contains "You have ordered a new card. Until you receive and activate the card, sign in with your security questions."
#    And I click the "Continue" link
#    And The page title contains "Your security questions"
#    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
#    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
#    And I press the "Continue" button
#    And The page title contains "Sign in successful"
#    And The page contains "You have signed in without your security card"
#    And I click the "Continue to home" link
#    And The page title contains "Your home"
#    And I click the "Your profile" link
#    And The page title contains "Your profile"
#    And I click the "Activate your security card" link
#    And I generate 2FA PIN with drift +0 as {pin}
#    And I enter {serialNumber} in the "Serial number" field
#    And I enter {pin} in the "Security card PIN" field
#    And I press the "Activate your security card" button
#    And The page contains "Your security card has been activated"
#    And I click the "Continue to home page" link
#    And I click the "Your profile" link
#    And I set today formatted using "d MMMM YYYY" as {activationDate}
#    And I check the "Activation date" field row has value {activationDate}
#    And I check the "Serial number" field row has value {serialNumber}
#    And I click the "Sign out" link
#
#    # Login with new card
#    And I login with 2FA as {username}
#    Then The page title contains "Your home"



  #    And I enter "answer" in the {question1} field
#    And I enter "answer" in the {question2} field