@regression
Feature: 11 - AEDM and AED

  # Scenario: AEDM assigns AED role
    # AO example: ZOLINAS SERVICE STATION, Authorised Examiner number: AE080126
    # Sign in as a AEDM (2FA active)
    # Select AO via the home page
    # Select 'assign a role'
    # Enter valid username (user without AEDM role) and search
    # Select AED role and confirm
    # Expected results:
    #   - User is directed /authorised-examiner/<AO number> page
    #   - Message displayed: A role notification has been sent to Dori Penney Ames 'AMES5885'.
    #   - Authorised examiner delegate nominated is listed with Pending role status

  # Scenario: AED assigns tester role
    # Sign in as a AED (2FA active)
    # Select VTS via the home page
    # Select 'assign a role'
    # Enter valid username (user without tester role) and search
    # Select tester role and confirm
    # Expected results:
    #   - User is directed /vehicle-testing-station/<VTS> page
    #   - Message displayed: You have assigned a role to Evie Rubin Dykes, DYKE8547. They have been sent a notification.
    #   - User nominated is listed as Tester with Pending role status
    # Checks:
    #   - User nominated cannot be assigned the nominated role for the VTS
    #   - User search for a user who is not a tester

  # Scenario: AEDM can view today's MOT Test Log information for associated VTS
    # Sign in as a AEDM (2FA active)
    # Select Your Profile
    # Select Test Logs
    # Select 'Today' date link (depending on test data)
    # Expected results: Test results displayed within Test Log table
    # Checks:
    #   - AEDM associated VTS's must have conducted and completed  MOT's
    #   - Could be too brittle as needs specific MOT conducted data (unless its created beforehand for this site)

  # Scenario: AEDM can view TQI - VTS List with RAG status
    # Pre-req: Pre req: RAG available / calculated...?
    # Sign in as a AEDM (2FA active)
    # Select AE via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: RAG status per VTS displayed

  # Scenario: AEDM can view TQI for each VTS
    # Pre req: VTS must have MOT's completed for selected month
    # Pre req: National averages calculated....""National stats populate once a month on its own there is a case when automatic job fails to populate them then first user that clicks every single month starts the job""
    # "ign in as a AEDM (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated

  # Scenario: AED can view TQI for each VTS tester
    # Sign in as a AED (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated per tester