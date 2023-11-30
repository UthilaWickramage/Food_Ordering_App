package lk.software.app.foodorderingapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.R;

public class Order_Item implements Serializable {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
private double totalProductPrice;
    private String image;

    private String currentSaveDate, currentSaveTime;

    public Order_Item(String name, String category, double price, int quantity, double totalProductPrice, String currentSaveDate,String currentSaveTime, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalProductPrice = totalProductPrice;
        this.currentSaveDate = currentSaveDate;
        this.currentSaveTime= currentSaveTime;
        this.image = image;
    }

    public Order_Item() {
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrentSaveDate() {
        return currentSaveDate;
    }

    public void setCurrentSaveDate(String currentSaveDate) {
        this.currentSaveDate = currentSaveDate;
    }

    public String getCurrentSaveTime() {
        return currentSaveTime;
    }

    public void setCurrentSaveTime(String currentSaveTime) {
        this.currentSaveTime = currentSaveTime;
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

    public String getImage(){
        return image;
    }

    public static List<Order_Item> getAllOrderItems(){
        List<Order_Item> orderItems = new ArrayList<>();




        return orderItems;
    }
}
