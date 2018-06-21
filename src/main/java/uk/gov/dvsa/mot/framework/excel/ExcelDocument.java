package uk.gov.dvsa.mot.framework.excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.elasticsearch.ElasticSearchResult;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Excel document class for writing results to an excel document.
 */
public class ExcelDocument {
    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(ExcelDocument.class);

    /**
     * Constructor for excel document.
     */
    public ExcelDocument() {

    }

    /**
     * Compares the tests results from two different environments.
     * @param resultSet1 the first result set
     * @param resultSet2 the second result set
     * @param csvFile the csvFile which provided the data
     */
    public void compareTestResults(ArrayList<ElasticSearchResult> resultSet1, ArrayList<ElasticSearchResult> resultSet2,
                                   String csvFile) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Search Results");

        CellStyle boldAndBorderStyle = workbook.createCellStyle();
        boldAndBorderStyle.setAlignment(HorizontalAlignment.CENTER);
        boldAndBorderStyle.setBorderRight(BorderStyle.MEDIUM);
        boldAndBorderStyle.setBorderLeft(BorderStyle.MEDIUM);
        boldAndBorderStyle.setBorderBottom(BorderStyle.MEDIUM);
        boldAndBorderStyle.setBorderTop(BorderStyle.MEDIUM);

        Row headerRow = sheet.createRow(0);
        String[] headerRows = {"Search term", "Original Top Ten", "New Top Ten"};

        for (int i = 0; i < headerRows.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(boldAndBorderStyle);
            cell.setCellValue(headerRows[i]);
        }

        int rowNumber = 1;
        int indexCounter = 0;

        for (ElasticSearchResult elasticSearchResult1 : resultSet1) {

            ElasticSearchResult elasticSearchResult2 = resultSet2.get(indexCounter);
            String searchTermData;

            if (hasTheOrderChanged(elasticSearchResult1.getTopTenList(), elasticSearchResult2.getTopTenList())) {
                searchTermData = String.format("%s, (%d, %d) Order has changed", elasticSearchResult1.getSearchTerm(),
                        elasticSearchResult1.getTotalCount(), elasticSearchResult2.getTotalCount());
            } else {
                searchTermData = String.format("%s, (%d, %d)", elasticSearchResult1.getSearchTerm(),
                        elasticSearchResult1.getTotalCount(), elasticSearchResult2.getTotalCount());
            }

            Row row = sheet.createRow(rowNumber++);

            row.createCell(0).setCellValue(searchTermData);

            CellStyle removedStyle = workbook.createCellStyle();
            removedStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            removedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int originalRow = rowNumber;

            for (String result1 : elasticSearchResult1.getTopTenList()) {
                Row topTenRow = sheet.createRow(originalRow++);

                Cell originalValue = topTenRow.createCell(1);
                originalValue.setCellValue(result1);

                if (!elasticSearchResult2.getTopTenList().contains(result1)) {
                    originalValue.setCellStyle(removedStyle);
                }
            }

            CellStyle addativeStyle = workbook.createCellStyle();
            addativeStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            addativeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int updatedRow = rowNumber;

            for (String result2 : elasticSearchResult2.getTopTenList()) {
                Row topTenRow;
                Cell updatedValue;

                try {
                    topTenRow = sheet.getRow(updatedRow++);
                    updatedValue  = topTenRow.createCell(2);
                } catch (NullPointerException exception) {
                    topTenRow = sheet.createRow(updatedRow++);
                    updatedValue = topTenRow.createCell(2);
                }

                updatedValue.setCellValue(result2);

                if (!elasticSearchResult1.getTopTenList().contains(result2)) {
                    updatedValue.setCellStyle(addativeStyle);
                }
            }

            rowNumber = Math.max(updatedRow, originalRow);
            rowNumber++;
            indexCounter++;
        }

        try {
            FileOutputStream fileOut = new FileOutputStream("build/reports/selenium/" + csvFile + "Results.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (java.io.IOException exception) {
            logger.error(exception.getMessage());
        }

    }

    /**
     * Compares two string arrays on size and content for order changes.
     * @param topTenResults1 the first set of results to compare
     * @param topTenResults2 the second set of results to compare
     * @return return whether the lists have a different order.
     */
    private boolean hasTheOrderChanged(ArrayList<String> topTenResults1, ArrayList<String> topTenResults2) {
        if (topTenResults1.size() != topTenResults2.size()) {
            return true;
        } else {
            int counter = 0;
            for (String result1 : topTenResults1) {
                if (!result1.equals(topTenResults2.get(counter++))) {
                    return true;
                }
            }
        }
        return false;
    }
}
