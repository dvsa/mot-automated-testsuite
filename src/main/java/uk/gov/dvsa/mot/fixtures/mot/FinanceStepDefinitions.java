package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;



/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class FinanceStepDefinitions implements En {

    /**
     * The driver wrapper to use.
     */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     *
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public FinanceStepDefinitions(WebDriverWrapper driverWrapper, WebDriverWrapper driverWrapper1) {
        this.driverWrapper = driverWrapper1;

        And("^I click the link \"([^\"]+)\" with id \"([^\"]+)\"$", (String text, String id) ->
                driverWrapper.clickElement(id));

        When("^I enter the last 8 characters of \\{([^\\}]+)\\} in the field with id \"([^\"]+)\"$",
                (String dataKey, String id) -> enterLast8CharsIntoFieldWithId(driverWrapper.getData(dataKey), id));

        Then("^I record the finance report URL", () -> recordFinanceReportUrl());

        When("^I set the finance report from date to \\{([^\\}]+)\\} \\{([^\\}]+)\\} \\{([^\\}]+)\\}$",
                (String day, String month, String year) -> setFinanceReportFromDates(driverWrapper.getData(day),
                        driverWrapper.getData(month), driverWrapper.getData(year)));

        When("^I set the finance report to date to \\{([^\\}]+)\\} \\{([^\\}]+)\\} \\{([^\\}]+)\\}$",
                (String day, String month, String year) -> setFinanceReportToDates(driverWrapper.getData(day),
                        driverWrapper.getData(month), driverWrapper.getData(year)));
    }

    /**
     * Enters the last 8 chars of the payment/invoice reference string.
     * @param reference the payment/invoice reference string
     * @param id        the id of the field to enter the value into
     */
    private void enterLast8CharsIntoFieldWithId(String reference, String id) {
        driverWrapper.enterIntoFieldWithId(reference.substring(reference.length() - 8, reference.length()), id);
    }

    /**
     * Sets the current URL as the finance URL so it can be checked later to see if it was generated.
     */
    private void recordFinanceReportUrl() {
        driverWrapper.setData("finance_report", driverWrapper.getCurrentUrl());
    }

    private void setFinanceReportFromDates(String day, String month, String year) {
        driverWrapper.enterTextInFieldWithName("dateFrom[day]", day);
        driverWrapper.enterTextInFieldWithName("dateFrom[month]", month);
        driverWrapper.enterTextInFieldWithName("dateFrom[year]", year);
    }

    private void setFinanceReportToDates(String day, String month, String year) {
        driverWrapper.enterTextInFieldWithName("dateTo[day]", day);
        driverWrapper.enterTextInFieldWithName("dateTo[month]", month);
        driverWrapper.enterTextInFieldWithName("dateTo[year]", year);
    }
}