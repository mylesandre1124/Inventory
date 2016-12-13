/**
 * Created by Myles on 5/19/16.
 */
public class Price {

    private double price;

    public Price(double price) {
        this.price = price;
    }

    public String getPrice$() {
        String price = ("$" + this.price);
        return price;
    }

    public double getPriceD() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
