import java.sql.*;

/**
 * Created by mandre3 on 11/8/16.
 */
public class Database {

    private static Statement statement;
    private static Connection connection;
    private static String hostnameLogin;
    private static String passwordLogin;
    private static String address;
    private static String dbName;
    private ServerCredential creds;

    public Database(Statement statement, Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = statement;
        this.statement = this.connection.createStatement();

    }

    public Database(ServerCredential creds) {
        this.creds = creds;
        this.setHostnameLogin(creds.getHostname());
        this.setPasswordLogin(creds.getPassword());
        this.setAddress(creds.getAddress());
    }

    public Database(int select, ServerCredential serverCredential)
    {
        this.creds = serverCredential;
        this.setHostnameLogin(serverCredential.getHostname());
        this.setPasswordLogin(serverCredential.getPassword());
        this.setAddress(serverCredential.getAddress());
        if(select == 0)
        {
            connectInventoryDatabase();
        }
        else if(select == 1)
        {
            connectEmployeeDatabase();
        }
    }

    public Database(String hostnameLogin, String passwordLogin, String address) {
        setHostnameLogin(hostnameLogin);
        setPasswordLogin(passwordLogin);
        setAddress(address);
    }

    public Database() {
        this.creds = new ServerCredential().openServerProfile();
        this.setCredentials();
    }

    public static Statement getStatement() {
        return statement;
    }

    public static void setStatement(Statement statement) {
        Database.statement = statement;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

    public String getHostnameLogin() {
        return hostnameLogin;
    }

    public void setHostnameLogin(String hostnameLogin) {
        this.hostnameLogin = hostnameLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ServerCredential getCreds() {
        return creds;
    }

    public void setCreds(ServerCredential creds) {
        this.creds = creds;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        Database.dbName = dbName;
    }

    public void setCredentials()
    {
        setHostnameLogin(creds.getHostname());
        setPasswordLogin(creds.getPassword());
        setAddress(creds.getAddress());
    }

    public ResultSet query(String msg) {
        ResultSet set = null;
        try {
            set = statement.executeQuery(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;
    }

    public void update(String msg){
        try {
            getStatement().executeUpdate(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connectDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + address + "/" + getDbName() + "?useSSL=false", hostnameLogin , passwordLogin));
            setStatement(getConnection().createStatement());
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex)
        {
            System.out.println("Connection Refused");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connectDatabase(String dbName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + address + "/" + dbName + "?useSSL=false", hostnameLogin , passwordLogin));
            setStatement(getConnection().createStatement());
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex)
        {
            System.out.println("Connection Refused");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connectToEditDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + address + "?useSSL=false", hostnameLogin , passwordLogin));
            setStatement(getConnection().createStatement());
        }
        catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex)
        {
            System.out.println("Connection Refused");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connectInventoryDatabase()
    {
        connectDatabase("Inventory1");
    }

    public static void connectEmployeeDatabase()
    {
        connectDatabase("Employee");
    }

    public boolean checkDbExists(String dbName)
    {
        Database database = new Database();
        try {
            ResultSet set = database.query("select exists (SELECT 1 FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + dbName + "')");
            int exists = -1;
            while(set.next())
            {
                exists = set.getInt(1);
            }
            if(exists == 1)
            {
                return true;
            }
            else if(exists == 0 || exists == -1)
            {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public void createInventoryDatabase()
    {
        update("CREATE SCHEMA `Inventory1` ");
        update("CREATE TABLE `Inventory1`.`Inventory1` (\n" +
                "  `barcode` INT(11) NOT NULL DEFAULT '0',\n" +
                "  `name` VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  `count` INT(11) NULL DEFAULT '0',\n" +
                "  `vendor` VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  `price` DECIMAL(5,2) NULL DEFAULT NULL,\n" +
                "  `empId` INT(11) NULL DEFAULT '0',\n" +
                "  PRIMARY KEY (`barcode`));");
    }

}
