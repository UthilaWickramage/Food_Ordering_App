package lk.software.app.foodorderingapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.R;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
private int person_per_serve;
    private double rating;

    private String description;
private String category_name;
    private int prepare_time;
    private String image;
private String addedDate;
private String productDocumentId;

    public Product(int id, String name, double price, double rating, String description, String category_name,int person_per_serve, int prepare_time, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.prepare_time = prepare_time;
        this.rating = rating;
        this.person_per_serve = person_per_serve;
        this.category_name = category_name;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPerson_per_serve() {
        return person_per_serve;
    }

    public void setPerson_per_serve(int person_per_serve) {
        this.person_per_serve = person_per_serve;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public Product() {
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPrepare_time() {
        return prepare_time;
    }

    public void setPrepare_time(int prepare_time) {
        this.prepare_time = prepare_time;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductDocumentId() {
        return productDocumentId;
    }

    public void setProductDocumentId(String productDocumentId) {
        this.productDocumentId = productDocumentId;
    }
}
