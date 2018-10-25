@regression
Feature: 20 - Existing user has forgotten 2FA card

  Scenario: Active 2FA card user can login via security questions with the Temporary sign in link
    # Login via security questions, change insecure answers to questions
    Given I login and click "temporary" card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "answer" in the {question1} field
    And I enter "answer" in the {question2} field
    And I press the "Continue" button
    And The page title contains "Change security questions - Reset your account security - MOT testing service"
    And I press the "Continue" button
    And The page title contains "First security question - MOT testing service"
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I press the "Continue" button
    And The page title contains "Second security question - MOT testing service"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I press the "Continue" button
    And The page title contains "Review security question changes - MOT testing service"
    And I press the "Save Changes" button
    And The page title contains "Your security questions have been changed - MOT testing service"
    And I click the "Continue to home" link
    And The page title contains "Your home"

    # Subsequent logins that day do ask for 2FA pin, because thew Temporary sign in link is used
    And I click the "Sign out" link
    And I login without 2FA as {username}
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with previous drift {lastDrift} as {pin}
    And I enter {pin} in the "Security card PIN" field
    And I press the "Sign in" button
    And The page title contains "Your home"


  Scenario: Active 2FA card user can login via security questions with the Lost or damaged security card link
    # Login via security questions, change insecure answers to questions
    Given I login and click "lost" card using "2FA_CARD_USER" as {username}, {lastDrift}, {question1}, {question2}
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "answer" in the {question1} field
    And I enter "answer" in the {question2} field
    And I press the "Continue" button
    And The page title contains "Change security questions - Reset your account security - MOT testing service"
    And I press the "Continue" button
    And The page title contains "First security question - MOT testing service"
    And I select "What did you want to be when you grew up?" in the field with id "question1"
    And I enter "MOT Tester" in the field with id "answer1"
    And I press the "Continue" button
    And The page title contains "Second security question - MOT testing service"
    And I select "What was your favourite place to visit as a child?" in the field with id "question2"
    And I enter "MOT Test Centre" in the field with id "answer2"
    And I press the "Continue" button
    And The page title contains "Review security question changes - MOT testing service"
    And I press the "Save Changes" button
    And The page title contains "Your security questions have been changed - MOT testing service"
    And I click the "Continue to home" link
    And The page title contains "Your home"

    # First login after changing security questions will go though the normal login journey again, check cookie is set
    And I click the "Sign out" link
    And I login without 2FA as {username}
    And The page title contains "Your security card PIN - MOT testing service"
    And I click the "Lost or damaged security card" link
    And The page title contains "Sign in without your security card"
    When I click the "Continue" link
    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
    And I press the "Continue" button
    Then The page title contains "Sign in successful"
    And The page contains "You have signed in without your security card"
    And I click the "Continue to home" link
    And The page title contains "Your home"
    And I check the "_hasLoggedInWithLostForgottenCardJourney" cookie is set

    # Subsequent logins that day do not ask for 2FA pin, because cookie is set
    And I click the "Sign out" link
    And I login without 2FA as {username}
    And The page title contains "Your security questions"
    And I enter "MOT Tester" in the "What did you want to be when you grew up?" field
    And I enter "MOT Test Centre" in the "What was your favourite place to visit as a child?" field
    And I press the "Continue" button
    And The page title contains "Sign in successful"
    And The page contains "You have signed in without your security card"
    And I click the "Continue to home" link
    And The page title contains "Your home"

    # Logins the following day after cookie expired (simulated by clearing the cookie) require PIN
    And I click the "Sign out" link
    And I delete the "_hasLoggedInWithLostForgottenCardJourney" cookie
    And I login without 2FA as {username}
    And The page title contains "Your security card PIN"
    And I generate 2FA PIN with previous drift {lastDrift} as {pin}
    And I enter {pin} in the "Security card PIN" field
    And I press the "Sign in" button
    And The page title contains "Your home"
