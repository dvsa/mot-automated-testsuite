@regression
Feature: 12 - Site Manager and Site Admin

  # Scenario: Site Manager assigns Site Admin role
    # Pre-req: Require a user to nominate without a site admin role
    # Sign in as a Site Manager (2FA active)
    # Select VTS via the home page
    # Select 'assign a role'
    # Enter valid username (user without site admin role) and search
    # Select Site Admin role and confirm
    # Expected results:
    #   - User is directed /vehicle-testing-station/<VTS> page
    #   - Message displayed: You have assigned a role to <full name>, <username>. They have been sent a notification.
    #   - User nominated listed as Site Admin with Pending role status

  # Scenario: Site Admin assigns Tester role
    # Pre-req: Require a user to nominate without a tester role
    # Sign in as a Site Admin (2FA active)
    # Select VTS via the home page
    # Select 'assign a role'
    # Enter valid username (user without tester role) and search
    # Select Tester role and confirm
    # Expected results:
    #   - User is directed /vehicle-testing-station/<VTS> page
    #   - Message displayed: You have assigned a role to <full name>, <username>. They have been sent a notification.
    #   - User nominated listed as Tester with Pending role status

  # Scenario: Site Manager views and aborts an active MOT
    # Pre-req: <Pre-Req: Associated VTS must have a test in progress>
    # Sign in as a Site Manager (2FA active)
    # Select VTS via the home page
    # Select active test
    # Select abort test
    # Select abort reason
    # Select abort test
    # Expected results: You have successfully aborted MOT test 809277497964 with a reason of Vehicle registered in error.

  # Scenario: Site Admin updates VTS details
    # Pre-req: Test can be repeated for 'Change test hours'
    # Sign in as a Site Manager (2FA active)
    # Select VTS via the home page
    # Select 'Change default test settings'
    # Select service and brake options and confirm
    # Expected results: Selected service and brake options displayed

  # Scenario: Site Manager can view TQI statistics for testers associated with VTS
    # Pre-req: VTS must have MOT's completed for selected month
    # Sign in as a Site Manager (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated per tester