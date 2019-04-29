package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.Calendar;

import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import javax.inject.Inject;

/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class TesterDoesStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(TesterDoesStepDefinitions.class);

    /** The web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public TesterDoesStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating TesterDoesStepDefinitions...");
        this.driverWrapper = driverWrapper;

        And("^I enter an odometer reading in miles of \\{([^\\}]+)\\} plus (\\d+)$",
                (String dataKey, Integer amount) -> {
                    int startingMileage = Integer.parseInt(driverWrapper.getData(dataKey));
                    enterOdometerReading(OdometerJourney.EnterInMiles, startingMileage + amount);
            });

        And("^I enter an odometer reading in miles of (\\d+)$", (Integer amount) ->
                enterOdometerReading(OdometerJourney.EnterInMiles, amount));

        And("^I enter an odometer reading in kilometres of (\\d+)$", (Integer amount) ->
                enterOdometerReading(OdometerJourney.EnterInKilometres, amount));

        And("^I enter odometer not present$", () ->
                enterOdometerReading(OdometerJourney.NotPresent, 0));

        And("^The MOT status is \"([^\"]+)\"$", (String status) ->
                // unfortunately given no proper formed label etc we have to use the id
                assertTrue("Wrong MOT status", driverWrapper.getElementText("testStatus").contains(status)));

        When("^I search for a vehicle \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String regKey, String vinKey, String siteNameKey) ->
                        searchForVehicle(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                                driverWrapper.getData(siteNameKey)));

        When("^I select an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}"
                 + " with \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String regKey, String vinKey, String siteNameKey,
                 String colour1Key,  String colour2Key,  String issueDateKey) ->
                 motTestSelect(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                    driverWrapper.getData(siteNameKey), driverWrapper.getData(colour1Key),
                    driverWrapper.getData(colour2Key), driverWrapper.getData(issueDateKey),
                        false, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String regKey, String vinKey, String siteNameKey) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                   driverWrapper.getData(siteNameKey),false, Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty()));

        When("^I start a contingency MOT test at site \\{([^\\}]+)\\}$",
                (String siteNameKey) -> startContingencyMotTest(driverWrapper.getData(siteNameKey)));

        When("^I start an MOT retest for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String regKey, String vinKey, String siteNameKey) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                    driverWrapper.getData(siteNameKey),true, Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty()));

        When("^I start an MOT test for DVLA vehicle \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}"
                 + " as class (\\d+)$", (String regKey, String vinKey, String siteNameKey, Integer vehicleClass) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                    driverWrapper.getData(siteNameKey),false, Optional.of(vehicleClass), Optional.empty(),
                        Optional.empty(), Optional.empty()));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}"
                + " with colour changed to \"([^\"]+)\"$",
                (String regKey, String vinKey, String siteNameKey, String colour) ->
                   startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                       driverWrapper.getData(siteNameKey), false, Optional.empty(), Optional.of(colour),
                           Optional.empty(), Optional.empty()));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}"
                + " with engine changed to \"([^\"]+)\" with capacity (\\d+)$",
                (String regKey, String vinKey, String siteNameKey, String fuelType, Integer capacity) ->
                        startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                            driverWrapper.getData(siteNameKey), false, Optional.empty(), Optional.empty(),
                                Optional.of(fuelType), Optional.of(capacity)));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\"$", (String defectType, String category, String defect, String comment) ->
                    addDefectFromBrowse(defectType, category, Optional.empty(), Optional.empty(), defect, comment));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\"$", (String defectType, String category, String subcategory, String defect,
                    String comment) -> addDefectFromBrowse(defectType, category, Optional.of(subcategory),
                    Optional.empty(), defect, comment));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\" with a first use date alert$", (String defectType, String category,
                    String subcategory, String defect, String comment) -> addDefectFromBrowseAlert(defectType, category,
                    Optional.of(subcategory), Optional.empty(), defect, comment));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\", "
                + "\"([^\"]+)\"\\) with comment \"([^\"]+)\"$", (String defectType, String category,
                    String subcategory, String subsubcategory, String defect, String comment) ->
                    addDefectFromBrowse(defectType, category, Optional.of(subcategory), Optional.of(subsubcategory),
                            defect, comment));

        And("^I search for a \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\"$",
                this::addDefectFromSearch);

        And("^I add a manual advisory of \"([^\"]+)\"$", this::addManualAdvisory);

        And("^I edit the \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\" and not dangerous$",
                (String defectType, String defect, String updatedComment) ->
                    editDefect(defectType, defect, updatedComment, false, false));

        And("^I edit the \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\" and is dangerous$",
                (String defectType, String defect, String updatedComment) ->
                    editDefect(defectType, defect, updatedComment, true, false));

        And("^I remove the \"([^\"]+)\" defect of \"([^\"]+)\"$", this::removeDefect);

        And("^I enter decelerometer results of efficiency (\\d+)$", (Integer efficiency) ->
                handleBrakeResults(BrakeTestJourney.addGroupADecelerometerJourney(efficiency)));

        And("^I enter decelerometer results of service brake (\\d+) and parking brake (\\d+)$",
                (Integer serviceBrake, Integer parkingBrake) ->
                    handleBrakeResults(BrakeTestJourney.addGroupBDecelerometerJourney(serviceBrake, parkingBrake)));

        And("^I enter single line decelerometer results of service brake (\\d+) and parking brake (\\d+)$",
                (Integer serviceBrake, Integer parkingBrake) ->
                    handleBrakeResults(BrakeTestJourney.addSingleGroupBDecelerometerJourney(serviceBrake,
                            parkingBrake)));

        And("^I edit decelerometer results of service brake (\\d+) and parking brake (\\d+)$",
                (Integer serviceBrake, Integer parkingBrake) ->
                    handleBrakeResults(BrakeTestJourney.editGroupBDecelerometerJourney(serviceBrake, parkingBrake)));

        And("^I enter decelerometer service brake result of (\\d+) and gradient parking brake result "
                + "of \"([^\"]+)\"$", (Integer serviceBrake, String parkingBrake) ->
                    handleBrakeResults(BrakeTestJourney.addGroupBDecelerometerServiceAndGradientParkingJourney(
                        serviceBrake, parkingBrake)));

        And("^I enter single line decelerometer service brake result of (\\d+) and gradient parking brake "
                + "result of \"([^\"]+)\"$", (Integer serviceBrake, String parkingBrake) ->
                handleBrakeResults(BrakeTestJourney.addSingleGroupBDecelerometerServiceAndGradientParkingJourney(
                        serviceBrake, parkingBrake)));

        And("^I enter group a plate results for weights of (\\d+),(\\d+),(\\d+) as (\\d+),(\\d+),(\\d+),(\\d+)$",
                (Integer frontWeight, Integer rearWeight, Integer riderWeight, Integer control1EffortFront,
                 Integer control1EffortRear, Integer control2EffortFront, Integer control2EffortRear) ->
                handleBrakeResults(BrakeTestJourney.addGroupAPlateJourney(frontWeight, rearWeight, riderWeight,
                    control1EffortFront, control1EffortRear, control2EffortFront, control2EffortRear)));

        And("^I enter class 4 plate results for weights of (\\d+) as service brake (\\d+),(\\d+),(\\d+),"
                        + "(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer parkingBrakeNearside,
                 Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addClass4PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line class 4 plate results for weights of (\\d+) as service brake (\\d+),(\\d+),"
                        + "(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer parkingBrakeNearside,
                 Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingleClass4PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line 3 axle class 4 plate results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingle3AxleClass4PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter single line 3 axle class 7 plate results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingle3AxleClass7PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter 3 axle class 4 plate results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.add3AxleClass4PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter 3 axle class 7 plate results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.add3AxleClass7PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter single line 3 axle class 4 roller results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingle3AxleClass4RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter single line 3 axle class 7 roller results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingle3AxleClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter 3 axle class 4 roller results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.add3AxleClass4RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter 3 axle class 7 roller results for vehicle weight of (\\d+) as service brake "
                        + "(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer serviceBrakeNearsideAxle3,
                 Integer serviceBrakeOffsideAxle3, Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.add3AxleClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3, parkingBrakeNearside,
                        parkingBrakeOffside)));

        And("^I enter class 7 plate results for weights of (\\d+) as service brake (\\d+),(\\d+),(\\d+),"
                        + "(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer parkingBrakeNearside,
                 Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addClass7PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line class 7 plate results for weights of (\\d+) as service brake (\\d+),(\\d+),(\\d+),"
                        + "(\\d+) and parking brake (\\d+),(\\d+)$",
                (Integer weight, Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                 Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2, Integer parkingBrakeNearside,
                 Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingleClass7PlateJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2, serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter class 4 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addClass4RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line class 4 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingleClass4RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line class 7 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingleClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter single line class 5 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addSingleClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I edit class 4 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.editClass4RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter class 5 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I enter class 7 roller results for vehicle weight of (\\d+) as service brake "
                + "(\\d+),(\\d+),(\\d+),(\\d+) and parking brake (\\d+),(\\d+)$", (Integer weight,
                    Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                    Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                    Integer parkingBrakeNearside, Integer parkingBrakeOffside) ->
                handleBrakeResults(BrakeTestJourney.addClass7RollerJourney(weight, serviceBrakeNearsideAxle1,
                        serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,serviceBrakeOffsideAxle2,
                        parkingBrakeNearside, parkingBrakeOffside)));

        And("^I mark the defect \"([^\"]+)\" as repaired$", this::markAsRepaired);

        And("^I mark the manual advisory defect as repaired$", () -> markManualAdvisoryAsRepaired());

        And("^I search for a vehicle with \\{([^\\}]+)\\}$", (String siteNameKey) ->
                searchForVehicle("", "", driverWrapper.getData(siteNameKey)));

        And("^I check the \"Add brake test\" link is hidden$", () ->
                assertTrue(driverWrapper.getLinkClass("Add brake test").contains("hidden")));

        And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \\{([^\\}]+)\\}$",
                (String field, String key) ->
                    assertEquals(driverWrapper.getData(key), driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the registration plate \\{([^\\}]+)\\} is shown within the registration number span text",
                (String registration) ->
                assertTrue(driverWrapper.checkTextInSpan("Registration number", driverWrapper.getData(registration))));

        And("^I check the VIN \\{([^\\}]+)\\} is shown within the VIN span text",
                (String vin) ->
                        assertTrue(driverWrapper.checkTextInSpan("VIN", driverWrapper.getData(vin))));

        And("^I check the brake test summary section has \"([^\"]+)\" of \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the defect section has \"([^\"]+)\" with value \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the defect section contains \"([^\"]+)\" with value \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the test information section of the test summary is \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromHeading("Test information").contains(text)));

        And("^I check the brake results section of the test summary is \"([^\"]+)\"$", (String text) ->
                assertEquals(text, driverWrapper.getTextFromDefinitionList("Brake results overall")));

        And("^I check the fails section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromUnorderedList("Fails").contains(text)));

        And("^I check the dangerous failures section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromDefinitionList("Dangerous failures").contains(text)));

        And("^I check the major failures section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromDefinitionList("Major failures").contains(text)));

        And("^I check the prs section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromDefinitionList("PRS").contains(text)));

        And("^I check the minors section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromDefinitionList("Minors").contains(text)));

        And("^I check the advisory section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromDefinitionList("Advisory text").contains(text)));

        And("^I check the dangerous failures section of the brake test summary of the test summary has \"([^\"]+)\"$",
                (String text) -> assertTrue(driverWrapper.getTextFromDefinitionListOfIndex("Dangerous failures", 1)
                        .contains(text)));

        And("^I check the major failures section of the brake test summary of the test summary has \"([^\"]+)\"$",
                (String text) -> assertTrue(driverWrapper.getTextFromDefinitionListOfIndex("Major failures", 1)
                        .contains(text)));

        And("^I check the dangerous failures section of the brake test summary of the test summary "
                        + "does not have \"([^\"]+)\"$",
                (String text) -> assertFalse(driverWrapper.getTextFromDefinitionListOfIndex("Dangerous failures", 1)
                        .contains(text)));

        And("^I check the major failures section of the brake test summary of the test summary "
                        + "does not have \"([^\"]+)\"$",
                (String text) -> assertFalse(driverWrapper.getTextFromDefinitionListOfIndex("Major failures", 1)
                        .contains(text)));

        And("^I check the fails section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromUnorderedList("Fails").contains(text)));

        And("^I check the dangerous failures section of the test summary does not have \"([^\"]+)\"$",
                (String text) ->
                assertFalse(driverWrapper.getTextFromDefinitionList("Dangerous failures").contains(text)));

        And("^I check the major failures section of the test summary does not have \"([^\"]+)\"$",
                (String text) ->
                assertFalse(driverWrapper.getTextFromDefinitionList("Major failures").contains(text)));

        And("^I check the prs section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromDefinitionList("PRS").contains(text)));

        And("^I check the advisory section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromDefinitionList("Advisory text").contains(text)));

        And("^I enter the current time for the contingency test$", () ->
                selectContingencyTestTime());

        And("^I search for defect \"([^\"]+)\" and open the \"([^\"]+)\" manual link,"
                       + " I expect the \"([^\"]+)\" manual page",
                (String defect, String linkText, String manualTitle) ->
                        openManualLinkFromSearchPage(defect, linkText, manualTitle));

        And("^I record the MOT test number$", () ->
                recordTestNumber());
    }

    /**
     * Starts an MOT test for the specified vehicle. Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     * @param siteName      The name of the site to use (for multi-site testers)
     * @param isRetest      Whether this is a retest
     * @param vehicleClass  The vehicle class to nominate (if any)
     * @param colour        The new primary colour to change to (if any)
     * @param fuelType      The new engine fuel type to change to (if any)
     * @param capacity      The new engine capacity to change to (if any)
     */
    private void startMotTest(String registration, String vin, String siteName, boolean isRetest,
                Optional<Integer> vehicleClass, Optional<String> colour, Optional<String> fuelType,
                    Optional<Integer> capacity) {
        //And I Search for a vehicle
        searchForVehicle(registration, vin, siteName);

        if (isRetest) {

            //And I click the "Select vehicle for retest" link
            driverWrapper.clickLink("Select vehicle for retest");

            //And The page title contains "Confirm vehicle for retest"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle for retest");

            //And I press the "Confirm and start retest" button
            driverWrapper.pressButton("Confirm and start retest");

            //And The page title contains "MOT test started"
            driverWrapper.checkCurrentPageTitle("MOT retest started");

        } else {
            //And I click the "Select vehicle" link
            driverWrapper.clickLink("Select vehicle");

            //And The page title contains "Confirm vehicle and start test"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle and start test");

            if (vehicleClass.isPresent()) {
                //And I click the "Change" link for the MOT test class
                driverWrapper.clickLink("dt", "MOT test class", "../dd/", "Change");

                //And I select the "Class <n>" radio button
                driverWrapper.selectRadio("Class " + vehicleClass.get());

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            if (colour.isPresent()) {
                //And I click the "Change" link for the colour
                driverWrapper.clickLinkContainingHrefValue("change-under-test/colour");

                //And I select <colour> in the "Primary Colour" field
                driverWrapper.selectOptionInField(colour.get(), "Primary colour");

                //And I select "Not stated" in the "Secondary Colour" field
                driverWrapper.selectOptionInField("Not stated", "Secondary colour");

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            if (fuelType.isPresent() || capacity.isPresent()) {
                //And I click the "Change" link for the engine
                driverWrapper.clickLinkContainingHrefValue("change-under-test/engine");

                fuelType.ifPresent(value -> {
                    //And I select <fuel type> in the "Fuel type" field
                    driverWrapper.selectOptionInField(value, "Fuel type");
                });

                capacity.ifPresent(value -> {
                    //And I enter <capacity> in the "Cylinder capacity" field
                    driverWrapper.enterIntoField(String.valueOf(value), "Cylinder capacity");
                });

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            //And I press the "Confirm and start test" button
            driverWrapper.clickButton("Confirm and start test");

            //And The page title contains "MOT test started - MOT testing service"
            driverWrapper.checkCurrentPageTitle("MOT test started - MOT testing service");
        }

        //And I click the "Return to home" link
        driverWrapper.clickLink("Return to home");
    }

    /**
     * *
     * *
     * *
     * search for a vehicle to MOT test. Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     * @param siteName      The name of the site to use (for multi-site testers)
     * @param isRetest      Whether this is a retest
     * @param colour1       The new primary colour to change to (if any)
     * @param colour2       The new primary colour to change to (if any)
     * @param issueDate     The issue date of the last MOT for the vehicle selected
     * @param vehicleClass  The vehicle class to nominate (if any)
     * @param fuelType      The new engine fuel type to change to (if any)
     * @param capacity      The new engine capacity to change to (if any)
     */
    private void motTestSelect(String registration, String vin, String siteName,
                               String colour1, String colour2, String issueDate, boolean isRetest,
                               Optional<Integer> vehicleClass, Optional<String> colour, Optional<String> fuelType,
                               Optional<Integer> capacity) {
        //And the Find a vehicle page contains the following attributes
        assertTrue(driverWrapper.checkTextInSpan("Registration number", registration));
        assertTrue(driverWrapper.checkTextInSpan("VIN", vin));
        assertTrue(driverWrapper.checkTextInSpan("Colour", colour1));
        assertTrue(driverWrapper.getElementText("results-table").contains(issueDate));

        if (isRetest) {

            //And I click the "Select vehicle for retest" link
            driverWrapper.clickLink("Select vehicle for retest");

            //And The page title contains "Confirm vehicle for retest"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle for retest");

            assertTrue(driverWrapper.containsMessage("MOT testing"));
            assertTrue(driverWrapper.containsMessage("Confirm vehicle and start retest"));

            //And I check the Confirm vehicle and start retest page
            //Add in a mew method

            //And I press the "Confirm and start retest" button
            driverWrapper.pressButton("Confirm and start retest");

            //And The page title contains "MOT test started"
            driverWrapper.checkCurrentPageTitle("MOT retest started");

        } else {
            //And I click the "Select vehicle" link
            driverWrapper.clickLink("Select vehicle");

            //And The page title contains "Confirm vehicle and start test"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle and start test");

            assertTrue(driverWrapper.containsMessage("MOT testing"));
            assertTrue(driverWrapper.containsMessage("Confirm vehicle and start test"));

            if (vehicleClass.isPresent()) {
                //And I click the "Change" link for the MOT test class
                driverWrapper.clickLink("dt", "MOT test class", "../dd/", "Change");

                //And I select the "Class <n>" radio button
                driverWrapper.selectRadio("Class " + vehicleClass.get());

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            //And I check the Confirm vehicle and start test page
            //Add in a mew method

            //And I press the "Confirm and start test" button
            driverWrapper.clickButton("Confirm and start test");

            //And The page title contains "MOT test started - MOT testing service"
            driverWrapper.checkCurrentPageTitle("MOT test started - MOT testing service");
        }

        //And I click the "Return to home" link
        driverWrapper.clickLink("Return to home");
    }

    /**
     * Check the Confirm vehicle and start (re)test pages
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     * @param siteName      The name of the site to use (for multi-site testers)
     * @param colour1       The new primary colour to change to (if any)
     * @param colour2       The new primary colour to change to (if any)
     * @param issueDate     The issue date of the last MOT for the vehicle selected
     * @param vehicleClass  The vehicle class to nominate (if any)
     * @param fuelType      The new engine fuel type to change to (if any)
     * @param capacity      The new engine capacity to change to (if any)
     */
    private void motCheckConfirm(String registration, String vin, String siteName,
                               String colour1, String colour2, String issueDate,
                               Optional<Integer> vehicleClass, Optional<String> colour, Optional<String> fuelType,
                               Optional<Integer> capacity) {

        //And the vehicle information section contains the following attributes
        assertTrue(driverWrapper.checkTextInSpan("Vehicle", "Make and Model from SQL"));
        assertTrue(driverWrapper.checkTextInSpan("Registration number", registration));
        assertTrue(driverWrapper.checkTextInSpan("VIN", vin));
        assertTrue(driverWrapper.checkTextInSpan("Colour", colour1));

        //And I check both the primary and secondary colour
        String colourElements = driverWrapper.getElementColour(colour1, colour2);
        if (colour2.equals("Not Stated")) {
            assertTrue(colourElements.equals(colour1));
        } else {
            assertTrue(colourElements.equals(colour1 + ", " + colour2));
        }

        //And the vehicle specification section contains the following attributes
        //Make and model	        SUZUKI, SPLASH
        assertEquals("Make and Model from SQL", driverWrapper.getTextFromDefinitionList("Make and model"));
        //Engine	                Petrol, 996
        assertEquals("Fuel Type and Engine size from SQL", driverWrapper.getTextFromDefinitionList("Engine"));
        //Colour	                Black
        assertEquals(colour1, driverWrapper.getTextFromDefinitionList("Colour"));
        //Brake test weight	        1075 kg
        assertEquals("weight from SQL" + "kg", driverWrapper.getTextFromDefinitionList("Brake test weight"));

        //And the vehicle registration section contains the following attributes
        //Registration mark	        GT93EJL
        assertEquals(registration, driverWrapper.getTextFromDefinitionList("Registration mark"));
        //VIN	                    LJETGJAAAAA011133
        assertEquals(vin, driverWrapper.getTextFromDefinitionList("VIN"));
        //Country of registration	GB, UK, ENG, CYM, SCO (UK) - Great Britain
        assertEquals("Country of registration from SQL", driverWrapper.getTextFromDefinitionList("Country of registration"));
        //MOT test class	        4
        assertEquals("MOT test class from SQL", driverWrapper.getTextFromDefinitionList("MOT test class"));
        //Vehicle category	        M1
        assertEquals("Vehicle category from DVLA SQL", driverWrapper.getTextFromDefinitionList("Vehicle category"));
        //Date of first use	        26 July 2013
        assertEquals(issueDate, driverWrapper.getTextFromDefinitionList("Date of first use"));
        //MOT expiration date       26 August 2018
        assertEquals("MOT expiration date from SQL", driverWrapper.getTextFromDefinitionList("MOT expiration date"));

    }

    /**
     * Starting a contingency for a specific site where the user has multiple sites.
     * @param siteName  the site to perform the contingency test against
     */
    private void startContingencyMotTest(String siteName) {
        // When I click the "Record contingency test" link
        driverWrapper.clickLink("Record contingency test");

        // if page requires a site to be selected
        if (driverWrapper.containsMessage("Location where the test was performed")) {
            // select the site
            driverWrapper.selectRadio(siteName);
        }
    }

    /**
     * Selecting the current time for a contingency test.
     */
    private void selectContingencyTestTime() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London") ;
        Calendar currentDate = Calendar.getInstance(tz, Locale.UK);
        String hour  = String.valueOf(currentDate.get(Calendar.HOUR));
        // fix to output the correct 12pm time
        if (hour.equals("0")) {
            hour = "12";
        }
        String minute = String.valueOf(currentDate.get(Calendar.MINUTE));
        if (minute.equals("0")) {
            minute = "00";
        }
        String ampm = currentDate.get(Calendar.AM_PM) == 0 ? "am" : "pm";

        driverWrapper.setData("Contingency time", hour + ':' + minute + ' ' + ampm);

        // Enter the Hour
        driverWrapper.enterIntoFieldWithId(hour, "contingency_time-hour");
        // Enter the Minute
        driverWrapper.enterIntoFieldWithId(minute, "contingency_time-minutes");
        // Enter either am or pm
        driverWrapper.selectOptionInFieldById(ampm, "contingency_time-ampm");
    }

    /**
     * Recording the MOT test number for the current test from the MOT test summary page.
     */
    private void recordTestNumber() {
        //Get the current MOT test number value
        String motTestNumber = driverWrapper.getElementText("motTestNumber");

        //Add the MOT test number to check later
        driverWrapper.setData("currentMotTestNumber", String.valueOf(motTestNumber));
    }

    /**
     * Search for a vehicle from the trade user search.
     * @param registration  The registration of the vehicle to find
     * @param vin           The vin number of the vehicle to find
     * @param siteName      The site name to use (for multi-site testers)
     */
    private void searchForVehicle(String registration, String vin, String siteName) {
        // When I click the "Start MOT test" link
        driverWrapper.clickLink("Start MOT test");

        // if page title Select your current site
        if (driverWrapper.getCurrentPageTitle().contains("Select your current site")) {
            // select the first site
            driverWrapper.clickRadioButtonByText(siteName);
            // press "Confirm" button
            driverWrapper.clickButton("Confirm");
        }

        // And The page title contains "Find a vehicle"
        driverWrapper.checkCurrentPageTitle("Find a vehicle");
        //And I enter <registration1> in the "Registration mark" field
        driverWrapper.enterIntoField(registration, "Registration mark");
        //And I enter <vin1> in the "VIN" field
        driverWrapper.enterIntoField(vin, "VIN");
        //And I press the "Search" button
        driverWrapper.pressButton("Search");

        //And The page title contains "Find a vehicle"
        driverWrapper.checkCurrentPageTitle("Find a vehicle");

        if (registration != null && registration.trim().length() > 0) {
            //Check added because this is an area of previous defects - e.g. DVSA and DVLA vehicle ids being mixed up
            //And I check that the search results has Registration that was searched for
            assertTrue(driverWrapper.checkTextInSpan("Registration number", registration));
        }

        if (vin != null && vin.trim().length() > 0) {
            //And I check that the search results has VIN that was searched for
            assertTrue(driverWrapper.checkTextInSpan("VIN", vin));
        }
    }

    /** Encapsulates the user journey for the Odometer screen. */
    private enum OdometerJourney {
        EnterInMiles, EnterInKilometres, NotPresent
    }

    /**
     * Enter an odometer reading of the specified amount/type. Refactored repeated cucumber steps, the original steps
     * are detailed below.
     * @param journey   The user journey being taken
     * @param amount    The new odometer reading amount
     */
    private void enterOdometerReading(OdometerJourney journey, int amount) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        // And I click the "Add reading" link
        driverWrapper.clickLink("Add reading");

        // And The page title contains "Odometer reading"
        driverWrapper.checkCurrentPageTitle("Odometer reading");

        switch (journey) {
            case EnterInMiles:
                // And I enter <n> in the odometer field
                driverWrapper.enterIntoFieldWithId(String.valueOf(amount), "odometer");

                // And I select "Miles" in the "Measurement units" field
                driverWrapper.selectOptionInField("Miles", "Measurement units");
                break;

            case EnterInKilometres:
                // And I enter <n> in the odometer field
                driverWrapper.enterIntoFieldWithId(String.valueOf(amount), "odometer");

                // And I select "Kilometres" in the "Measurement units" field
                driverWrapper.selectOptionInField("Kilometres", "Measurement units");
                break;

            case NotPresent:
                // And I select the "Odometer is not present" radio button
                driverWrapper.selectRadio("Odometer is not present");
                break;

            default:
                String message = "Unknown Odometer journey: " + journey;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

        // And I press the "Update reading" button
        driverWrapper.pressButton("Update reading");
    }

    /**
     * Inner class used to encapsulate the journey type and associated data for all the various brake test scenarios.
     * <p>Each static factory method has a valid combination of data fields.</p>
     */
    private static class BrakeTestJourney {

        /** Encapsulates the user journey for the Brake test screens. */
        private enum BrakeTestJourneyType {
            AddSingleDecelerometerResult, AddServiceAndParkingDecelerometerResult,
            AddDecelerometerServiceAndGradientParkingResult, EditServiceAndParkingDecelerometerResult,
            AddSinglePlateResult, AddClass4ServiceAndParkingRollerResult, EditClass4ServiceAndParkingRollerResult,
            AddClass7ServiceAndParkingRollerResult, AddClass4ServiceAndParkingPlateResult,
            AddClass7ServiceAndParkingPlateResult, AddSingleClass4ServiceAndParkingRollerResult,
            AddSingleClass4ServiceAndParkingPlateResult, AddSingleServiceAndParkingDecelerometerResult,
            AddSingleDecelerometerAndGradientParkingResult, AddSingleClass7ServiceAndParkingRollerResult,
            AddSingleClass7ServiceAndParkingPlateResult, AddSingle3AxleClass4ServiceAndParkingPlateResult,
            AddSingle3AxleClass7ServiceAndParkingPlateResult, AddSingle3AxleClass4ServiceAndParkingRollerResult,
            AddSingle3AxleClass7ServiceAndParkingRollerResult, Add3AxleClass4ServiceAndParkingPlateResult,
            Add3AxleClass7ServiceAndParkingPlateResult, Add3AxleClass4ServiceAndParkingRollerResult,
            Add3AxleClass7ServiceAndParkingRollerResult,
        }

        /** The brake test journey type. */
        private BrakeTestJourneyType journeyType;

        /** Efficiency result for group A brake test result. */
        private int brakeTestEfficiency;

        /** Efficiency result for group B service brake test result. */
        private int serviceBrakeTestEfficiency;

        /** Efficiency result for group B parking brake test result. */
        private int parkingBrakeTestEfficiency;

        /** Gradient test result for group B parking brake test result. */
        private boolean parkingBrakeGradientTestResult;

        /** Plate brake results for group A. */
        private int frontWeight;
        private int rearWeight;
        private int riderWeight;
        private int control1EffortFront;
        private int control1EffortRear;
        private int control2EffortFront;
        private int control2EffortRear;

        /** Roller/Plate brake results for group B. */
        private int weight;
        private int serviceBrakeNearsideAxle1;
        private int serviceBrakeOffsideAxle1;
        private int serviceBrakeNearsideAxle2;
        private int serviceBrakeOffsideAxle2;
        private int serviceBrakeNearsideAxle3;
        private int serviceBrakeOffsideAxle3;
        private int parkingBrakeNearside;
        private int parkingBrakeOffside;

        /**
         * Add group A brake test result - using decelerometer.
         * @param brakeTestEfficiency The efficiency
         * @return The journey
         */
        static BrakeTestJourney addGroupADecelerometerJourney(int brakeTestEfficiency) {
            BrakeTestJourney journey = new BrakeTestJourney(BrakeTestJourneyType.AddSingleDecelerometerResult);
            journey.brakeTestEfficiency = brakeTestEfficiency;
            return journey;
        }

        /**
         * Add group B brake test result - both service and parking brake using decelerometer.
         * @param serviceBrakeTestEfficiency Service brake efficiency
         * @param parkingBrakeTestEfficiency Parking brake efficiency
         * @return The journey
         */
        static BrakeTestJourney addGroupBDecelerometerJourney(int serviceBrakeTestEfficiency,
                int parkingBrakeTestEfficiency) {
            BrakeTestJourney journey =
                    new BrakeTestJourney(BrakeTestJourneyType.AddServiceAndParkingDecelerometerResult);
            journey.serviceBrakeTestEfficiency = serviceBrakeTestEfficiency;
            journey.parkingBrakeTestEfficiency = parkingBrakeTestEfficiency;
            return journey;
        }

        /**
         * Add group B brake test result - service brake using decelerometer, parking brake using gradient.
         * @param serviceBrakeTestEfficiency        Service brake efficiency
         * @param parkingBrakeGradientTestResult    Parking brake gradient result ("Pass<" or "Fail")
         * @return The journey
         */
        static BrakeTestJourney addGroupBDecelerometerServiceAndGradientParkingJourney(
                int serviceBrakeTestEfficiency, String parkingBrakeGradientTestResult) {
            BrakeTestJourney journey =
                    new BrakeTestJourney(BrakeTestJourneyType.AddDecelerometerServiceAndGradientParkingResult);
            journey.serviceBrakeTestEfficiency = serviceBrakeTestEfficiency;

            if ("Pass".equals(parkingBrakeGradientTestResult)) {
                journey.parkingBrakeGradientTestResult = true;
            } else if ("Fail".equals(parkingBrakeGradientTestResult)) {
                journey.parkingBrakeGradientTestResult = false;
            } else {
                String message = "Unknown Gradient parking brake result: " + parkingBrakeGradientTestResult;
                logger.error(message);
                throw new IllegalArgumentException(message);
            }

            return journey;
        }

        /**
         * Add group B brake test result - service brake using decelerometer, parking brake using gradient.
         * @param serviceBrakeTestEfficiency        Service brake efficiency
         * @param parkingBrakeGradientTestResult    Parking brake gradient result ("Pass<" or "Fail")
         * @return The journey
         */
        static BrakeTestJourney addSingleGroupBDecelerometerServiceAndGradientParkingJourney(
                int serviceBrakeTestEfficiency, String parkingBrakeGradientTestResult) {
            BrakeTestJourney journey =
                    new BrakeTestJourney(BrakeTestJourneyType.AddSingleDecelerometerAndGradientParkingResult);
            journey.serviceBrakeTestEfficiency = serviceBrakeTestEfficiency;

            if ("Pass".equals(parkingBrakeGradientTestResult)) {
                journey.parkingBrakeGradientTestResult = true;
            } else if ("Fail".equals(parkingBrakeGradientTestResult)) {
                journey.parkingBrakeGradientTestResult = false;
            } else {
                String message = "Unknown Gradient parking brake result: " + parkingBrakeGradientTestResult;
                logger.error(message);
                throw new IllegalArgumentException(message);
            }

            return journey;
        }

        /**
         * Edit group B brake test result - both service and parking brake using decelerometer.
         * @param serviceBrakeTestEfficiency Service brake efficiency
         * @param parkingBrakeTestEfficiency Parking brake efficiency
         * @return The journey
         */
        static BrakeTestJourney editGroupBDecelerometerJourney(int serviceBrakeTestEfficiency,
                int parkingBrakeTestEfficiency) {
            BrakeTestJourney journey =
                    new BrakeTestJourney(BrakeTestJourneyType.EditServiceAndParkingDecelerometerResult);
            journey.serviceBrakeTestEfficiency = serviceBrakeTestEfficiency;
            journey.parkingBrakeTestEfficiency = parkingBrakeTestEfficiency;
            return journey;
        }

        /**
         * Edit group B brake test result - both service and parking brake using decelerometer.
         * @param serviceBrakeTestEfficiency Service brake efficiency
         * @param parkingBrakeTestEfficiency Parking brake efficiency
         * @return The journey
         */
        static BrakeTestJourney addSingleGroupBDecelerometerJourney(int serviceBrakeTestEfficiency,
                int parkingBrakeTestEfficiency) {
            BrakeTestJourney journey =
                    new BrakeTestJourney(BrakeTestJourneyType.AddSingleServiceAndParkingDecelerometerResult);
            journey.serviceBrakeTestEfficiency = serviceBrakeTestEfficiency;
            journey.parkingBrakeTestEfficiency = parkingBrakeTestEfficiency;
            return journey;
        }

        /**
         * Add group A brake test result - using plate.
         * @param frontWeight       Vehicle front weight
         * @param rearWeight        Vehicle rear weight
         * @param riderWeight       Rider weight
         * @param control1EffortFront   Plate test result - control 1 - front
         * @param control1EffortRear    Plate test result - control 1 - rear
         * @param control2EffortFront   Plate test result - control 2 - front
         * @param control2EffortRear    Plate test result - control 2 - rear
         * @return The journey
         */
        static BrakeTestJourney addGroupAPlateJourney(int frontWeight, int rearWeight, int riderWeight,
                int control1EffortFront, int control1EffortRear, int control2EffortFront, int control2EffortRear) {
            BrakeTestJourney journey = new BrakeTestJourney(BrakeTestJourneyType.AddSinglePlateResult);
            journey.frontWeight = frontWeight;
            journey.rearWeight = rearWeight;
            journey.riderWeight = riderWeight;
            journey.control1EffortFront = control1EffortFront;
            journey.control1EffortRear = control1EffortRear;
            journey.control2EffortFront = control2EffortFront;
            journey.control2EffortRear = control2EffortRear;
            return journey;
        }

        /**
         * Add Class 4 brake test result - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addClass4PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                  int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                  int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddClass4ServiceAndParkingPlateResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 4 single line brake test result - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingleClass4PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddSingleClass4ServiceAndParkingPlateResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 4 single line brake test result on 3 axles - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingle3AxleClass4PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                 int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                 int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                 int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.AddSingle3AxleClass4ServiceAndParkingPlateResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 7 & 5 single line brake test result on 3 axles - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingle3AxleClass7PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                 int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                 int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                 int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingPlateResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 4 brake test result on 3 axles - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney add3AxleClass4PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                   int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                   int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                   int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.Add3AxleClass4ServiceAndParkingPlateResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 7 brake test result on 3 axles - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney add3AxleClass7PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                   int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                   int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                   int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.Add3AxleClass7ServiceAndParkingPlateResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 4 single line brake test result on 3 axles - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingle3AxleClass4RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                  int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                  int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                  int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.AddSingle3AxleClass4ServiceAndParkingRollerResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 7 & 5 single line brake test result on 3 axles - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingle3AxleClass7RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                  int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                  int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                  int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingRollerResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 4 brake test result on 3 axles - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney add3AxleClass4RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.Add3AxleClass4ServiceAndParkingRollerResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 7 & 5 brake test result on 3 axles - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney add3AxleClass7RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                int serviceBrakeNearsideAxle3, int serviceBrakeOffsideAxle3,int parkingBrakeNearside,
                int parkingBrakeOffside) {
            return addPlateOrRollerJourney3axle(BrakeTestJourneyType.Add3AxleClass7ServiceAndParkingRollerResult,
                    weight, serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, serviceBrakeNearsideAxle3, serviceBrakeOffsideAxle3,
                    parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Class 7 & 5 brake test result - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addClass7PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                  int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                  int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddClass7ServiceAndParkingPlateResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add Single Line Class 7 & 5 brake test result - both service and parking brake using plate.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingleClass7PlateJourney(int weight, int serviceBrakeNearsideAxle1,
                    int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                    int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddSingleClass7ServiceAndParkingPlateResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add class 4 brake test result - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addClass4RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                   int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                   int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddClass4ServiceAndParkingRollerResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add single line class 4 brake test result - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingleClass4RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                     int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                     int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddSingleClass4ServiceAndParkingRollerResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add single line class 7 brake test result - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addSingleClass7RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                     int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                     int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddSingleClass7ServiceAndParkingRollerResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Edit class 4 brake test result - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney editClass4RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                    int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                    int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.EditClass4ServiceAndParkingRollerResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add class 7 & 5 brake test result - both service and parking brake using roller.
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        static BrakeTestJourney addClass7RollerJourney(int weight, int serviceBrakeNearsideAxle1,
                   int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2, int serviceBrakeOffsideAxle2,
                   int parkingBrakeNearside, int parkingBrakeOffside) {
            return addPlateOrRollerJourney(BrakeTestJourneyType.AddClass7ServiceAndParkingRollerResult, weight,
                    serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                    serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);
        }

        /**
         * Add brake test result - both service and parking brake using roller or plate.
         * @param journeyType                   The journey type
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        private static BrakeTestJourney addPlateOrRollerJourney(BrakeTestJourneyType journeyType, int weight,
                    int serviceBrakeNearsideAxle1, int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2,
                    int serviceBrakeOffsideAxle2, int parkingBrakeNearside, int parkingBrakeOffside) {
            BrakeTestJourney journey = new BrakeTestJourney(journeyType);
            journey.weight = weight;
            journey.serviceBrakeNearsideAxle1 = serviceBrakeNearsideAxle1;
            journey.serviceBrakeOffsideAxle1 = serviceBrakeOffsideAxle1;
            journey.serviceBrakeNearsideAxle2 = serviceBrakeNearsideAxle2;
            journey.serviceBrakeOffsideAxle2 = serviceBrakeOffsideAxle2;
            journey.parkingBrakeNearside = parkingBrakeNearside;
            journey.parkingBrakeOffside = parkingBrakeOffside;
            return journey;
        }

        /**
         * Add brake test result - both service and parking brake using roller or plate on 3 axles.
         * @param journeyType                   The journey type
         * @param weight                        The vehicle weight
         * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
         * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
         * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
         * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
         * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
         * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
         * @param parkingBrakeNearside          Weight applied by parking brake to nearside
         * @param parkingBrakeOffside           Weight applied by parking brake to offside
         * @return The journey
         */
        private static BrakeTestJourney addPlateOrRollerJourney3axle(BrakeTestJourneyType journeyType, int weight,
                 int serviceBrakeNearsideAxle1, int serviceBrakeOffsideAxle1, int serviceBrakeNearsideAxle2,
                 int serviceBrakeOffsideAxle2, int serviceBrakeNearsideAxle3,
                 int serviceBrakeOffsideAxle3, int parkingBrakeNearside, int parkingBrakeOffside) {
            BrakeTestJourney journey = new BrakeTestJourney(journeyType);
            journey.weight = weight;
            journey.serviceBrakeNearsideAxle1 = serviceBrakeNearsideAxle1;
            journey.serviceBrakeOffsideAxle1 = serviceBrakeOffsideAxle1;
            journey.serviceBrakeNearsideAxle2 = serviceBrakeNearsideAxle2;
            journey.serviceBrakeOffsideAxle2 = serviceBrakeOffsideAxle2;
            journey.serviceBrakeNearsideAxle3 = serviceBrakeNearsideAxle3;
            journey.serviceBrakeOffsideAxle3 = serviceBrakeOffsideAxle3;
            journey.parkingBrakeNearside = parkingBrakeNearside;
            journey.parkingBrakeOffside = parkingBrakeOffside;
            return journey;
        }

        /**
         * Creates a new instance.
         * @param journeyType   The journey type
         */
        private BrakeTestJourney(BrakeTestJourneyType journeyType) {
            this.journeyType = journeyType;
        }
    }

    /**
     * Handles various types of brake tests.
     * @param journey   The user journey being taken
     */
    private void handleBrakeResults(BrakeTestJourney journey) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        switch (journey.journeyType) {
            case EditServiceAndParkingDecelerometerResult:
            case EditClass4ServiceAndParkingRollerResult:
                // And I click the "Edit brake test" link
                driverWrapper.clickLink("Edit brake test");
                break;

            default:
                // And I click the "Add brake test" link
                driverWrapper.clickLink("Add brake test");
                break;
        }

        // And The page title contains "Brake test configuration"
        driverWrapper.checkCurrentPageTitle("Brake test configuration");

        switch (journey.journeyType) {
            case AddSingleDecelerometerResult:
                // And I select "Decelerometer" in the "Brake test type" field
                driverWrapper.selectOptionInField("Decelerometer", "Brake test type");
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And The page title contains "Add brake test results"
                driverWrapper.checkCurrentPageTitle("Add brake test results");
                // And I enter <n> in the "Control one" field
                driverWrapper.enterIntoField(journey.brakeTestEfficiency, "Control one");
                // And I enter <n> in the "Control two" field
                driverWrapper.enterIntoField(journey.brakeTestEfficiency, "Control two");
                break;

            case AddSinglePlateResult:
                // And I select "Plate" in the "Brake test type" field
                driverWrapper.selectOptionInField("Plate", "Brake test type");

                // (note - all these vehicle weight textboxes have invalid labels, have to use id)

                // And I enter <n> in the field with id "vehicleWeightFront"
                driverWrapper.enterIntoFieldWithId(journey.frontWeight, "vehicleWeightFront");
                // And I enter <n> in the field with id "vehicleWeightRear"
                driverWrapper.enterIntoFieldWithId(journey.rearWeight, "vehicleWeightRear");
                // And I enter <n> in the field with id "riderWeight"
                driverWrapper.enterIntoFieldWithId(journey.riderWeight, "riderWeight");
                // And I click the "No" radio button in fieldset "Is there a sidecar attached?"
                driverWrapper.selectRadioInFieldset("Is there a sidecar attached?", "No");
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And The page title contains "Add brake test results"
                driverWrapper.checkCurrentPageTitle("Add brake test results");
                // And I enter <n> in the field with id "control1EffortFront"
                driverWrapper.enterIntoFieldWithId(journey.control1EffortFront, "control1EffortFront");
                // And I enter <n> in the field with id "control1EffortRear"
                driverWrapper.enterIntoFieldWithId(journey.control1EffortRear, "control1EffortRear");
                // And I enter <n> in the field with id "control2EffortFront"
                driverWrapper.enterIntoFieldWithId(journey.control2EffortFront, "control2EffortFront");
                // And I enter <n> in the field with id "control2EffortRear"
                driverWrapper.enterIntoFieldWithId(journey.control2EffortRear, "control2EffortRear");
                break;

            case AddClass4ServiceAndParkingPlateResult:
            case AddSingleClass4ServiceAndParkingPlateResult:
            case AddClass7ServiceAndParkingPlateResult:
            case AddSingleClass7ServiceAndParkingPlateResult:
                // And I select "Plate" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Plate", "Service brake test type");

                // And I select "Plate" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Plate", "Parking brake test type");

                // If the vehicle is class 7 or 5 the name of the radio button changes
                breakTestWeightType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddClass7ServiceAndParkingPlateResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass7ServiceAndParkingPlateResult);

                // And I enter <n> in the "Vehicle Weight in kilograms" field
                driverWrapper.enterIntoField(journey.weight, "Vehicle Weight in kilograms");

                // Selects the correct break line type
                breakLineType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass4ServiceAndParkingPlateResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass7ServiceAndParkingPlateResult);

                // And I select "2 axles" in the "Number of axles" field
                driverWrapper.selectOptionInField("2 axles", "Number of axles");
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And I enter all the details for a 2 axle vehicle
                enterBrakeValues2Axles(journey.serviceBrakeNearsideAxle1, journey.serviceBrakeOffsideAxle1,
                        journey.serviceBrakeNearsideAxle2, journey.serviceBrakeOffsideAxle2,
                        journey.parkingBrakeNearside, journey.parkingBrakeOffside);

                // And I set the first axle as the steered axle
                driverWrapper.selectCheckboxById("serviceBrake1SteeredAxle1");
                break;

            case AddSingle3AxleClass4ServiceAndParkingPlateResult:
            case AddSingle3AxleClass7ServiceAndParkingPlateResult:
            case Add3AxleClass4ServiceAndParkingPlateResult:
            case Add3AxleClass7ServiceAndParkingPlateResult:
                // And I select "Plate" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Plate", "Service brake test type");

                // And I select "Plate" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Plate", "Parking brake test type");

                // If the vehicle is class 7 or 5 the name of the radio button changes
                breakTestWeightType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.Add3AxleClass7ServiceAndParkingPlateResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingPlateResult);

                // And I enter <n> in the "Vehicle Weight in kilograms" field
                driverWrapper.enterIntoField(journey.weight, "Vehicle Weight in kilograms");

                // Selects the correct break line type, defaulted to dual.
                breakLineType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass4ServiceAndParkingPlateResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingPlateResult);

                // And I select "3 axles" in the "Number of axles" field
                driverWrapper.selectOptionInField("3 axles", "Number of axles");

                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And I enter all the details for a 3 axle vehicle
                enterBrakeValues3Axles(journey.serviceBrakeNearsideAxle1, journey.serviceBrakeOffsideAxle1,
                        journey.serviceBrakeNearsideAxle2, journey.serviceBrakeOffsideAxle2,
                        journey.serviceBrakeNearsideAxle3, journey.serviceBrakeOffsideAxle3,
                        journey.parkingBrakeNearside, journey.parkingBrakeOffside);

                // And I set the first axle as the steered axle
                driverWrapper.selectCheckboxById("serviceBrake1SteeredAxle1");
                break;

            case AddServiceAndParkingDecelerometerResult:
            case AddSingleServiceAndParkingDecelerometerResult:
            case EditServiceAndParkingDecelerometerResult:
                // And I select "Decelerometer" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Decelerometer", "Service brake test type");
                // And I select "Decelerometer" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Decelerometer", "Parking brake test type");

                breakLineType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleServiceAndParkingDecelerometerResult);
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And The page title contains "Add brake test results"
                driverWrapper.checkCurrentPageTitle("Add brake test results");
                // And I enter <n> in the "Service brake" field
                driverWrapper.enterIntoField(journey.serviceBrakeTestEfficiency, "Service brake");
                // And I enter <n> in the "Parking brake" field
                driverWrapper.enterIntoField(journey.parkingBrakeTestEfficiency, "Parking brake");
                break;

            case AddDecelerometerServiceAndGradientParkingResult:
            case AddSingleDecelerometerAndGradientParkingResult:
                // And I select "Decelerometer" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Decelerometer", "Service brake test type");
                // And I select "Gradient" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Gradient", "Parking brake test type");

                breakLineType(journey.journeyType
                        ==  BrakeTestJourney.BrakeTestJourneyType.AddSingleDecelerometerAndGradientParkingResult);
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And The page title contains "Add brake test results"
                driverWrapper.checkCurrentPageTitle("Add brake test results");
                // And I enter <n> in the "Service brake" field
                driverWrapper.enterIntoField(journey.serviceBrakeTestEfficiency, "Service brake");

                if (journey.parkingBrakeGradientTestResult) {
                    // And I select the "Pass" parking brake radio button
                    driverWrapper.selectRadio("Pass");
                } else {
                    // And I select the "Fail" parking brake radio button
                    driverWrapper.selectRadio("Fail");
                }
                break;

            case AddClass4ServiceAndParkingRollerResult:
            case AddSingleClass4ServiceAndParkingRollerResult:
            case AddSingleClass7ServiceAndParkingRollerResult:
            case EditClass4ServiceAndParkingRollerResult:
            case AddClass7ServiceAndParkingRollerResult:
                // And I select "Roller" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Roller", "Service brake test type");
                // And I select "Roller" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Roller", "Parking brake test type");

                // If the vehicle is class 7 or 5  the name of the radio button changes
                breakTestWeightType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddClass7ServiceAndParkingRollerResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass7ServiceAndParkingRollerResult);

                // And I enter <n> in the "Vehicle Weight in kilograms" field
                driverWrapper.enterIntoField(journey.weight, "Vehicle Weight in kilograms");

                // Selects the correct break line type, defaulted to dual.
                breakLineType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass4ServiceAndParkingRollerResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingleClass7ServiceAndParkingRollerResult);

                // And I select "2 axles" in the "Number of axles" field
                driverWrapper.selectOptionInField("2 axles", "Number of axles");
                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And I enter all the details for a 2 axle vehicle
                enterBrakeValues2Axles(journey.serviceBrakeNearsideAxle1, journey.serviceBrakeOffsideAxle1,
                        journey.serviceBrakeNearsideAxle2, journey.serviceBrakeOffsideAxle2,
                        journey.parkingBrakeNearside, journey.parkingBrakeOffside);

                // And I set the first axle as the steered axle
                driverWrapper.selectCheckboxById("serviceBrake1SteeredAxle1");
                break;

            case AddSingle3AxleClass4ServiceAndParkingRollerResult:
            case AddSingle3AxleClass7ServiceAndParkingRollerResult:
            case Add3AxleClass4ServiceAndParkingRollerResult:
            case Add3AxleClass7ServiceAndParkingRollerResult:
                // And I select "Roller" in the "Service brake test type" field
                driverWrapper.selectOptionInField("Roller", "Service brake test type");
                // And I select "Roller" in the "Parking brake test type" field
                driverWrapper.selectOptionInField("Roller", "Parking brake test type");

                // If the vehicle is class 7 or 5 the name of the radio button changes
                breakTestWeightType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.Add3AxleClass7ServiceAndParkingRollerResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingRollerResult);

                // And I enter <n> in the "Vehicle Weight in kilograms" field
                driverWrapper.enterIntoField(journey.weight, "Vehicle Weight in kilograms");

                // Selects the correct break line type, defaulted to dual.
                breakLineType(journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass4ServiceAndParkingRollerResult
                        || journey.journeyType
                        == BrakeTestJourney.BrakeTestJourneyType.AddSingle3AxleClass7ServiceAndParkingRollerResult);

                // And I select "3 axles" in the "Number of axles" field
                driverWrapper.selectOptionInField("3 axles", "Number of axles");

                // And I press the "Next" button
                driverWrapper.pressButton("Next");

                // And I enter all the details for a 3 axle vehicle
                enterBrakeValues3Axles(journey.serviceBrakeNearsideAxle1, journey.serviceBrakeOffsideAxle1,
                        journey.serviceBrakeNearsideAxle2, journey.serviceBrakeOffsideAxle2,
                        journey.serviceBrakeNearsideAxle3, journey.serviceBrakeOffsideAxle3,
                        journey.parkingBrakeNearside, journey.parkingBrakeOffside);

                // And I set the first axle as the steered axle
                driverWrapper.selectCheckboxById("serviceBrake1SteeredAxle1");
                break;

            default:
                String message = "Unknown brake results journey: " + journey;
                logger.error(message);
                throw new IllegalArgumentException(message);

        }
        // And I press the "Submit" button
        driverWrapper.pressButton("Submit");

        // And The page title contains "Brake test summary"
        driverWrapper.checkCurrentPageTitle("Brake test summary");
        // And I click the "Done" link
        driverWrapper.clickLink("Done");
    }


    /**
     * Adds a break test values for a 2 axle vehicle.
     * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
     * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
     * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
     * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
     * @param parkingBrakeNearside          Weight applied by parking brake to nearside
     * @param parkingBrakeOffside           Weight applied by parking brake to offside
     */
    private void enterBrakeValues2Axles(Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                                        Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                                        Integer parkingBrakeNearside, Integer parkingBrakeOffside) {
        // And The page title contains "Add brake test results"
        driverWrapper.checkCurrentPageTitle("Add brake test results");
        // And I enter <n> in the field with id "serviceBrakeEffortNearsideAxle1"
        driverWrapper.enterIntoFieldWithId(serviceBrakeNearsideAxle1, "serviceBrakeEffortNearsideAxle1");
        // And I enter <n> in the field with id "serviceBrakeEffortOffsideAxle1"
        driverWrapper.enterIntoFieldWithId(serviceBrakeOffsideAxle1, "serviceBrakeEffortOffsideAxle1");
        // And I enter <n> in the field with id "serviceBrakeEffortNearsideAxle2"
        driverWrapper.enterIntoFieldWithId(serviceBrakeNearsideAxle2, "serviceBrakeEffortNearsideAxle2");
        // And I enter <n> in the field with id "serviceBrakeEffortOffsideAxle2"
        driverWrapper.enterIntoFieldWithId(serviceBrakeOffsideAxle2, "serviceBrakeEffortOffsideAxle2");
        // And I enter <n> in the field with id "parkingBrakeEffortNearside"
        driverWrapper.enterIntoFieldWithId(parkingBrakeNearside, "parkingBrakeEffortNearside");
        // And I enter <n> in the field with id "parkingBrakeEffortOffside"
        driverWrapper.enterIntoFieldWithId(parkingBrakeOffside, "parkingBrakeEffortOffside");
    }

    /**
     * Adds a break test values for a 3 axle vehicle.
     * @param serviceBrakeNearsideAxle1     Weight applied by service brake to nearside axle 1
     * @param serviceBrakeOffsideAxle1      Weight applied by service brake to offside axle 1
     * @param serviceBrakeNearsideAxle2     Weight applied by service brake to nearside axle 2
     * @param serviceBrakeOffsideAxle2      Weight applied by service brake to offside axle 2
     * @param serviceBrakeNearsideAxle3     Weight applied by service brake to nearside axle 3
     * @param serviceBrakeOffsideAxle3      Weight applied by service brake to offside axle 3
     * @param parkingBrakeNearside          Weight applied by parking brake to nearside
     * @param parkingBrakeOffside           Weight applied by parking brake to offside
     */
    private void enterBrakeValues3Axles(Integer serviceBrakeNearsideAxle1, Integer serviceBrakeOffsideAxle1,
                                        Integer serviceBrakeNearsideAxle2, Integer serviceBrakeOffsideAxle2,
                                        Integer serviceBrakeNearsideAxle3, Integer serviceBrakeOffsideAxle3,
                                        Integer parkingBrakeNearside, Integer parkingBrakeOffside) {

        // And I enter all the details for a 2 axle vehicle
        enterBrakeValues2Axles(serviceBrakeNearsideAxle1, serviceBrakeOffsideAxle1, serviceBrakeNearsideAxle2,
                serviceBrakeOffsideAxle2, parkingBrakeNearside, parkingBrakeOffside);

        // And I enter <n> in the field with id "serviceBrakeEffortOffsideAxle3"
        driverWrapper.enterIntoFieldWithId(serviceBrakeNearsideAxle3, "serviceBrakeEffortNearsideAxle3");
        // And I enter <n> in the field with id "serviceBrakeEffortOffsideAxle3"
        driverWrapper.enterIntoFieldWithId(serviceBrakeOffsideAxle3, "serviceBrakeEffortOffsideAxle3");
    }

    /**
     * Checks either the Single or Dual Line brake type radio button.
     * @param condition    The true or false value of the brake type
     */
    private void breakLineType(Boolean condition) {
        if (condition) {
            // And I click the "Single" radio button in fieldset "Brake line type"
            driverWrapper.selectRadioInFieldset("Brake line type", "Single");
        } else {
            // And I click the "Dual" radio button in fieldset "Brake line type"
            driverWrapper.selectRadioInFieldset("Brake line type", "Dual");
        }
    }

    /**
     * Selects the correct radio button depending on the vehicle class type.
     * @param condition    The true or false value of the brake weight type
     */
    private void breakTestWeightType(Boolean condition) {
        if (condition) {
            // And I click the "DGW (design gross weight from manufacturers plate)" radio button
            driverWrapper.selectRadio("DGW (design gross weight from manufacturers plate)");
        } else {
            // And I click the "Brake test weight (from manufacturer or other reliable data)" radio button
            driverWrapper.selectRadio("Brake test weight (from manufacturer or other reliable data)");
        }
    }

    /**
     * Marks the specified defect as repaired.
     * @param defect    The defect
     */
    private void markAsRepaired(String defect) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I press the "Mark as repaired" button for the specified defect
        driverWrapper.pressButtonWithSiblingElement(
                "Mark as repaired","input", "value", defect);
    }

    /**
     * Marks the manual advisory defect as repaired.
     */
    private void markManualAdvisoryAsRepaired() {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I press the "Mark as repaired" button for the specified defect
        driverWrapper.pressButtonWithSiblingElement(
                "Mark as repaired","input", "value", " ");
    }

    /**
     * Edits the specified defect, updating the comment, and possibly marking as dangerous. Refactored repeated
     * cucumber steps, the original steps are detailed below.
     * @param defectType            The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect                The defect
     * @param updatedComment        The updated comment
     * @param isDangerous           Whether this defect should be marked as dangerous
     * @param isFirstUseDateWarning Whether this defect will trigger the First use date Warning Page
     */
    private void editDefect(String defectType, String defect, String updatedComment, boolean isDangerous,
                            boolean isFirstUseDateWarning) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        // edit the defect
        handleDefect(DefectJourney.EditFromSummary, defect, DefectType.fromString(defectType), updatedComment,
                isDangerous, isFirstUseDateWarning);

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Removes the specified defect. Refactored repeated cucumber steps, the original steps are detailed below.
     * @param defectType        The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect            The defect
     */
    private void removeDefect(String defectType, String defect) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        // And I click the "Remove" link for the specified defect
        driverWrapper.clickLink("h4", defect, "../../ul/", "Remove");

        // Note: page title is blank

        // And I press the "Remove <failure/PRS/advisory>" button
        driverWrapper.pressButton("Remove " + DefectType.fromString(defectType).description);

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a defect to the current mot tests by browsing through the specified category.
     * @param defectType        The defect type, must be "Advisory", "PRS" or "Failure"
     * @param category          The defect category
     * @param subcategory       The defect sub-category (if any)
     * @param subSubCategory    The defect second sub-category (if any)
     * @param defect            The defect
     * @param comment           The comment to use
     */
    private void addDefectFromBrowse(String defectType, String category, Optional<String> subcategory,
                                     Optional<String> subSubCategory, String defect, String comment) {
        // Browse to the desired defect
        browseForDefect(category, subcategory, subSubCategory);

        // Add the defect
        addDefectAndReturnToResults(DefectJourney.AddFromBrowse, defect, DefectType.fromString(defectType),
                comment, false, false, "Defects");
    }

    /**
     * Adds a First use Date warning defect to the current mot tests by browsing through the specified category.
     * @param defectType        The defect type, must be "Advisory", "PRS" or "Failure"
     * @param category          The defect category
     * @param subcategory       The defect sub-category (if any)
     * @param subSubCategory    The defect second sub-category (if any)
     * @param defect            The defect
     * @param comment           The comment to use
     */
    private void addDefectFromBrowseAlert(String defectType, String category, Optional<String> subcategory,
                                     Optional<String> subSubCategory, String defect, String comment) {
        // Browse to the desired defect
        browseForDefect(category, subcategory, subSubCategory);

        // Add the defect
        addDefectAndReturnToResults(DefectJourney.AddFromBrowse, defect, DefectType.fromString(defectType),
                comment, false, true, "Defects");
    }

    /**
     * Adds a defect to the current MOT test, by browsing through the specified category and optional sub-category.
     * Refactored repeated cucumber steps, the original steps are detailed below.
     * @param category      The defect category
     * @param subcategory   The defect sub-category (if any)
     */
    private void browseForDefect(String category, Optional<String> subcategory, Optional<String> subsubcategory) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the <category> link
        driverWrapper.clickLink(category);

        subcategory.ifPresent(value -> {
            // And I click the <subcategory> link
            driverWrapper.clickLink(value);
        });

        subsubcategory.ifPresent(value -> {
            // And I click the <subsubcategory> link
            driverWrapper.clickLink(value);
        });
    }

    /**
     * Adds a defect a returns the Mot test results page.
     * @param journey       The journey taken to add the defect
     * @param defect        The defect
     * @param defectType    The defect type, must be "Advisory", "PRS" or "Failure"
     * @param comment       The comment to use
     * @param isDangerous   Is the defect dangerous
     * @param pageTitle     The title of the page the journey returns to after adding the defect
     */
    private void addDefectAndReturnToResults(DefectJourney journey, String defect, DefectType defectType,
                                             String comment, boolean isDangerous, boolean isFirstUseDateWarning,
                                             String pageTitle) {
        // Add the defect
        handleDefect(journey, defect, defectType, comment, isDangerous, isFirstUseDateWarning);
        // And The page title contains "Search for a defect"
        driverWrapper.checkCurrentPageTitle(pageTitle);
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a manual advisory defect to the current MOT test. Refactored repeated cucumber steps, the original steps
     * are detailed below.
     * @param comment       The comment to use
     */
    private void addManualAdvisory(String comment) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the "Add a manual advisory" link
        driverWrapper.clickLink("Add a manual advisory");

        // note - page title is blank
        // And I enter <comment> into the "Give brief details of the defect" field
        driverWrapper.enterIntoField(comment, "Give brief details of the defect");
        // And I press the "Add manual advisory" button
        driverWrapper.pressButton("Add manual advisory");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a defect to the current MOT test, by searching for the specified defect text. Refactored repeated cucumber
     * steps, the original steps are detailed below.
     * @param defect        The defect
     */
    private void searchForDefect(String defect) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Search for a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Search for a defect"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I enter <text> into the <search-main> field (has no label)
        driverWrapper.enterIntoFieldWithId(defect, "search-main");
        // And I press the "Search" button
        driverWrapper.pressButton("Search");
    }

    /**
     * Add a defect by searching for it.
     * @param defectType    The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect        The defect
     * @param comment       The comment to use
     */
    private void addDefectFromSearch(String defectType, String defect, String comment) {
        //Search for the defect
        searchForDefect(defect);

        //Add the defect and return to results
        addDefectAndReturnToResults(DefectJourney.AddFromSearch, defect, DefectType.fromString(defectType),
                comment, false, false, "Search for a defect");
    }

    /**
     * Opens a specified manuals link and checks the page is as expected.
     * @param defect the defect to search for
     * @param manualLinkText the manual link text
     * @param manualTitle the title of the manuals page
     */
    private void openManualLinkFromSearchPage(String defect, String manualLinkText, String manualTitle) {
        //Search for defect
        searchForDefect(defect);
        //Click the first manual reference link
        driverWrapper.clickFirstLink(manualLinkText);

        //Check the second Tab is open
        assertEquals("The new tab was not opened", driverWrapper.getCurrentTabsCount(), 2);

        //Switch to the second tab
        driverWrapper.switchToTab(1);

        //Check the manual page title
        driverWrapper.checkCurrentPageTitle(manualTitle);

        //Switch back to the first tab
        driverWrapper.switchToTab(0);

        //Close other tabs
        driverWrapper.closeTabs();

        //Check the current page title
        driverWrapper.checkCurrentPageTitle("Search for a defect");
    }

    /**
     * Handles adding and editing defects of all types, from defect browse or search.
     * @param journey                   The user journey being followed
     * @param defect                    The defect description
     * @param defectType                The defect type
     * @param comment                   The comment to add/update
     * @param isDangerous               Whether to mark this defect as dangerous
     * @param isFirstUseDateWarning     Whether this defect will trigger the First use date Warning Page
     */
    private void handleDefect(DefectJourney journey, String defect, DefectType defectType, String comment,
                              boolean isDangerous, boolean isFirstUseDateWarning) {
        switch (journey) {
            case AddFromSearch:
                // And I click the <Advisory> button for the specified defect
                // (note - need to ignore the defect listed as "You searched for...")
                if (defectType == DefectType.PreEUAdvisory || defectType == DefectType.PreEUFailure) {
                    driverWrapper.clickLink("div/strong", defect, "../../ul/", defectType.type);
                } else {
                    driverWrapper.clickLink("span", defect, "../div/", defectType.type);
                }
                break;

            case AddFromBrowse:
                // And I click the Advisory button for the specified defect
                if (defectType == DefectType.PreEUAdvisory || defectType == DefectType.PreEUFailure) {
                    driverWrapper.clickLink("strong", defect, "../../ul/", defectType.type);
                } else {
                    driverWrapper.clickLink("span", defect, "../div/", defectType.type);
                }
                break;

            case EditFromSummary:
                // And I click the "Edit" link for the specified defect
                driverWrapper.clickLink("h4", defect, "../../ul/", "Edit");
                break;

            default:
                break;
        }

        if (isFirstUseDateWarning) {
            // And I check for the correct page text for the defect warning
            // And I click the "Defect is correct — continue" button
            assertTrue(driverWrapper.containsMessage("This defect is for vehicles newer than the one you're testing"));
            assertTrue(driverWrapper.containsMessage(defect.toLowerCase()));
            driverWrapper.clickLink("Defect is correct — continue");
        }

        // Note: new page title at this point is not always populated

        // And I enter <comment> into the "Add any further comments if required" field
        driverWrapper.enterIntoField(comment, "Add any further comments if required");

        if (isDangerous) {
            // And I select the "This defect is dangerous" checkbox
            driverWrapper.selectCheckbox("This defect is dangerous");
        }

        // And I press the "<Add/Edit> <advisory>" button
        driverWrapper.pressButton(journey.action + " " + defectType.description);
    }

    /** Encapsulates the user journey being followed. */
    private enum DefectJourney {
        AddFromSearch("Add"), AddFromBrowse("Add"), EditFromSummary("Edit");

        /** The action being applied to the defect. */
        private final String action;

        /**
         * Creates a new instance.
         * @param action    The defect action
         */
        DefectJourney(String action) {
            this.action = action;
        }
    }

    /** Encapsulates the type of defect. */
    private enum DefectType {
        Dangerous("Dangerous failure", "dangerous failure"),
        Major("Major failure", "major failure"),
        Minor("Minor defect", "minor defect"),
        PreEUFailure("Failure", "failure"),
        PreEUAdvisory("Advisory", "advisory"),
        PRS("PRS", "PRS"),
        Advisory("Advisory", "advisory");

        /** Capitalised defect type. */
        private final String type;

        /** Lower case (unless abbreviated) defect type. */
        private final String description;

        /**
         * Creates a new instance.
         * @param type              The Capitalised defect type.
         * @param description       The Lower case (unless abbreviated) defect type.
         */
        DefectType(String type, String description) {
            this.type = type;
            this.description = description;
        }

        /**
         * Converts a <code>String</code> to a <code>DefectType</code>.
         * @param type      The string
         * @return The defect type
         * @throws IllegalArgumentException If the string is invalid
         */
        public static DefectType fromString(String type) {
            switch (type) {
                case "Dangerous":
                    return Dangerous;

                case "Major":
                    return Major;

                case "Minor":
                    return Minor;

                case "Pre EU Failure":
                    return PreEUFailure;

                case "PRS":
                    return PRS;

                case "Advisory":
                    return Advisory;

                case "Pre EU Advisory":
                    return PreEUAdvisory;

                default:
                    String message = "Unknown defect type: " + type;
                    logger.error(message);
                    throw new IllegalArgumentException(message);
            }
        }
    }
}
