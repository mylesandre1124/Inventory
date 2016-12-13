import java.io.Serializable;

/**
 * Created by mandre3 on 10/31/16.
 */
public class Barcode implements Serializable{

    private long barcode;

    public Barcode(long barcode) {
        this.barcode = barcode;
    }

    public Barcode() {
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }


}
