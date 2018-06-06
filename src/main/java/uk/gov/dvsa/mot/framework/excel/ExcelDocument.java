package uk.gov.dvsa.mot.framework.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class for Excel Documents.
 */
public class ExcelDocument {
    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(ExcelDocument.class);

    /** Static formatter to convert cells to string. */
    private static DataFormatter formatter = new DataFormatter();

    /** The underlying Excel document. */
    private Workbook workbook;

    /** Sheet used to get the data stored in an Excel document. */
    private Sheet sheet;

    /**
     * public constructor.
     * @param workbook    The underlying Excel document
     */
    public ExcelDocument(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * Set the sheet to use by the ExcelDocument object.
     *
     * @param sheetName to load.
     */
    public void setCurrentSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
    }

    /**
     * Whether this Excel document contains all the string values.
     * @param value            String values to check for
     * @return                  whether the string is contained within the Excel
     * @throws ExcelException     error retrieving Excel contents
     */
    public boolean contains(String value) throws Exception {
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();

            Iterator<Cell> cells = row.iterator();

            while (cells.hasNext()) {
                Cell cell = cells.next();

                String cellString = formatter.formatCellValue(cell);

                if (value.equals(cellString)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Whether this Excel document contains all the string values.
     * @param values            list of string values to check for
     * @return                  whether all strings are contained within the Excel
     * @throws ExcelException     error retrieving Excel contents
     */
    public boolean contains(List<String> values) throws Exception {
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();

            Iterator<Cell> cells = row.iterator();

            while (cells.hasNext()) {
                Cell cell = cells.next();

                String cellString = formatter.formatCellValue(cell);

                if (values.contains(cellString)) {
                    values.remove(cellString);
                }
            }
        }

        if (values.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Loads document from a file path.
     *
     * @param filePath to load the document from.
     * @return the loaded document.
     */
    public static ExcelDocument getDocument(String filePath) {
        try {
            return new ExcelDocument(WorkbookFactory.create(new File(filePath)));
        } catch (Exception ex) {
            String message = String.format("Unable to load a file from: %s", filePath);

            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Get a column at a specified index.
     *
     * @param columnIndex to get.
     * @return column at the specified index.
     */
    public List<String> getColumn(int columnIndex) {
        List<String> column = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            column.add(formatter.formatCellValue(row.getCell(columnIndex)));
        }

        return column;
    }

    /**
     * Get a row.
     *
     * @param rowIndex of the row to get.
     * @return the row.
     */
    public List<String> getRow(int rowIndex) {
        List<String> row = new ArrayList<>();

        Iterator<Cell> cellIterator = sheet.getRow(rowIndex).cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            row.add(formatter.formatCellValue(cell));
        }

        return row;
    }

    /**
     * Gets the column index by column name.
     *
     * @param columnName to look for.
     * @return column index or -1 if the column with that name does not exist.
     */
    public int getColumnIndexByName(String columnName) {
        Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (formatter.formatCellValue(cell).equals(columnName)) {
                return cell.getColumnIndex();
            }
        }

        return -1;
    }

    /**
     * Write a file to the HDD.
     *
     * @param document name of the file to write.
     * @param sheets a map containing data of each individual sheet in the excel document, to write.
     */
    public static void writeFile(String document, Map<String, List<List<String>>> sheets) {
        Workbook workbook = new XSSFWorkbook();

        for (String key : sheets.keySet()) {
            Sheet outputSheet = workbook.createSheet(key);

            short green = IndexedColors.GREEN.getIndex();
            short red = IndexedColors.RED.getIndex();
            CellStyle passStyle = workbook.createCellStyle();
            CellStyle failStyle = workbook.createCellStyle();

            passStyle.setFillBackgroundColor(green);
            failStyle.setFillBackgroundColor(red);

            for (int i = 0; i < sheets.get(key).size(); ++i) {
                Row row = outputSheet.createRow(i);

                for (int j = 0; j < sheets.get(key).get(i).size(); ++j) {
                    Cell cell = row.createCell(j);

                    cell.setCellStyle((sheets.get(key).get(i).contains("fail")) ? failStyle : passStyle);
                    cell.setCellValue(new XSSFRichTextString(sheets.get(key).get(i).get(j)));
                }
            }

            try {
                File file = new File(document);
                file.mkdirs();
                file.delete();
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);

                workbook.write(outputStream);
            } catch (IOException io) {
                logger.error(String.format("Unable to write file %s.", document));
                System.out.println(String.format("Unable to write file %s.", document));
            }
        }
    }
}
