import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Myles on 6/24/16.
 */
public class EmployeeTableEntry {

    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleIntegerProperty empId = new SimpleIntegerProperty();
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty admin = new SimpleStringProperty();
    //private EmployeeTableEntry entry = new EmployeeTableEntry();

    public EmployeeTableEntry(SimpleStringProperty name, SimpleIntegerProperty empId, SimpleStringProperty username, SimpleStringProperty password, SimpleStringProperty admin) {
        this.name = name;
        this.empId = empId;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    /*public EmployeeTableEntry(EmployeeTableEntry entry) {
        this.entry = entry;
        this.name = this.entry.nameProperty();
        this.empId = this.entry.empIdProperty();
        this.username = this.entry.usernameProperty();
        this.password = this.entry.passwordProperty();
        this.admin = this.entry.adminProperty();
    }
    */

    public EmployeeTableEntry() {
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getEmpId() {
        return empId.get();
    }

    public SimpleIntegerProperty empIdProperty() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId.set(empId);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAdmin() {
        return admin.get();
    }

    public SimpleStringProperty adminProperty() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin.set(admin);
    }
}
