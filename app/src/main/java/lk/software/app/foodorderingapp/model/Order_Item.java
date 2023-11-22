package lk.software.app.foodorderingapp.model;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.R;

public class Order_Item {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    private int image;

    public Order_Item(int id, String name, String category, double price, int quantity, int image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public Order_Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getImage(){
        return image;
    }

    public static List<Order_Item> getAllOrderItems(){
        List<Order_Item> orderItems = new ArrayList<>();

        orderItems.add(new Order_Item(1,"Chocolate Cake","Cake",12.99,2, R.mipmap.cake));
        orderItems.add(new Order_Item(1,"CuttleFish Pizza","Pizza",7.99,1, R.mipmap.pizza));
        orderItems.add(new Order_Item(1,"Italian Pasta","Pizza",7.99,1, R.mipmap.pasta));
        orderItems.add(new Order_Item(1,"Chicken Burger","Pizza",7.99,3, R.mipmap.burger));
        orderItems.add(new Order_Item(1,"British Bread","Pizza",7.99,3, R.mipmap.burger));
        orderItems.add(new Order_Item(1,"French Fries","Pizza",7.99,3, R.mipmap.pasta));
        orderItems.add(new Order_Item(1,"Chicken Pizza","Pizza",7.99,3, R.mipmap.pizza));


        return orderItems;
    }
}
