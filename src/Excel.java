import javafx.scene.control.Label;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Myles on 6/30/16.
 */
public class Excel {

    File file;
    POIFSFileSystem fs;
    XSSFWorkbook wb;
    XSSFSheet sheet;
    Statement stmt;
    Connection connection;
    Label alertPane;


    public Excel(File file, Statement stmt, Connection connection) throws IOException, InvalidFormatException {
        this.file = file;
        this.wb = new XSSFWorkbook(this.file);
        this.sheet = this.wb.getSheetAt(0);
        this.stmt = stmt;
        this.connection = connection;

    }

    private int order = 0;
    private long multipleKeys = 0;

    public void setAlertPane(Label alertPane)
    {
        this.alertPane = alertPane;
    }

    public void orderOfOperations() throws Throwable {

        rearrangeColumns();

        if(order == 1)
        {
            alertPane.setText("There was a problem rearranging columns in excel");
        }
        else if(order == 0) {
            clearSheet();
            if (order == 2) {}
            else if (order == 0) {
                checkData();
                importData();
                if (order == 3) {
                    alertPane.setText("There was a problem importing items into database");
                }
                if (getMultipleKeys() > 0) {
                    alertPane.setText("Success. But there were " + multipleKeys + " duplicate items found. Unimported items can be found in new excel file.");
                    close();
                    resetTime();
                }
                else if(getMultipleKeys() == 0)
                {
                    alertPane.setText("Success");
                }
            }
        }
        order = 0;
        reset();

    }

    public void rearrangeColumns() throws IOException {
        try
        {
            FileInputStream excellFile = new FileInputStream(file);
            XSSFWorkbook workbook1 = new XSSFWorkbook(excellFile);
            XSSFSheet mainSheet = workbook1.getSheetAt(0);
            String[] outColumns = {"BARCODE", "NAME", "VENDOR", "PRICE"};
            XSSFWorkbook outWorkBook = reArrange(mainSheet, mapHeaders(outColumns, mainSheet));
            excellFile.close();
            // write workbook into output file
            File mergedFile = this.file;
            if (!mergedFile.exists()) {
                mergedFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(mergedFile);
            outWorkBook.write(out);
            out.close();
            alertPane.setText("File Columns Were Re-Arranged Successfully");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            order = 1;
        }
    }

    public static XSSFWorkbook reArrange(XSSFSheet mainSheet,
                                         LinkedHashMap<String, Integer> map) {

        // get column headers
        Set<String> colNumbs = map.keySet();

        // Create New Workbook instance
        XSSFWorkbook outWorkbook = new XSSFWorkbook();
        XSSFSheet outSheet = outWorkbook.createSheet();

        // map for cell styles
        Map<Integer, XSSFCellStyle> styleMap = new HashMap<Integer, XSSFCellStyle>();

        int colNum = 0;
        XSSFRow hrow = outSheet.createRow(0);
        for (String col : colNumbs) {
            XSSFCell cell = hrow.createCell(colNum);
            cell.setCellValue(col);
            colNum++;
        }

        // This parameter is for appending sheet rows to mergedSheet in the end
        for (int j = mainSheet.getFirstRowNum() + 1; j <= mainSheet.getLastRowNum(); j++) {

            XSSFRow row = mainSheet.getRow(j);

            // Create row in main sheet
            XSSFRow mrow = outSheet.createRow(j);
            int num = -1;
            for (String k : colNumbs) {
                Integer cellNum = map.get(k);
                num++;
                if (cellNum != null) {
                    XSSFCell cell = row.getCell(cellNum.intValue());

                    // if cell is null then continue with next cell
                    if(cell == null) {
                        continue;
                    }
                    // Create column in main sheet
                    XSSFCell mcell = mrow.createCell(num);

                    if (cell.getSheet().getWorkbook() == mcell.getSheet()
                            .getWorkbook()) {
                        mcell.setCellStyle(cell.getCellStyle());
                    } else {
                        int stHashCode = cell.getCellStyle().hashCode();
                        XSSFCellStyle newCellStyle = styleMap.get(stHashCode);
                        if (newCellStyle == null) {
                            newCellStyle = mcell.getSheet().getWorkbook()
                                    .createCellStyle();
                            newCellStyle.cloneStyleFrom(cell.getCellStyle());
                            styleMap.put(stHashCode, newCellStyle);
                        }
                        mcell.setCellStyle(newCellStyle);
                    }

                    // set value based on cell type
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_FORMULA:
                            mcell.setCellFormula(cell.getCellFormula());
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            mcell.setCellValue(cell.getNumericCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_STRING:
                            mcell.setCellValue(cell.getStringCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            mcell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            mcell.setCellValue(cell.getBooleanCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_ERROR:
                            mcell.setCellErrorValue(cell.getErrorCellValue());
                            break;
                        default:
                            mcell.setCellValue(cell.getStringCellValue());
                            break;
                    }

                }
            }
        }
        return outWorkbook;
    }

    // get Map of Required Headers and its equivalent column number
    public static LinkedHashMap<String, Integer> mapHeaders(String[] outColumns,
                                                            XSSFSheet sheet) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        XSSFRow row = sheet.getRow(0);
        for (String outColumn : outColumns) {
            Integer icol = null;
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().equals(outColumn)) {
                    icol = new Integer(i);
                }
            }
            map.put(outColumn, icol);
        }
        return map;
    }

    public void importData() throws Throwable {
        Row myrow;
        Entry entry = new Entry();

        for (int row = 1; row <= sheet.getLastRowNum(); row++) {
            myrow = sheet.getRow(row);
            for (int i = myrow.getFirstCellNum(); i < myrow.getLastCellNum(); i++) {
                Cell cell = myrow.getCell(i);
                if (myrow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC && (i == 0 || i == 3)) {
                    if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC && i == 0)
                    {
                        entry.setBarcode((long)cell.getNumericCellValue());
                    }
                    else if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC && i == 3)
                    {
                        entry.setPrice(cell.getNumericCellValue());
                    }
                }
                else if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_STRING && (i == 1 || i == 2))
                {
                    if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_STRING && i == 1)
                    {
                        entry.setName(cell.getStringCellValue());
                    }
                    else if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_STRING && i == 2)
                    {
                        entry.setVendor(cell.getStringCellValue());
                    }
                }
                else
                {}
                entry.setCount(0);
            }
            try {
                if(entry.getBarcode() != 0) {
                    stmt.executeUpdate("insert into Inventory1 set name = '" + entry.getName() + "', vendor = '" + entry.getVendor() + "', count = " + entry.getCount() + ", barcode = " + entry.getBarcode() + ", price = " + entry.getPrice());
                }
                entry.emptyForImport();
            }
            catch (SQLIntegrityConstraintViolationException ex)
            {
                export(entry);
                entry.emptyForImport();
                multipleKeys++;
            }
        }
    }

    public void reset()
    {
        multipleKeys = 0;
    }

    public long getMultipleKeys()
    {
        return this.multipleKeys;
    }

    private EntryString es;

    public void checkData() throws Throwable {
        Row myrow;
        long errFlag = 0;
        for (int row = 1; row <= sheet.getLastRowNum(); row++) {
            myrow = sheet.getRow(row);
            for (int i = myrow.getFirstCellNum(); i < myrow.getLastCellNum(); i++) {
                Cell cell = myrow.getCell(i);
                if (myrow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC && (i == 0 || i == 3)) {}
                else if(myrow.getCell(i).getCellType() == Cell.CELL_TYPE_STRING && (i == 1 || i == 2)) {}
                else
                {
                    errFlag++;
                    String barcode = "";
                    String name = "";
                    String vendor = "";
                    String price = "";
                    for(int i1 = 0; i1 < 4; i1++) {
                        Cell cell1 = myrow.getCell(i1);
                        if (i1 == 0) {
                            try {
                                barcode = "" + (int)cell1.getNumericCellValue();
                            }
                            catch (IllegalStateException ex)
                            {
                                barcode = cell1.getStringCellValue();
                            }
                        } else if (i1 == 1) {
                            try {
                                name = cell1.getStringCellValue();
                            }
                            catch (IllegalStateException ex)
                            {
                                name = "" + cell1.getNumericCellValue();
                            }
                        } else if (i1 == 2) {
                            try {
                                vendor = cell1.getStringCellValue();
                            }
                            catch (IllegalStateException ex)
                            {
                                vendor = "" + cell1.getNumericCellValue();
                            }
                        } else if (i1 == 3) {
                            try {
                                price = "" + cell1.getNumericCellValue();
                            }
                            catch (IllegalStateException ex)
                            {
                                price = cell1.getStringCellValue();
                            }
                        }
                    }
                    es = new EntryString(barcode, name, vendor, price, "0");
                    export(es);
                    es.empty();
                    multipleKeys++;
                }
            }
        }
        if(errFlag > 0)
        {
            order = 2;
            alertPane.setText(errFlag + " improper data types in database.");

        }
        else if(errFlag == 0)
        {
            alertPane.setText("All Data Types Are Correct.");
        }
    }



    public void export(Entry entry) throws Throwable {
        Row row = getSh1().createRow((int)multipleKeys + 1);
        while(time < 1){
            System.out.println(time);
            int column = 0;
            Row row1 = sh1.createRow(0);
            while (column < 4)
            {
                Cell cellHeaders = row1.createCell(column);
                if (column == 0) {
                    cellHeaders.setCellValue("BARCODE");
                } else if (column == 1) {
                    cellHeaders.setCellValue("NAME");
                } else if (column == 2) {
                    cellHeaders.setCellValue("VENDOR");
                } else if (column == 3) {
                    cellHeaders.setCellValue("PRICE");
                }

                column++;
            }
            time++;
        }
        for(int cellnum = 0; cellnum < 4; cellnum++){
            Cell cell = row.createCell(cellnum);
            if(cellnum == 0) {
                cell.setCellValue(entry.getBarcode());
            }
            else if(cellnum == 1) {
                cell.setCellValue(entry.getName());
            }
            else if(cellnum == 2) {
                cell.setCellValue(entry.getVendor());
            }
            else if(cellnum == 3) {
                cell.setCellValue(entry.getPrice());
            }
        }
    }
    public void export(EntryString entry) throws Throwable {
        Row row = getSh1().createRow((int)multipleKeys + 1);
        while(time < 1){
            System.out.println(time);
            int column = 0;
            Row row1 = sh1.createRow(0);
            while (column < 4)
            {
                Cell cellHeaders = row1.createCell(column);
                if (column == 0) {
                    cellHeaders.setCellValue("BARCODE");
                } else if (column == 1) {
                    cellHeaders.setCellValue("NAME");
                } else if (column == 2) {
                    cellHeaders.setCellValue("VENDOR");
                } else if (column == 3) {
                    cellHeaders.setCellValue("PRICE");
                }

                column++;
            }
            time++;
        }
        for(int cellnum = 0; cellnum < 4; cellnum++){
            Cell cell = row.createCell(cellnum);
            if(cellnum == 0) {
                cell.setCellValue(entry.getBarcode());
            }
            else if(cellnum == 1) {
                cell.setCellValue(entry.getName());
            }
            else if(cellnum == 2) {
                cell.setCellValue(entry.getVendor());
            }
            else if(cellnum == 3) {
                cell.setCellValue(entry.getPrice());
            }
        }
    }

    public void close() throws IOException {
        FileProperty fp = new FileProperty();
        Controller cont = new Controller();
        FileOutputStream errOut = new FileOutputStream(fp.saveFile("Excel File (*.xlsx)", "*.xlsx", "Save Unimported items", "UnimportedItems.xlsx", cont.getPrimaryStage()));
        this.wb1.write(errOut);
        errOut.close();
    }

    public void clearSheet()
    {
        Sheet sheet = getWb().getSheetAt(0);
        for (Row row : sheet) {
            sheet.removeRow(row);
        }
    }

    public Sheet getSh1()
    {
        return this.sh1;
    }


    public static SXSSFWorkbook getWb()
    {
        return wb1;
    }

    public void resetTime()
    {
        time = 0;
    }

    private static SXSSFWorkbook wb1 = new SXSSFWorkbook(100);
    private static Sheet sh1 = getWb().createSheet();
    private static Row row1 = sh1.createRow(0);
    private static int time = 0;




}

