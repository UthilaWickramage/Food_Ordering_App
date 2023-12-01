package lk.software.app.foodorderingapp.model;

public class Order_Item {
    private String product_name;
    private double price;
    private int quantity;

    private double totalProductPrice;

String image;

    public Order_Item(String product_name, double price, int quantity, double totalProductPrice,String image) {
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.totalProductPrice = totalProductPrice;
       this.image = image;
    }

    public Order_Item() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
