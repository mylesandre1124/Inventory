/**
 * Created by Myles on 7/1/16.
 */
public class EntryString {

    private String name;
    private String count;
    private String price;
    private String vendor;
    private String barcode;

    public EntryString() {
    }

    public EntryString(String barcode, String name, String vendor, String price, String count) {
        this.name = name;
        this.price = price;
        this.vendor = vendor;
        this.barcode = barcode;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void empty()
    {
        this.name = "";
        this.price = "";
        this.vendor = "";
        this.barcode = "";
        this.count = "";
    }

    public void print()
    {
        System.out.println("Data Type:");
        System.out.println(barcode);
        System.out.println(name);
        System.out.println(vendor);
        System.out.println(price);
    }
}
