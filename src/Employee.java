import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Myles on 5/19/16.
 */
public class Employee implements java.io.Serializable {

    private String name;
    private int empID;
    private int admin;
    private String username;

    public Employee(String name, int empID) {
        this.name = name;
        this.empID = empID;
    }

    public Employee() {
        this.name = "";
        this.empID = -1;
        this.admin = -1;

    }

    public Employee(String name, int empID, String username) {
        this.name = name;
        this.empID = empID;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void logout() {
        this.name = "";
        this.empID = -1;
        this.admin = -1;
        this.username = "";
    }
    public boolean isAdmin()
    {
        if(admin == 1)
        {
            return true;
        }
        else if(admin == 0)
        {
            return false;
        }
        else {
            return false;
        }
    }

    public void deleteEmployee(String username, Database db, Employee employee, Controls controls) throws SQLException, ClassNotFoundException, IOException {

        Database db1 = db;
        String delUser = username;
        int zer0OrOne = -1;
        ResultSet userTest = db1.query("SELECT EXISTS(SELECT 1 FROM Employee WHERE username = '" + delUser + "')");
        while(userTest.next())
        {
            zer0OrOne = userTest.getInt(1);
        }
        if(zer0OrOne == 1)
        {
            try {
                ResultSet admin = db1.query("select admin from Employee where empId = " + employee.getEmpID());
                while (admin.next()) {
                    int adminTest = admin.getInt(1);
                    if (adminTest == 1) {
                        db1.update("delete from Employee where username = '" + delUser + "'");
                        Stage stage = (Stage) ((Button)controls.getControls().get(0)).getScene().getWindow();
                        stage.close();
                    } else if (adminTest == 0) {
                        ((Label)controls.getControls().get(1)).setText("Contact Admin. You are not authorised to make these changes.");
                    }
                }
            }
            catch (SQLException e)
            {}
            db1.getStatement().close();
        }
        else if(zer0OrOne == 0)
        {
            ((Label)controls.getControls().get(1)).setText("Username Not Found.");
        }
    }

    public void createNewEmployee(Database db, String password1)
    {
        db.update("insert into Employee set name = '" + getName() + "', username = '" + getUsername() + "', empId = " + getEmpID() + ", password = '" + password1 + "'");
    }

    public int checkEmployeeExists(Database db, int empId)
    {
        int exists = -1;
        ResultSet set = db.query("SELECT EXISTS(select 1 from Employee where empId = " + empId + ")");
        try {
            while (set.next()) {
                exists = set.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return exists;
    }


    public void createEmployeeDatabase(Database database)
    {
        database.connectToEditDatabase();
        database.update("CREATE SCHEMA `Employee1`");
        database.update("CREATE TABLE `Employee1`.`Employee1` " +
                "(`name` VARCHAR(45) NULL DEFAULT NULL, " +
                "`empId` INT(11) NOT NULL, " +
                "`username` VARCHAR(45) NOT NULL, " +
                "`password` VARCHAR(45) NOT NULL, " +
                "`admin` INT(11) NULL DEFAULT '0', " +
                "PRIMARY KEY (`empId`));");
    }


}
