import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Myles on 6/20/16.
 */
public class EntryProperty {

    private SimpleStringProperty  name = new SimpleStringProperty();
    private SimpleIntegerProperty count = new SimpleIntegerProperty();
    private SimpleDoubleProperty price = new SimpleDoubleProperty();
    private SimpleStringProperty vendor = new SimpleStringProperty();
    private SimpleLongProperty barcode = new SimpleLongProperty();

    public EntryProperty() {
    }

    public EntryProperty(SimpleStringProperty name, SimpleIntegerProperty count, SimpleDoubleProperty price, SimpleStringProperty vendor, SimpleLongProperty barcode) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.vendor = vendor;
        this.barcode = barcode;
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

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public String getVendor() {
        return vendor.get();
    }

    public SimpleStringProperty vendorProperty() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor.set(vendor);
    }

    public long getBarcode() {
        return barcode.get();
    }

    public SimpleLongProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode.set(barcode);
    }
}
