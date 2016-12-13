import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Myles on 6/4/16.
 */
public class Entry {
    private String name;
    private int count;
    private double price;
    private String vendor;
    private long barcode;

    public Entry() {
    }

    public Entry(String name, int count, double price, String vendor, long barcode) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.vendor = vendor;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public void empty()
    {
        this.name = "";
        this.count = -1;
        this.price = -1.0;
        this.vendor = "";
    }

    public String getItem(int i)
    {
        switch (i)
        {
            case 0:
                return "" + getBarcode();
            case 1:
                return "" + getName();
            case 2:
                return "" + getCount();
            case 3:
                return "" + getVendor();
            case 4:
                return "" + getPrice();
        }
        return "0";
    }

    public ResultSet getEntrySet(Database db) throws SQLException {
        ResultSet set = db.query("select name, vendor, price, count from Inventory1 where barcode = " + this.barcode);
        return set;
    }
    public void editEntry(Database db, int empId) throws SQLException {
        db.update("update Inventory1 set name = '" + getName() + "', vendor = '" +
                getVendor() + "', price = " + getPrice() + ", count = "
                + getCount() + ", empId = " + empId + " where barcode = " + getBarcode());
    }

    public void addEntry(Database db, Employee employee) throws SQLException {
        db.update("insert into Inventory1 set name = '" + getName() + "', vendor = '" + getVendor() + "', price = " + getPrice() + ", count = "
                + getCount() + ", empId = " + employee.getEmpID() + ", barcode = " + getBarcode());
    }

    public void emptyForImport()
    {
        this.name = "";
        this.barcode = 0;
        this.count = 0;
        this.price = 0;
        this.vendor = "";
    }

    public void print()
    {
        System.out.println("Multiple Key:");
        System.out.println(barcode);
        System.out.println(name);
        System.out.println(vendor);
        System.out.println(price);
    }

}
