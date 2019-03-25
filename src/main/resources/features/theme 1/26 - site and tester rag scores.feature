@regression
  Feature: 26 - Site and Tester RAG scores

    Scenario: VE user searches for a GREEN site
      Given I load "SITE_GREEN" as {siteNumber}, {siteRag}, {siteName}, {organisationName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}
      And I click the first {organisationName} link
      And I click the "Service reports" link
      And I find the {siteName} site in the service reports
      Then I check the row with value {siteNumber} also has value "Green"

    Scenario: VE user searches for an AMBER site
      Given I load "SITE_AMBER" as {siteNumber}, {siteRag}, {siteName}, {organisationName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}
      And I click the first {organisationName} link
      And I click the "Service reports" link
      And I find the {siteName} site in the service reports
      Then I check the row with value {siteNumber} also has value "Amber"

    Scenario: VE user searches for a RED site
      Given I load "SITE_RED" as {siteNumber}, {siteRag}, {siteName}, {organisationName}
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I search for Site information by site number with {siteNumber}
      And I click the first {organisationName} link
      And I click the "Service reports" link
      And I find the {siteName} site in the service reports
      Then I check the row with value {siteNumber} also has value "Red"

      #Currently cannot add Tester RAG scores as all tester usernames with RAG scores have NULL usernames in the anon dataset