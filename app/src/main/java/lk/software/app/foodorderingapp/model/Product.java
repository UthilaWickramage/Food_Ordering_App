package lk.software.app.foodorderingapp.model;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.R;

public class Product {
    public int id;
    public String name;
    public double price;
    public int image;

    public Product(int id, String name, double price, int image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Product() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static List<Product> getNewProducts(){
        List<Product> newProducts = new ArrayList<>();

        newProducts.add(new Product(1,"Chicken Burger",7.99, R.mipmap.burger));
        newProducts.add(new Product(2,"Italian Pasta",15.99, R.mipmap.pasta));
        newProducts.add(new Product(3,"Cuttlefish Pizza",5.99, R.mipmap.pizza));
        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
        newProducts.add(new Product(4,"Chocolate Cake",2.99, R.mipmap.cake));
        newProducts.add(new Product(5,"Chicken Wings",7.99, R.mipmap.pizza));

        return newProducts;
    }
}
