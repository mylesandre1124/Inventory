import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Myles on 5/19/16.
 */
public class Count {

    private int count = 0;
    private Employee employee;

    public Count(Employee employee) {
        this.employee = employee;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int add()
    {
        this.count += 1;
        return count;
    }

    public int subtract()
    {
        this.count -= 1;
        return count;
    }


    public boolean isScanEmpty()
    {
        return this.employee == null;
    }

    public boolean samePerson(Employee other)
    {
        return this.employee.getEmpID() == other.getEmpID();
    }

    public int getCountDb(Database db, long barcode)
    {
        ResultSet resultSet = db.query
                ("select count, empId from Inventory1 where barcode = " + barcode);
        int count = 0;
        int empId = 0;
        try {
            while (resultSet.next()) {
                count = Integer.parseInt(resultSet.getString(1));
                empId = resultSet.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setCount(count);
        Employee employee = new Employee();
        employee.setEmpID(empId);
        return employee.getEmpID();
    }

    public void updateCount(Database db, long barcode)
    {
        db.update("update Inventory1 set count = " + getCount() + ", empId = " + employee.getEmpID() + " where barcode = " + barcode);
    }

}
