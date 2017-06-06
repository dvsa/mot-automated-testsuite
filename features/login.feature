@tag1
Feature: Login

  #Scenario: Redirect to login page
    ##When I browse to https://dev.motdev.org.uk
    #When I browse to https://acpt.test.mot-testing.service.gov.uk
    #Then The page title contains Sign in

  Scenario: Login with 2FA as a tester
    When I login as username CALL0020 and password Password007
    Then The page title contains Your home
    #todo: autologout in after scenario hook