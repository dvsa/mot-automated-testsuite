package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.excel.ExcelDocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

public class RfrDefinitions implements En {

    private Map<String, String> documentSheets;
    private Map<String, Map<String, Map<String, Map<String, String>>>> externalData;

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(RfrDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /** The test results to write to the excel file. */
    private Map<String, List<List<String>>> results;

    /**
     * Creates a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public RfrDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating new RFRDefinitions...");
        this.driverWrapper = driverWrapper;
        this.externalData = new HashMap<>();
        this.documentSheets = new HashMap<>();
        this.results = new HashMap<>();
        results.put("Find by Search", new ArrayList<>());
        results.put("Find by Component Tree", new ArrayList<>());

        results.get("Find by Search").add(Arrays.asList("RFR ID", "Component ID", "Expected Component Name",
                "Expected Condition", "Expected Advisory Condition", "Expected Manual Reference",
                "Expected Deficiency", "Status", "Comments"));
        results.get("Find by Component Tree").add(Arrays.asList("RFR ID", "Component ID", "Expected Component Name",
                "Expected Condition", "Expected Advisory Condition", "Expected Manual Reference",
                "Expected Deficiency", "Status", "Comments"));

        And("^I check every \"([^\"]*)\" RFR for class \"([^\"]*)\" "
                        + "using loaded RFR sheet \"([^\"]*)\" "
                        + "and Component Tree sheet \"([^\"]*)\" in \"([^\"]*)\" file$",
                (String type, String vehicleClass, String rfrSheet, String componentTreeSheet, String file) -> {
                    //Loop through all rfr id's, perform both tests and move onto next id
                    String firstKey = (String) externalData.get(file).get(rfrSheet).keySet().toArray()[0];
                    Set<String> rfrIdList = externalData.get(file).get(rfrSheet).get(firstKey).keySet();

                    int currentCount = 0;
                    String rfrCount = String.format("Total rfr count is: '%s'.", rfrIdList.size());
                    logger.debug(rfrCount);
                    System.out.println(rfrCount);

                    for (String rfrId : rfrIdList) {
                        ++currentCount;

                        String rfrLeft = String.format("RFRs tested '%s', RFRs left '%s'.", currentCount,
                                rfrIdList.size());
                        logger.debug(rfrLeft);
                        System.out.println(rfrLeft);

                        logger.debug(String.format("Trying to get RFR Type string, file: %s, rfrSheet: %s"
                                + ", vehicle class: 'Class %s', rfrID: %s", file, rfrSheet, vehicleClass, rfrId));

                        String rfrTypeString = externalData.get(file).get(rfrSheet)
                                .get("Class " + vehicleClass).get(rfrId).toLowerCase().trim();

                        boolean rfrType = rfrTypeString.equals("y");

                        boolean testType = type.equals("positive");

                        logger.debug("RFR ID: " + rfrId + ", rfrType: " + rfrType + ", testType" + testType);
                        System.out.println(String.format("RFR ID - '%s'", rfrId));

                        if (rfrType != testType) {
                            logger.debug("Skipping rfr with ID " + rfrId
                                    + " as it does not match test type for class" + vehicleClass + ".");
                            continue;
                        }

                        String componentId = externalData.get(file).get(rfrSheet).get("Component ID").get(rfrId);
                        List<String> componentElements = new ArrayList<>();

                        List<String> componentElementNames = Arrays.asList("L5", "L4", "L3", "L2", "L1");

                        logger.debug("Component ID: " + componentId);
                        System.out.println("Component ID: " + componentId);

                        String parentId = componentId;

                        for (int i = 0; i < componentElementNames.size(); ++i) {
                            if (externalData.get(file).get(componentTreeSheet).containsKey(
                                    componentElementNames.get(i))) {
                                String component = externalData.get(file).get(componentTreeSheet)
                                        .get(componentElementNames.get(i)).get(parentId);

                                logger.debug("Component: " + component);

                                if (component == null || component.trim().equals("")) {
                                    continue; // this is not the bottom component in the tree
                                }

                                componentElements.add(component);

                                logger.debug("Parent ID: " + parentId + ", component: " + component);
                                System.out.println("Parent ID: " + parentId + ", component: " + component);

                                //stop at L2, as it will get parent to L1
                                for (int j = i + 1; j < componentElementNames.size(); ++j) {
                                    logger.debug(String.format("Trying to get %s column.",
                                            componentElementNames.get(j)));

                                    parentId = externalData.get(file).get(componentTreeSheet)
                                            .get("Parent ID").get(parentId);

                                    component = externalData.get(file).get(componentTreeSheet)
                                            .get(componentElementNames.get(j)).get(parentId);

                                    logger.debug("Parent ID: " + parentId + ", component: " + component);
                                    System.out.println("Parent ID: " + parentId + ", component: " + component);

                                    componentElements.add(component);
                                }

                                Collections.reverse(componentElements);

                                break;
                            }
                        }

                        findRfrByComponentTree(file, componentTreeSheet, rfrSheet,
                                componentId, componentElements, rfrId);
                        //findRfrBySearch(file, componentTreeSheet, rfrSheet, componentId, componentElements, rfrId);
                    }

                    ExcelDocument.writeFile("target/documents/RFR_Results_Class-" + vehicleClass
                                    + "_Type-" + type + ".xlsx",
                            results);
            });

        When("^I load \"([^\"]*)\" workbook sheet from \"([^\"]*)\". I map columns by ID's as:$",
                (String sheetName, String filePath, DataTable table) ->
                        loadDataFromSpreadsheet(sheetName, filePath, table));
    }

    /**
     * Load data from spreadsheet.
     *
     * @param sheetName to load from workbook.
     * @param filePath to load the file from.
     * @param table to map the data in the file - loading skips first row, which should be column headers.
     */
    private void loadDataFromSpreadsheet(String sheetName, String filePath, DataTable table) {
        if (documentSheets.containsKey(filePath) && documentSheets.containsValue(sheetName)) {
            return;
        } else {
            documentSheets.put(filePath, sheetName);
        }

        ExcelDocument document = ExcelDocument.getDocument(filePath);

        document.setCurrentSheet(sheetName);

        List<List<String>> rawData = table.raw();

        for (List<String> row : rawData.subList(1, rawData.size())) {
            String datasetKey = row.get(0);
            List<String> dataKey = document.getColumn(Integer.parseInt(row.get(1)));
            dataKey = dataKey.subList(1, dataKey.size());
            List<String> data = document.getColumn(Integer.parseInt(row.get(2)));
            data = data.subList(1, data.size());

            loadExternalData(filePath, sheetName, datasetKey, dataKey, data);
        }
    }

    private void findRfrByComponentTree(String file, String componentSheet, String rfrSheet,
                                        String componentId, List<String> componentElements, String rfrId) {
        String categoryPath = "";
        try {
            logger.debug(String.format("Component Tree - file: " + file + ", sheet: " + componentSheet
                    + ", componentID: " + componentId + ", componentElements: " + String.join(", ",
                    componentElements)));

            for (String componentElement : componentElements) {
                categoryPath += componentElement + "/";
                logger.debug(String.format("Trying to click component: %s. Component exists: %s.",
                        componentElement, driverWrapper.checkElementExists(componentElement) ? "true" : "false"));
                driverWrapper.clickLink(componentElement);
            }

            checkIfPageContainsRfr(file, componentSheet, rfrSheet, componentId, componentElements, rfrId, false);
        } catch (Exception ex) {
            results.get("Find by Component Tree").add(Arrays.asList(rfrId, componentId, "-", "-", "-",
                    "-", "-", "fail", String.format("Category or sub-category missing. Category path: '%s'",
                            categoryPath)));
        }

        try {
            driverWrapper.clickFirstLink("Categories");
        } catch (Exception ex) {
            System.out.println("Failed to find defect dategories link...");
            logger.debug("Failed to find defect dategories link...");
        }
    }

    private void findRfrBySearch(String file, String componentSheet, String rfrSheet,
                                 String componentId, List<String> componentElements, String rfrId) {
        logger.debug(String.format("Component Tree - file: " + file + ", sheet: " + componentSheet
                + ", componentID: " + componentId));

        checkIfPageContainsRfr(file, componentSheet, rfrSheet, componentId, componentElements, rfrId, true);

        try {
            driverWrapper.clickFirstLink("Return to defect categories");
        } catch (Exception ex) {
            try {
                driverWrapper.clickFirstLink("defect categories");
            } catch (Exception ex2) {
                System.out.println("Failed to find defect dategories link...");
                logger.debug("Failed to find defect dategories link...");
            }
        }
    }

    private void checkIfPageContainsRfr(String file, String componentTreeSheet,String rfrSheet,
                                        String componentId, List<String> componentElements,
                                        String rfrId, boolean isSearch) {
        List<String> comments = new ArrayList<>();

        String defect = externalData.get(file).get(rfrSheet).get("Condition").get(rfrId);
        System.out.println(String.format("Defect - '%s'", defect));


        String description = externalData.get(file).get(componentTreeSheet).get("Description").get(componentId);
        String subComponent = externalData.get(file).get(rfrSheet).get("Sub Component").get(rfrId);
        boolean hasSubComponent = subComponent.matches(".*[a-zA-Z].*");
        String defectTitle = (hasSubComponent ? getDefectTitle(description, subComponent) : description);
        System.out.println(String.format("Description - '%s'\nSubComponent - '%s'\nDefect Title - '%s'",
                description, subComponent, defectTitle));

        String search = description.trim() + " " + defect.trim();

        try {
            if (isSearch) {
                driverWrapper.enterIntoFieldWithId(search, "search-main");
                driverWrapper.clickButton("Search");
            }
        } catch (Exception ex) {
            results.get("Find by Search").add(Arrays.asList(rfrId, componentId, "-", "-", "-",
                    "-", "-", "fail", String.format("Failed to find defect using search. Search phrase - '%s'",
                            search)));

            return;
        }

        boolean passed = true;
        String rootPath = "";
        if (!isSearch) {
            rootPath = "div/h2";
            try {
                if (!driverWrapper.checkElementExistsInsensitive(rootPath, defectTitle)) {
                    comments.add(String.format("Failed to find defect title '%s', with xpath '%s'.",
                            defectTitle, rootPath));
                    passed = false;
                }
            } catch (Exception ex) {
                passed = false;
            }
        } else {
            rootPath = "div/ul/li/div/span";

            if (!driverWrapper.checkElementExistsInsensitive(rootPath, defectTitle)) {
                comments.add(String.format("Failed to find defect title '%s', with xpath '%s'.",
                        defectTitle, rootPath));
                passed = false;
            }
        }

        String defectPath = (isSearch ? rootPath + "[contains(translate(text(), "
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                + defectTitle + "\"/.." : "div/")
                + "ul/li/div/span";
        try {
            if (!driverWrapper.checkElementExistsInsensitive(defectPath, defect)) {
                comments.add(String.format("Failed to find defect '%s', with xpath '%s'.",
                        defect, defectPath));
                passed = false;
            }
        } catch (Exception ex) {
            passed = false;
        }

        String manualReferencePath = defectPath + "[contains(translate(text(), "
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                + defect + "\")]/../div/span/a";
        String manualReference = externalData.get(file).get(rfrSheet).get("Manual Ref").get(rfrId);
        System.out.println(String.format("Manual Reference - '%s'", manualReference));
        try {
            if (!driverWrapper.checkElementExistsInsensitive(manualReferencePath, manualReference)) {
                comments.add(String.format("Failed to manual reference '%s', with xpath '%s'.",
                        manualReference, manualReferencePath));
                passed = false;
            }
        } catch (Exception ex) {
            passed = false;
        }

        String prsButton = "PRS";
        String deficiencyCategory = externalData.get(file).get(rfrSheet).get("Deficiency Category").get(rfrId);
        System.out.println(String.format("Deficiency category: '%s'", deficiencyCategory));
        String deficiencyButton = getDeficiencyCategory(deficiencyCategory);
        String prsAndDeficiencyButtonsPath = defectPath + "[contains(translate(text(), "
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                + defect + "\")]/../div/a";
        try {
            if (!driverWrapper.checkElementExistsInsensitive(prsAndDeficiencyButtonsPath, prsButton)) {
                comments.add(String.format("Failed to prs button '%s', with xpath '%s'.",
                        prsButton, prsAndDeficiencyButtonsPath));
                passed = false;
            }
            if (!driverWrapper.checkElementExistsInsensitive(prsAndDeficiencyButtonsPath, deficiencyButton)) {
                comments.add(String.format("Failed to deficiency button '%s', with xpath '%s'.",
                        manualReference, manualReferencePath));
                passed = false;
            }
        } catch (Exception ex) {
            passed = false;
        }

        String advisory = externalData.get(file).get(rfrSheet).get("Advisory Condition").get(rfrId);
        boolean isAdvisory = advisory != null && advisory != "" && advisory.matches(".*[a-zA-Z].*");
        System.out.println((isAdvisory ? String.format("Advisory Condition - '%s'", advisory) : "No advisory."));

        if (isAdvisory) {
            String advisoryPath = defectPath + "[contains(translate(text(), "
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                    + defect + "\")]/../ul/li/dev/span";
            String advisoryButtonPath = advisoryPath + "[contains(translate(text(), "
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                    + advisory + "\")]/../div/a";
            String advisoryButton = "Advisory";
            try {
                if (!driverWrapper.checkElementExistsInsensitive(advisoryPath, advisory)) {
                    comments.add(String.format("Failed to advisory '%s', with xpath '%s'.",
                            advisory, advisoryPath));
                    passed = false;
                }
                if (!driverWrapper.checkElementExistsInsensitive(advisoryButtonPath, advisoryButton)) {
                    comments.add(String.format("Failed to advisory button '%s', with xpath '%s'.",
                            advisoryButton, advisoryButtonPath));
                    passed = false;
                }
            } catch (Exception ex) {
                passed = false;
            }
        }
        //
        //        if (manualReference != null && manualReference.matches(".*[1-9].*")) {
        //            //Click the manual reference here and go to the new tab
        //            String manualRef = manualReference.split(" ")[0];
        //            String manualReferenceHeader = "";
        //            String manualReferenceHeaderTag = "";
        //
        //            for (int i = 0; i < componentElements.size(); ++i) {
        //                manualReferenceHeader = manualRef.trim() + " " + componentElements.get(i).trim();
        //                manualReferenceHeaderTag = "h" + String.valueOf(i + 2);
        //                String sectionId = "section_" + manualRef;
        //
        //                try {
        //                    if (!driverWrapper.checkElementContainsIdInsensitive(
        // manualReferenceHeaderTag, manualReferenceHeader,
        //                            sectionId)) {
        //                        comments.add(String.format("Header ID does not match the expected value of '%s'.",
        //                                sectionId));
        //
        //                        passed = false;
        //                    }
        //                } catch (Exception ex) {
        //                    comments.add(String.format("Failed to find the manual reference header '%s'.",
        //                            manualReferenceHeader));
        //
        //                    passed = false;
        //                }
        //            }
        //
        //            try {
        //                String manualRefL1 = manualReference.split(" ")[1];
        //                String manualRefL2 = "";
        //
        //                try {
        //                    try {
        //                        manualRefL2 = manualReference.split(" ")[2];
        //                    } catch (Exception ex) {
        //                        //just swallow the exception as no man ref l2 is present
        //                    }
        //
        //                    String defectDeficiencyXpath = manualReferenceHeaderTag + "[contains(translate(text(), "
        //                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
        //                            + manualReferenceHeader + "\")]/following::p/following::table[1]/tbody/tr/td[2]";
        //
        //                    WebElement manualDeficiencyCategory =
        // driverWrapper.getElemenetInsensitive(defectDeficiencyXpath,
        //                            deficiencyCategory);
        //                } catch (Exception ex) {
        //                    comments.add(
        // String.format("Failed to match the deficiency category for manual reference '%s' with"
        //                            + " the value of '%s'.", manualReference, deficiencyCategory));
        //
        //                    passed = false;
        //                }
        //
        //            } catch (Exception ex) {
        //                comments.add(String.format("Failed to get deficiency category list for the sub-category."));
        //
        //                passed = false;
        //            }
        //            //close the tab
        //        }

        StringBuilder commentsString = new StringBuilder();

        for (String comment : comments) {
            commentsString.append(comment).append("\n\n");
        }

        results.get((isSearch ? "Find by Search" : "Find by Component Tree")).add(Arrays.asList(rfrId, componentId,
                defectTitle, defect, (isAdvisory ? advisory : "-"), manualReference, deficiencyButton,
                passed ? "pass" : "fail", commentsString.toString()));
    }

    /**
     * Load the data from an external data source.
     *
     * @param file to load the data from.
     * @param sheet name.
     * @param alias to use for the data set.
     * @param dataKeys to load the data from.
     * @param data to load the data from.
     */
    private void loadExternalData(String file, String sheet, String alias, List<String> dataKeys, List<String> data) {
        if (!externalData.containsKey(file)) {
            externalData.put(file, new HashMap<>());
        }
        if (!externalData.get(file).containsKey(sheet)) {
            externalData.get(file).put(sheet, new HashMap<>());
        }
        if (!externalData.get(file).get(sheet).containsKey(alias)) {
            externalData.get(file).get(sheet).put(alias, new HashMap<>());
        }

        if (dataKeys.size() != data.size()) {
            throw new RuntimeException("Unable to load excel data - data key list size different than data size.");
        }

        for (int i = 0; i < dataKeys.size(); ++i) {
            if ((dataKeys.get(i) != null || dataKeys.get(i) != "")
                    && (data.get(i) != null || data.get(i) != "")) {
                externalData.get(file).get(sheet).get(alias).put(dataKeys.get(i), data.get(i));
            }
        }
    }

    private String getDeficiencyCategory(String rawDeficiencyCategory) {
        switch (rawDeficiencyCategory) {
            case "Minor":
                return rawDeficiencyCategory + " defect";

            case "Major":
            case "Dangerous":
                return rawDeficiencyCategory + " failure";
            default:
                logger.debug(String.format("Unknown deficiency category: %s.", rawDeficiencyCategory));
                return rawDeficiencyCategory;
        }
    }

    private String getDefectTitle(String description, String subComponent) {
        return description
                + ((subComponent != null || subComponent.length() < 3 || subComponent != "" || subComponent != " ")
                ? ": " + subComponent : "");
    }
}
