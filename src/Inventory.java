import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Myles on 2/25/17.
 */

public class Inventory {

    private String name;
    private int count;
    private double price;
    private String vendor;
    private long barcode;
    private int empId;
    private Inventory inventory;

    public Inventory(String name, int count, double price, String vendor, long barcode, int empId) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.vendor = vendor;
        this.barcode = barcode;
        this.empId = empId;
    }

    public Inventory() {
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

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Inventory toInventory(Inventory inventory) {
        this.name = inventory.getName();
        this.count = inventory.getCount();
        this.price = inventory.getPrice();
        this.vendor = inventory.getVendor();
        this.barcode = inventory.getBarcode();
        this.empId = inventory.getEmpId();
        return this.inventory;
    }


}
