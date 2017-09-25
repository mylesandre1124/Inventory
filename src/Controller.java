import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Myles on 5/19/16.
 */

//Version 1.0

public class Controller {
    private double version = 1.0;
    private static Employee employee = new Employee();


    private static Statement stmt;
    private static Connection connection;
    private EntryProperty entryProperty;

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    /*public void checkVersion() throws SQLException
    {

        ResultSet newestVersion = loginS.executeQuery("select version from Versions where order = 1");
        while(newestVersion.next())
        {
            double nVersion = newestVersion.getDouble(1);
            if(nVersion > version)
            {
                *.setText("Software is not up to date");
            }
            else if(nVersion == version)
            {
                *.setText("Software is up to date");
            }
        }
    }*/

    @FXML
    private TableView<EntryProperty> entryTableView = new TableView<>();
    private EntryProperty entryForTable = new EntryProperty();


    @FXML
    private TableColumn barcodeCol = new TableColumn("Barcode");
    @FXML
    private TableColumn countCol = new TableColumn("Count");
    @FXML
    private TableColumn nameCol = new TableColumn("Name");
    @FXML
    private TableColumn vendorCol = new TableColumn("Vendor");
    @FXML
    private TableColumn priceCol = new TableColumn("Price");


    private Entry entry1 = new Entry();

    private EmployeeTableEntry empEntry = new EmployeeTableEntry();
    @FXML
    private TableView<EmployeeTableEntry> empEntryTableView = new TableView<>();
    ObservableList<EmployeeTableEntry> empList = FXCollections.observableArrayList();

    public void populateEmployeeTable() throws SQLException, IOException, ClassNotFoundException {
        empEntryTableView.getItems().clear();
        intLoginDatabase();
        checkEmpLoggedIn();
        String name;
        String username;
        int empId;
        String password = "";
        String admin = "";
        int adminInt;

        ResultSet empEntrySet = loginS.executeQuery("select * from Employee");
        while(empEntrySet.next()) {
            name = empEntrySet.getString(1);
            empId = empEntrySet.getInt(2);
            username = empEntrySet.getString(3);
            String prePassword = empEntrySet.getString(4);
            adminInt = empEntrySet.getInt(5);
            if (empId == employee.getEmpID()){
                password = prePassword;
            }
            else if(empId != employee.getEmpID())
            {
                int stars = ((int)Math.random() * 5) + 7;
                while(stars >= 0)
                {
                    password += "*";
                    stars--;
                }
            }
            if (adminInt == 1) {
                admin = "Yes";
            } else if (adminInt == 0) {
                admin = "No";
            }

            SimpleStringProperty nameP = new SimpleStringProperty();
            SimpleIntegerProperty empIdP = new SimpleIntegerProperty();
            SimpleStringProperty usernameP = new SimpleStringProperty();
            SimpleStringProperty passwordP = new SimpleStringProperty();
            SimpleStringProperty adminP = new SimpleStringProperty();

            nameP.set(name);
            empIdP.set(empId);
            usernameP.set(username);
            passwordP.set(password);
            adminP.set(admin);

            password = "";
            empEntry.setAdmin(admin);

            empList.add(new EmployeeTableEntry(nameP, empIdP, usernameP, passwordP, adminP));
        }


        empNameCol.setCellValueFactory(
                new PropertyValueFactory<EmployeeTableEntry, String>("name")
        );
        empEmpIDCol.setCellValueFactory(
                new PropertyValueFactory<EmployeeTableEntry, String>("empId")
        );
        empUsernameCol.setCellValueFactory(
                new PropertyValueFactory<EmployeeTableEntry, String>("username")
        );
        empPasswordCol.setCellValueFactory(
                new PropertyValueFactory<EmployeeTableEntry, String>("password")
        );
        empAdminCol.setCellValueFactory(
                new PropertyValueFactory<EmployeeTableEntry, String>("admin")
        );
        empEntryTableView.setItems(empList);
    }

    @FXML
    private TableColumn empNameCol = new TableColumn("Name");
    @FXML
    private TableColumn empEmpIDCol = new TableColumn("Emp ID#");
    @FXML
    private TableColumn empUsernameCol = new TableColumn("Username");
    @FXML
    private TableColumn empPasswordCol = new TableColumn("Password");
    @FXML
    private TableColumn empAdminCol = new TableColumn("Admin?");

    public void empTableDialog() throws IOException, ClassNotFoundException, SQLException {
        cd.showDialog("empTable.fxml", " Employee List");

    }

    ObservableList<EntryProperty> entryList = FXCollections.observableArrayList();

    public void populateTable(long barcode) throws SQLException, IOException, ClassNotFoundException {
        checkLoggedIn();
        int count = 0;
        double price = 0.0;
        String name = "";
        String vendor = "";
        ResultSet entryItems = stmt.executeQuery("select count, price, name, vendor from Inventory1 where barcode = " + barcode);
        while(entryItems.next())
        {
            count = entryItems.getInt(1);
            price = entryItems.getDouble(2);
            name = entryItems.getString(3);
            vendor = entryItems.getString(4);
        }
        entryForTable.setBarcode(barcode);
        entryForTable.setCount(count);
        entryForTable.setPrice(price);
        entryForTable.setName(name);
        entryForTable.setVendor(vendor);

        barcodeCol.setCellValueFactory(
                new PropertyValueFactory<EntryProperty, String>("barcode")
        );
        countCol.setCellValueFactory(
                new PropertyValueFactory<EntryProperty,String>("count")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<EntryProperty,String>("name")
        );
        vendorCol.setCellValueFactory(
                new PropertyValueFactory<EntryProperty,String>("vendor")
        );
        priceCol.setCellValueFactory(
                new PropertyValueFactory<EntryProperty,String>("price")
        );
        entryList.clear();
        entryList.add(entryForTable);
        entryTableView.setItems(entryList);
    }

    private static Statement loginS;
    private static Connection loginC;

    private static int setCount = 0;

    @FXML
    private Button delOk = new Button();

    @FXML
    private TextField delUserField = new TextField();

    @FXML
    private Label alertDel = new Label();

    @FXML
    private Button loginClose = new Button();

    @FXML
    private Button loginCancel = new Button();

    @FXML
    private Label exception = new Label();

    @FXML
    private TextField barcodeField;

    @FXML
    private Label sure = new Label("");
    @FXML
    private Label displayCount = new Label("");

    @FXML
    private TextField countField = new TextField("0");


    @FXML
    private TextField username = new TextField("");
    @FXML
    private PasswordField password = new PasswordField();

    //Radio Buttons
    @FXML
    private RadioButton add = new RadioButton();
    @FXML
    private RadioButton sub = new RadioButton();
    @FXML
    private RadioButton set = new RadioButton();

    @FXML
    private Button close = new Button();

    @FXML
    private Button editSave = new Button();

    @FXML
    private Button addDupeCancel = new Button();
    @FXML
    private Button addDupeUpdate = new Button();

    @FXML
    private Button closeSure = new Button();

    @FXML
    private Button closeSureYes = new Button();

    @FXML
    private Button setClose = new Button();

    @FXML
    private TextField name = new TextField();
    @FXML
    private TextField vendor = new TextField();
    @FXML
    private TextField price = new TextField();
    @FXML
    private TextField countAdd = new TextField();
    @FXML
    private TextField barcode = new TextField();
    @FXML
    private TextField editName = new TextField();
    @FXML
    private TextField editVendor = new TextField();
    @FXML
    private TextField editPrice = new TextField();
    @FXML
    private TextField editCountAdd = new TextField();
    @FXML
    private TextField editBarcode = new TextField();
    @FXML
    private Button addOk = new Button();
    @FXML
    private Button addCancelB = new Button();

    @FXML
    private TextField delBarcode = new TextField();

    @FXML
    private ProgressIndicator ind = new ProgressIndicator();

    @FXML
    private TextField editSearchBarcode = new TextField();

    @FXML
    private Label alertLabelPane = new Label();

    //Flags
    private int state = 0;

    private static long searchEditBarcode = 0;

    private static boolean yesClicked;

    //Global variables
    private static int empId;
    private static int count;
    private static String empName;

    private static int increment = 0;

    private static Database db;

    private static void intDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        increment = 1;
        if(address.equals(""))
        {

        }
        else {
            connection = DriverManager.getConnection("jdbc:mysql://" + address + "/Inventory1" + "?useSSL=false", hostnameLogin, passwordLogin);
            increment = 2;
            stmt = connection.createStatement();
            db = new Database(stmt, connection);
        }
    }

    public void checkInc()
    {
        if(increment == 1)
        {
            ind.setProgress(.5);
            alertLabelPane.setText("Driver Loaded");
        }
        else if(increment == 2)
        {
            ind.setProgress(1.0);
            alertLabelPane.setText("Database Connected");
        }
        else
        {
            ind.setProgress(0.0);
            alertLabelPane.setText("Unable To Connect To Database");
        }
        increment = 0;
    }


    @FXML
    private TextField hostname = new TextField();
    @FXML
    private PasswordField sqlPassword = new PasswordField();
    @FXML
    private Button sqlLoginButton = new Button();

    private static String hostnameLogin = "root";
    private static String passwordLogin = "";

    @FXML
    private Label authSQLPassword = new Label();



    public void retrySQLDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("sqlLogin.fxml", "Cloud Login");
    }



    public void sqlException() throws ClassNotFoundException, SQLException, IOException
    {
        try {
            intDatabase();
            loggedIn = true;
        }
        catch (SQLException ex)
        {
            retrySQLDialog();
        }
    }

    public void retryLogin() throws ClassNotFoundException
    {
        hostnameLogin = hostname.getText();
        passwordLogin = sqlPassword.getText();
        try {
            intDatabase();
            loggedIn = true;
            Stage stage = (Stage) sqlLoginButton.getScene().getWindow();
            stage.close();
        }
        catch (SQLException ex)
        {
            authSQLPassword.setText("Login Information Incorrect. Retry.");
        }

    }

    private boolean loggedIn = false;
    public void checkLoggedIn() throws IOException, SQLException, ClassNotFoundException
    {
        while(loggedIn == false) {
            sqlException();
            checkInc();
        }
        if(loggedIn == true)
        {
            sqlException();
            checkInc();
        }
    }

    public void checkEmpLoggedIn() throws IOException, ClassNotFoundException {
        while (employee.getEmpID() == 0 || employee.getEmpID() == -1) {
            loginDisplay();
        }
    }



    public void incrementStock() throws SQLException, ClassNotFoundException, IOException {
        checkLoggedIn();
        checkEmpLoggedIn();
        Database db = new Database(stmt, connection);
        int barcode = returnBarcode();
        if(checkIfBarcodeExistsInDB(barcode)){
            Count count = new Count(employee);
            Controller.empId = count.getCountDb(db, barcode);
            Controller.count = count.getCount();
            boolean isYesClicked = checkEmpId();
            if (isYesClicked) {
                count.add();
                displayCount.setText("" + count.getCount());
                count.updateCount(db, barcode);
            } else {
            }
            System.out.println(count.getCount());
        }
        else if(!checkIfBarcodeExistsInDB(barcode))
        {
            alertLabelPane.setText("Barcode: " + barcode + " not found.");
        }
        populateTable(barcode);
    }

    public void decrementStock() throws SQLException, ClassNotFoundException, IOException {
        checkLoggedIn();
        checkEmpLoggedIn();
        Database db = new Database(stmt, connection);
        int barcode = returnBarcode();
        if(checkIfBarcodeExistsInDB(barcode)) {
            Count count = new Count(employee);
            Controller.empId = count.getCountDb(db, barcode);
            Controller.count = count.getCount();
            boolean isYesClicked = checkEmpId();
            if (isYesClicked) {
                count.subtract();
                displayCount.setText("" + count.getCount());
                count.updateCount(db, barcode);
            } else {
            }
            System.out.println(count.getCount());
        }
        else if(!checkIfBarcodeExistsInDB(barcode))
        {
            alertLabelPane.setText("Barcode: " + barcode + " not found.");
        }
        populateTable(barcode);
    }

    /**
     Checks to see if barcode is in the database
     @return boolean - If barcode is in database then it returns true. If not, false.
     @param barcode The barcode that is being checked
     @throws
     */
    public boolean checkIfBarcodeExistsInDB(int barcode) throws SQLException, IOException, ClassNotFoundException {
        checkLoggedIn();
        checkEmpLoggedIn();
        int zeroOrOne = -1;
        boolean bool = false;
        ResultSet test = stmt.executeQuery("select exists(select 1 from Inventory1 where barcode = " + barcode + ")");
        while(test.next())
        {
            zeroOrOne = test.getInt(1);
        }
        if(zeroOrOne == 1)
        {
            bool = true;
            return bool;
        }
        else if(zeroOrOne == 0)
        {
            bool = false;
            barcodeNotFound(barcode);
            return bool;
        }
        return bool;
    }

    /**
     Method updates the count of a certain when a user enters a count of that item
     @return void
     */
    public void setCount() throws SQLException, ClassNotFoundException, IOException {
        checkLoggedIn();
        checkEmpLoggedIn();
        int barcode = returnBarcode();
        if(checkIfBarcodeExistsInDB(barcode))
        {
            setDisplay();
            int count = setCount;
            Count count1 = new Count(employee);
            Controller.empId = count1.getCountDb(db, barcode);
            Controller.count = count1.getCount();
            boolean isYesClicked = checkEmpId();
            if (isYesClicked) {
                count1.setCount(count);
                displayCount.setText("" + count);
                count1.updateCount(db, barcode);
            } else {
            }
            System.out.println(count);
        }
        else if(!checkIfBarcodeExistsInDB(barcode)) {
            alertLabelPane.setText("Barcode: " + barcode + " not found.");
        }
        populateTable(barcode);

    }

    /**
     Method exports barcodes that are not found in the database into a buffered file to be exported into an excel file later
     @param barcode
     */
    public void barcodeNotFound(long barcode) {
        FileProperty fpout = new FileProperty();
        FileProperty fpin = new FileProperty();
        fpout.setFile(new File("src"+ File.separator+"internals"+ File.separator+"BarcodeNotFound.bnf"));
        ArrayList<Barcode> barcodeArrayList = new ArrayList<>();
        if(fpout.fileIsEmpty() || !fpout.getFile().exists()) {
            barcodeArrayList.add(new Barcode(barcode));
            ObjectOutputStream out = fpout.save(new File("src" +File.separator + "internals" +File.separator+"BarcodeNotFound.bnf"));
            try {
                out.writeObject(barcodeArrayList);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!fpout.fileIsEmpty())
        {
            ObjectInputStream in = fpin.open(new File("src" + File.separator+ "internals" + File.separator + "BarcodeNotFound.bnf"));
            try {
                barcodeArrayList = (ArrayList<Barcode>) in.readObject();
                boolean inList = checkBarcodes(barcodeArrayList, barcode);
                if(!inList) {
                    barcodeArrayList.add(new Barcode(barcode));
                    File file = new File("src" + File.separator + "internals" + File.separator + "BarcodeNotFound.bnf");
                    file.delete();
                    ObjectOutputStream out = fpout.save(new File("src"+ File.separator +"internals" + File.separator + "BarcodeNotFound.bnf"));
                    out.writeObject(barcodeArrayList);
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks to see if a barcode has already not been found in the database and returns a boolean
     * @param barcodeArrayList
     * @param barcode
     * @return
     */
    public boolean checkBarcodes(ArrayList<Barcode> barcodeArrayList, long barcode)
    {
        boolean inList = false;
        for (int i = 0; i < barcodeArrayList.size(); i++) {
            if(barcodeArrayList.get(i).getBarcode() == barcode)
            {
                inList = true;
                return inList;
            }
            else if (barcodeArrayList.get(i).getBarcode() != barcode)
            {
                inList = false;
            }
        }
        return inList;
    }

    public void exportNotFound()
    {
        FileProperty fp = new FileProperty();
        File file = fp.saveFile("Barcode Not Found excel file (*.xlsx)", "*.xlsx", "Export Barcodes Not Found", "BarcodeNotFound.xlsx", getPrimaryStage());
        try {
            file.createNewFile();
            XSSFWorkbook workbook = new XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
            String[] names = {"BARCODE", "NAME", "VENDOR", "PRICE"};
            Row row1 = sheet.createRow(0);
            for (int i = 0; i < 4; i++){
                Cell cell1 = row1.createCell(i);
                cell1.setCellValue(names[i]);
            }
            FileProperty fpin = new FileProperty();
            ObjectInputStream in = fpin.open(new File("src" + File.separator +"internals"+ File.separator +"BarcodeNotFound.bnf"));
            ArrayList<Barcode> barcodeArrayList = (ArrayList<Barcode>) in.readObject();
            for (int i = 0; i < barcodeArrayList.size(); i++) {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                Cell cell= row.createCell(0);
                cell.setCellValue("" + barcodeArrayList.get(i).getBarcode());
            }
            FileOutputStream fout= new FileOutputStream(file);
            workbook.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addSubSet() throws IOException, SQLException, ClassNotFoundException {
        int state = this.state;
        if (state == 1) {
            if (employee.getEmpID() == -1|| employee.getEmpID() == 0 ) {
                loginDisplay();
            }
            incrementStock();
        } else if (state == 2) {
            if (employee.getEmpID() == -1 || employee.getEmpID() == 0 ) {
                loginDisplay();
            }
            decrementStock();
        } else if (state == 3) {
            if (employee.getEmpID() == -1 || employee.getEmpID() == 0 ) {
                loginDisplay();
            }
            setCount();
        }
    }

    public void clearBarcodes()
    {
        File file = new File("src" + File.separator + "internals" + File.separator + "BarcodeNotFound.bnf");
        file.delete();
    }

    public void addCall() {
        this.state = 1;
        sub.setSelected(false);
        set.setSelected(false);
    }

    public void subCall() {
        this.state = 2;
        add.setSelected(false);
        set.setSelected(false);
    }

    public void setCall() {
        this.state = 3;
        add.setSelected(false);
        sub.setSelected(false);
    }


    public int returnBarcode() {
        int barcode = Integer.parseInt(barcodeField.getText());
        barcodeField.setText("");
        return barcode;
    }

    public int getCount() {
        return count;
    }

    public int getEmpId() {
        return empId;
    }

    public Employee getEmployee() {
        return employee;
    }


    public void yesClicked() {
        yesClicked = true;
        Stage stage = (Stage) closeSureYes.getScene().getWindow();
        stage.close();
    }

    public void noClicked() {
        yesClicked = false;
        Stage stage = (Stage) closeSure.getScene().getWindow();
        stage.close();
    }

    private static Label sureMessage = new Label();

    @FXML
    public boolean checkEmpId() throws IOException, ClassNotFoundException, SQLException {
        intLoginDatabase();
        empId = getEmpId();
        count = getCount();
        employee = getEmployee();
        if (empId == 0) {
            return true;
        } else if (empId != employee.getEmpID() && empId != 0) {
            ResultSet set = loginS.executeQuery("select name from Employee where empId = " + empId);
            while(set.next())
            {
                empName = set.getString(1);
            }
            //TODO: Figure out application threading for alert
            sureMessage.setText("User: " + empName + " has already scanned this item with a count of " + count);
            StringProperty valueProperty = new SimpleStringProperty("User: " + empName + " has already scanned this item with a count of " + count);
            sure.textProperty().bind(valueProperty);
            //sureBt.fire();
            System.out.println(sure.getText());
            display();
            if (isYesClicked()) {
                yesClicked = false;
                return true;
            } else if (!isYesClicked()) {
                yesClicked = false;
                return false;
            }
        } else if (empId == employee.getEmpID()) {
            return true;
        }
        return false;
    }
    @FXML
    Button sureBt = new Button();

    public void lastDitch(){
        sure.setText("User: " + empName + " has already scanned this item with a count of " + count);
    }

    public void display() throws IOException, ClassNotFoundException {
        sureBt.setVisible(false);
        cd.showDialog("sureDialog.fxml", "Are You Sure?");
    }

    public void setDisplay() throws IOException, ClassNotFoundException {
        cd.showDialog("setCount.fxml", "Set Count");

    }

    public boolean isYesClicked() {
        return yesClicked;
    }


    @FXML
    public void getSetCount() {
        int count1 = Integer.parseInt(countField.getText());
        setCount = count1;
        Stage stage = (Stage) setClose.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void login() throws SQLException, ClassNotFoundException {
        intLoginDatabase();
        Employee emp = new Employee();
        Database db = new Database();
        db.connectEmployeeDatabase();
        String username1 = username.getText();
        String password1 = password.getText();
        String password2 = "";
        String name = "";
        int admin = -1;
        int empId = 0;
        int zeroOrOne = -1;
        ResultSet userTest = db.query("SELECT EXISTS(SELECT 1 FROM Employee.Employee WHERE username = '" + username1 + "')");
        while (userTest.next()) {
            zeroOrOne = userTest.getInt(1);
        }
        if (zeroOrOne == 1) {
            ResultSet loginSet = db.query
                    ("select empId, password, name, admin from Employee where username = '" + username1 + "'");
            while (loginSet.next()) {
                empId = loginSet.getInt(1);
                password2 = loginSet.getString(2);
                name = loginSet.getString(3);
                admin = loginSet.getInt(4);
            }
        } else if (zeroOrOne == 0) {
            exception.setText("Username not found");
        }
        if (password2.toLowerCase().equals(password1.toLowerCase())) {
            emp.setEmpID(empId);
            emp.setName(name);
            emp.setAdmin(admin);
            exception.setText("Success.");
            Stage stage = (Stage) loginClose.getScene().getWindow();
            stage.close();

        } else if (password2.equals("") || password2.equals("null")) {
        } else if (!password2.equals(password1)) {
            exception.setText("Password is incorrect");
        }

        employee = emp;
        if(empChecked())
        {
            saveDefaultEmployeeProfile();
        }

    }

    private static int timer = 5;

    public static void intLoginDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            loginC = DriverManager.getConnection("jdbc:mysql://" + address + "/Employee" + "?useSSL=false", hostnameLogin, passwordLogin);
            loginS = loginC.createStatement();
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex)
        {
            System.out.println("Connection Refused");
        }
    }

    @FXML
    public void loginDisplay() throws IOException, ClassNotFoundException {
        cd.showDialog("login.fxml", "Login");
    }

    public void logout() {
        employee.logout();
        alertLabelPane.setText("Logged Out.");
    }


    public void closeSetButton() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    public void loginClose() {
        Stage stage = (Stage) loginCancel.getScene().getWindow();
        stage.close();
    }

    public void addItem() throws IOException, ClassNotFoundException {
        cd.showDialog("addItem.fxml", "Add Item");

    }

    public void addItemDetails() throws ClassNotFoundException, SQLException, IOException {
        entry1.setName(name.getText());
        entry1.setVendor(vendor.getText());
        entry1.setPrice(Double.parseDouble(price.getText()));
        entry1.setCount(Integer.parseInt(countAdd.getText()));
        entry1.setBarcode(Long.parseLong(barcode.getText()));
        checkLoggedIn();
        try {
            Database db = new Database();
            entry1.addEntry(db, employee);
        } catch (SQLIntegrityConstraintViolationException e) {
            duplicateKeyDisplay();
        }
        Stage stage = (Stage) addOk.getScene().getWindow();
        stage.close();
    }

    public void addCancel() {
        Stage stage = (Stage) addCancelB.getScene().getWindow();
        stage.close();
    }

    public void duplicateKeyDisplay() throws IOException, ClassNotFoundException {
        cd.showDialog("duplicateKey.fxml", "Duplicate Key Entered");
    }

    public void addDuplicateUpdate() throws SQLException
    {
        stmt.executeUpdate("update Inventory1 set name = '" + entry1.getName() + "', vendor = '" +
                entry1.getVendor() + "', price = " + entry1.getPrice() + ", count = "
                + entry1.getCount() + ", empId = " + employee.getEmpID() + " where barcode = " + entry1.getBarcode());
        Stage stage = (Stage) addDupeUpdate.getScene().getWindow();
        stage.close();
    }

    public void addDuplicateCancel()
    {
        Stage stage = (Stage) addDupeCancel.getScene().getWindow();
        stage.close();
    }

    public void deleteRow() throws SQLException, ClassNotFoundException, IOException
    {
        checkLoggedIn();
        long barcode = 0;
        //try {
            barcode = Long.parseLong(delBarcode.getText());
            stmt.executeUpdate("DELETE FROM Inventory1 where barcode = " + barcode);
        //}
        /*catch (NumberFormatException nf)
        {
            //*.setText("Barcode can't contain numbers");
        }*/

    }

    public void openDeleteDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("delEntry.fxml", "Delete Entry");


    }

    public void editItem() throws ClassNotFoundException, SQLException, IOException
    {
        checkLoggedIn();
        checkEmpLoggedIn();
        long barcode = searchEditBarcode;
        Database db = new Database(stmt, connection);
        entry1.setBarcode(barcode);
        ResultSet itemDetails = entry1.getEntrySet(db);
        while (itemDetails.next())
        {
            entry1.setName(itemDetails.getString(1));
            entry1.setVendor(itemDetails.getString(2));
            entry1.setPrice(itemDetails.getDouble(3));
            entry1.setCount(itemDetails.getInt(4));
        }


        editName.setText(entry1.getName());
        editVendor.setText(entry1.getVendor());
        editPrice.setText(entry1.getPrice() + "");
        editCountAdd.setText(entry1.getCount() + "");
        System.out.println(editBarcode.getText());

    }

    public void editItemSave() throws ClassNotFoundException, SQLException, IOException {
        entry1.setName(editName.getText());
        entry1.setVendor(editVendor.getText());
        entry1.setPrice(Double.parseDouble(editPrice.getText()));
        entry1.setCount(Integer.parseInt(editCountAdd.getText()));
        entry1.setBarcode(Long.parseLong(editSearchBarcode.getText()));
        checkLoggedIn();
        checkEmpLoggedIn();
        Database db = new Database(stmt, connection);

        entry1.editEntry(db, employee.getEmpID());

        Stage stage = (Stage) editSave.getScene().getWindow();
        stage.close();
    }


    public void editItemDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("editItem.fxml", "Edit Item");
    }



    @FXML
    private Button editSearchOk = new Button();

    public void getEditItemBarcode() throws SQLException, IOException, ClassNotFoundException {
        searchEditBarcode = Long.parseLong(editSearchBarcode.getText());
        editItem();

    }

    @FXML
    private Label registerExceptionHandle = new Label();
    @FXML
    private TextField registerName = new TextField();
    @FXML
    private TextField registerEmpId = new TextField();
    @FXML
    private TextField registerUsername = new TextField();
    @FXML
    private TextField registerPassword1 = new TextField();
    @FXML
    private TextField registerPassword2 = new TextField();
    @FXML
    private Button registerOk = new Button();


    /**
     * Registers a new Employee in the database. Gets the fields and inserts them into the database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void registerNewEmployee() throws SQLException, ClassNotFoundException {
        registerExceptionHandle.setText("");
        intLoginDatabase();
        Database db = new Database(loginS, loginC);
        String name = registerName.getText();
        Employee employee = new Employee();
        int empId = 0;
        try {
            String preset = registerEmpId.getText();
            if(preset.equals(""))
            {
                registerExceptionHandle.setText("Employee ID# must be entered");
            }
            else if(!preset.equals("")) {
                empId = Integer.parseInt(preset);
                String username = registerUsername.getText();
                String password1 = registerPassword1.getText();
                String password2 = registerPassword2.getText();
                int exists;
                if (password1.equals(password2)) {
                    exists = employee.checkEmployeeExists(db, empId);
                    if (exists == 0) {
                        employee.setEmpID(empId);
                        employee.setUsername(username);
                        employee.setName(name);
                        employee.createNewEmployee(db, password1);
                        Stage stage = (Stage) registerOk.getScene().getWindow();
                        stage.close();
                    } else if (exists == 1) {
                        registerExceptionHandle.setText("Employee ID# already exists. Retry");
                    }
                } else if (!password1.equals(password2)) {
                    registerExceptionHandle.setText("Passwords not the same. Retry.");
                }
            }
        }
        catch (NumberFormatException nf)
        {
            registerExceptionHandle.setText("Employee ID# can only have numbers.");
        }

    }

    public void registerDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("registerUser.fxml", "Register User");

    }
    @FXML
    private Button resetBt = new Button();

    public void resetInventoryCount() throws SQLException, IOException, ClassNotFoundException {
        checkLoggedIn();
        if(employee.isAdmin())
        {
            stmt.executeUpdate("update Inventory1 set count = 0 where !isnull(count);");
            Stage stage = (Stage) resetBt.getScene().getWindow();
            stage.close();
        }
    }


    public void resetCountDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("resetCount.fxml", "Reset Inventory");

    }


    public void deleteUser() throws SQLException, ClassNotFoundException, IOException {
        intLoginDatabase();
        checkEmpLoggedIn();
        Database db = new Database(loginS, loginC);
        Controls controls = new Controls(delOk, alertDel);
        Employee employee1 = new Employee();
        employee1.deleteEmployee(delUserField.getText(), db, employee, controls);
    }

    public void deleteUserDialog() throws IOException, ClassNotFoundException {
        cd.showDialog("delUser.fxml", "Delete User");
    }

    public void importData() throws SQLException, IOException, ClassNotFoundException {
        checkLoggedIn();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        try {
            File file = fileChooser.showOpenDialog(getPrimaryStage());
            Excel rex = new Excel(file, stmt, connection);
            rex.setAlertPane(alertLabelPane);
            rex.orderOfOperations();
            //alertLabelPane.setText("Success");
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public void exportAll() throws Throwable {
        checkLoggedIn();
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet();
        ResultSet set = stmt.executeQuery("select name, barcode, count, vendor, price from Inventory1 where barcode is not null");
        for(int rownum = 0; set.next(); rownum++){
            Row row = sh.createRow(rownum);
            for(int cellnum = 0; cellnum < 5; cellnum++){
                Cell cell = row.createCell(cellnum);
                int setIndex = 1 + cellnum;
                cell.setCellValue(set.getString(setIndex));
            }

        }
        FileChooser export = new FileChooser();
        export.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File (*.xlsx)", "*.xlsx"));
        File exportFile = export.showSaveDialog(getPrimaryStage());
        try{
            FileOutputStream out = new FileOutputStream(exportFile);
            wb.write(out);
            out.close();
            // dispose of temporary files backing this workbook on disk
            wb.dispose();
            alertLabelPane.setText("Successfully exported to excel. Opening file...");
            openFile(exportFile);
        }
        catch(NullPointerException ex)
        {
            setAlertText("No File Chosen.");
        }
    }



    public void saveUserProfile() throws IOException {
        FileChooser save = new FileChooser();
        save.setTitle("Save User Profile");
        save.getExtensionFilters().add(new FileChooser.ExtensionFilter("Inventory Counter Profile file (*.icp)", "*.icp"));
        try {
            File profile = save.showSaveDialog(getPrimaryStage());
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(profile)));

            out.writeObject(employee);
            out.close();
            alertLabelPane.setText("Successful Save as " + employee.getName());
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
        }


    }

    public void openUserProfile() throws IOException, ClassNotFoundException {
        try{
            FileProperty fp = new FileProperty(getPrimaryStage());
            ObjectInputStream in = fp.open("Inventory Counter Profile files (*.icp)", "*.icp", "Open User Profile");

            employee = (Employee)(in.readObject());

            in.close();
            alertLabelPane.setText("Successful Login as " + employee.getName());
        }
        catch (NullPointerException npe)
        {
            alertLabelPane.setText("No File Chosen.");
        }
        catch (EOFException eofEx)
        {
        }

    }



    public Label getAlertLabelPane()
    {
        return alertLabelPane;
    }

    public Stage getPrimaryStage()
    {
        Stage stage = (Stage) main.getScene().getWindow();
        return stage;
    }

    public void closeMain()
    {
        StageClose sc = new StageClose(getPrimaryStage());
        sc.closeStage();
    }

    public void saveDatabaseBackup() throws SQLException, ClassNotFoundException, IOException {
        checkLoggedIn();
        ResultSet backup = stmt.executeQuery("select * from Inventory1");
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Inventory Database Backup file (*.idb)", "*.idb"));
        choose.setTitle("Save Database Backup");
        long barcode;
        String name;
        int count;
        String vendor;
        double price;
        int empId;
        ObjectOutputStream out;
        boolean success = false;
        File saveBackup;
        try {
            saveBackup = choose.showSaveDialog(getPrimaryStage());
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(saveBackup)));
            while (backup.next())
            {
                barcode = (long)backup.getInt(1);
                name = backup.getString(2);
                count = backup.getInt(3);
                vendor = backup.getString(4);
                price = backup.getDouble(5);
                empId = backup.getInt(6);
                if(name == null)
                {
                    name = "";
                }
                if(vendor == null)
                {
                    vendor = "";
                }
                out.writeLong(new Long(barcode));
                out.writeUTF(new String(name));
                out.writeInt(new Integer(count));
                out.writeUTF(new String(vendor));
                out.writeDouble(new Double(price));
                out.writeInt(new Integer(empId));

            }
            out.close();
            success = true;
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(success)
        {
            alertLabelPane.setText("Backup Database Saved to File.");
        }
    }

    public void openDatabaseBackup() throws SQLException, ClassNotFoundException, IOException {
        checkLoggedIn();
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Inventory Database Backup files (*.idb)", "*.idb"));
        open.setTitle("Open Database Backup");
        long barcode;
        String name;
        int count;
        String vendor;
        double price;
        int empId;
        ObjectInputStream in;
        boolean success = false;
        File openBackup;
        long duplicateKeyCount = 0;
        try {
            openBackup = open.showOpenDialog(getPrimaryStage());
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(openBackup)));
            while (in.available() != -1)
            {
                barcode = in.readLong();
                name = in.readUTF();
                count = in.readInt();
                vendor = in.readUTF();
                price = in.readDouble();
                empId = in.readInt();
                stmt.executeUpdate("insert into Inventory1 (barcode, name, count, vendor, price, empId) values(" + barcode + ", '" + name + "', " + count + ", '" + vendor + "', " + price + ", " + empId + ")");
            }
            in.close();
            success = true;
        }
        catch (MySQLIntegrityConstraintViolationException ex)
        {
            duplicateKeyCount += 1;
            setAlertText(duplicateKeyCount + " Duplicate Keys Found");
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
            success = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        }
        catch (EOFException ex )
        {
            success = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        if(success)
        {
            alertLabelPane.setText("Backup Database Uploaded to Server.");
        }
        else if(!success)
        {}
        success = false;
    }

    @FXML
    VBox main = new VBox();

    public void setAlertText(String message)
    {
        alertLabelPane.setText(message);
    }

    public void clearAlertText()
    {
        alertLabelPane.setText("");
    }

    public void openFile(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    @FXML
    TextField serverAddress = new TextField();
    static String address = new String();

    public void connectToServer() throws SQLException, ClassNotFoundException, IOException {
        address = serverAddress.getText();
        if(checkVConnect) {
            checkLoggedIn();
            StageClose serverClose = new StageClose(connectServerBt);
            serverClose.closeButton();
        }
    }

    public void showConnectDialog() throws IOException {
        CreateDialog cd = new CreateDialog("HostnameDialog", "Connect To Server");
        cd.showDialog();
    }


    @FXML
    Button connectServerBt = new Button();

    @FXML
    Button serverCloseBt = new Button();

    public void closeConnectToServer()
    {
        StageClose serverClose = new StageClose(serverCloseBt);
        serverClose.closeButton();

    }

    public void serverToDb() throws IOException, SQLException, ClassNotFoundException {
        showConnectDialog();
        createDatabase();
    }

    private static void connectNewDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        newDBConnection = DriverManager.getConnection("jdbc:mysql://" + address , hostnameLogin, passwordLogin);
        newDB = newDBConnection.createStatement();
    }

    private static Connection newDBConnection;
    private static Statement newDB;
    public void createDatabase() throws IOException, ClassNotFoundException {
        //connectNewDatabase();
        Database database = new Database();
        try {
            boolean exists = database.checkDbExists("Inventory1");
            if(!exists) {
                database.createInventoryDatabase();
            }
            else {
                setAlertText("Inventory1 Database already exists");
            }
        }
        catch (NullPointerException nex)
        {}

    }

    private CreateDialog cd = new CreateDialog();



    public void checkServer() throws SQLException, IOException, ClassNotFoundException {
        //Connection Established
        checkVConnect = false;
        connectToServer();
        checkVConnect = true;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + address, hostnameLogin, passwordLogin);
            connectEst.setProgress(1.0);
        } catch (SQLException e) {
            alertLabelPane.setText("Connection Failed");
        }
        //Inventory Check
        intDatabase();
        ResultSet invFields = stmt.executeQuery("Select column_name from Information_Schema.columns Where Table_Name = 'Inventory1'");
        boolean status = true;
        String print = "";
        while (invFields.next())
        {
            print += invFields.getString(1);
        }
        if(!print.equals("barcodenamecountvendorpriceempId"))
        {
            status = false;
        }
        if(status)
        {
            inventory1.setProgress(1.0);
        }
        //Employee Check
        intLoginDatabase();
        ResultSet empFields = loginS.executeQuery("Select column_name from Information_Schema.Columns Where Table_Name = 'Employee'");
        boolean status1 = true;
        String printEmp = "";
        while (empFields.next())
        {
            printEmp += empFields.getString(1);
        }
        if(!printEmp.equals("nameempIdusernamepasswordadmin"))
        {
            status1 = false;
        }
        if(status1)
        {
            employeeDb.setProgress(1.0);
        }
    }
    @FXML
    ProgressIndicator connectEst = new ProgressIndicator();
    @FXML
    ProgressIndicator inventory1 = new ProgressIndicator();
    @FXML
    ProgressIndicator employeeDb = new ProgressIndicator();



    private boolean checkVConnect = true;

    public void serverStatusDialog() throws IOException {
        cd.showDialog("CheckServer.fxml","Server Status Check");
    }



    public void saveDefaultServerCredentials()
    {
        creds.setHostname(hostnameLogin);
        creds.setPassword(passwordLogin);
        creds.setAddress(address);
        creds.saveProfile();
    }

    ServerCredential creds = new ServerCredential();

    public void defaultCredentialLoader() throws ClassNotFoundException {
        //creds.saveProfile(creds);
        //creds.openProfile();
        hostnameLogin = creds.getHostname();
        passwordLogin = creds.getPassword();
        address = creds.getAddress();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + address, hostnameLogin, passwordLogin);
            ind.setProgress(1.0);
            alertLabelPane.setText("Connection to Server Successful");
        } catch (SQLException e) {
            alertLabelPane.setText("Connection Failed");
        }
    }

    @FXML
    Button serverCheckOk = new Button();

    public void serverCheckClose()
    {
        StageClose sc = new StageClose(serverCheckOk);
        sc.closeButton();
    }



    @FXML
    CheckBox employeeCheckBox = new CheckBox();


    public boolean empChecked()
    {
        if(employeeCheckBox.isSelected())
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void saveDefaultEmployeeProfile()
    {
        try {
            File profile = new File("src/internals/defaultProfile.icp");
            if(!profile.exists()) {
                profile.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(profile)));
                out.writeObject(employee);
                out.close();
                setAlertText("Default User for this system is: " + employee.getName());
            }
            else if(profile.exists())
            {
                profile.delete();
                profile.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(profile)));
                out.writeObject(employee);
                out.close();
                setAlertText("Default User for this system is: " + employee.getName());
            }
        }
        catch (NullPointerException nex)
        {
            alertLabelPane.setText("No File Chosen.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDefaultEmployeeProfile()
    {
        try {
            File profile = new File("src/internals/defaultProfile.icp");
            if(profile.exists() && profile.length() != 0) {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(profile)));
                employee = (Employee) (in.readObject());
                in.close();
                alertLabelPane.setText("Successful Login as " + employee.getName());
            }
            else if(!profile.exists())
            {}
        }
        catch (NullPointerException npe)
            {
                alertLabelPane.setText("No File Chosen.");
            }
        catch (EOFException eofex)
            {} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkEmployeeLoggedIn()
    {
        setAlertText("Employee Logged In: " + employee.getName());
    }

    public void clearDefaultEmployee()
    {
        File profile = new File("internals/defaultProfile.icp");
        profile.delete();
        setAlertText(employee.getName() + " is no longer the default employee");
    }

    @FXML
    ListView<String> vendorList = new ListView<>();

    public void exportByVendor() throws SQLException, IOException, ClassNotFoundException {
        checkLoggedIn();
        intLoginDatabase();
        ObservableList<String> items =FXCollections.observableArrayList();
        ResultSet set = db.query("select vendor from Inventory1 where vendor is not null");
        while (set.next()) {
            String vendor = set.getString(1);
            boolean in = false;
            for (int j = 0; j < items.size(); j++) {
                if(items.get(j).equals(vendor))
                {
                    in = true;
                }
            }
            if(!in){
                if(!vendor.equals(""))
                {
                    items.add(vendor);
                }
            }
        }
        vendorList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        vendorList.setItems(items);
    }

    public void openVendorExportDialog() throws IOException {
        cd1.showDialog("exportVendor.fxml", "Export By Vendor");
    }

    private CreateDialog cd1 = new CreateDialog();

    public void getVendors() throws SQLException {
        ObservableList<Integer> selections = vendorList.getSelectionModel().getSelectedIndices();
        ArrayList<String> vendors = new ArrayList<>();
        for (int i = 0; i < selections.size(); i++) {
            vendors.add(vendorList.getItems().get(selections.get(i)));
        }
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < vendors.size(); i++) {
            ResultSet set = db.query("select name, barcode, count, vendor, price from Inventory1 where vendor = '" + vendors.get(i) + "'");
            while(set.next()) {
                entries.add(new Entry(set.getString(1), set.getInt(3), set.getDouble(5), set.getString(4), set.getInt(2)));
            }
        }
        FileProperty fp = new FileProperty();
        File file = fp.saveFile("Excel File (*.xlsx)", "*.xlsx", "Export By Vendor(s)", "ExportByVendor.xlsx", cd1.getStage());

        SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        for(int rownum = 0; rownum < entries.size(); rownum++){
            Row row = sh.createRow(rownum);
            for(int cellnum = 0; cellnum < 5; cellnum++){
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(entries.get(rownum).getItem(cellnum));
            }

        }

        try{
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            // dispose of temporary files backing this workbook on disk
            wb.dispose();
            alertLabelPane.setText("Successfully exported to excel. Opening file...");
            openFile(file);
        }
        catch(NullPointerException ex)
        {
            setAlertText("No File Chosen.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField ipAddress = new TextField();
    @FXML
    private TextField hostnameTxt = new TextField();
    @FXML
    private TextField passwordTxt = new TextField();

    @FXML
    private Button editCredsOk = new Button();


    public void editDefaultServerCredential()
    {
        ServerCredential sc = new ServerCredential();
        sc.setAddress(ipAddress.getText());
        sc.setHostname(hostnameTxt.getText());
        sc.setPassword(passwordTxt.getText());
        sc.saveProfile();


        StageClose sc1 = new StageClose(editCredsOk);
        sc1.closeButton();
    }

    public void openEditCredentialDialog()
    {
        try {
            cd.showDialog("editServerCreds.fxml", "Edit Default Server Credentials");
        } catch (IOException e) {
            setAlertText("Failed to open Server Credential Dialog Box");
        }
    }

    public void loadDefaultCredentials()
    {
        creds = new ServerCredential().openServerProfile();
        address = creds.getAddress();
        hostnameLogin = creds.getHostname();
        passwordLogin = creds.getPassword();
    }

    public void createEmployeeDatabase()
    {
        Database database = new Database();
        Employee employee = new Employee();
        employee.createEmployeeDatabase(database);
    }

}




