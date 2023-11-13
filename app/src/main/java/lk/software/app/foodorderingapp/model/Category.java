package lk.software.app.foodorderingapp.model;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.R;

public class Category {
    private int id;
    private String name;
    private int image;

    public Category(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Category() {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static List<Category> allCategories(){
        List<Category> allCategories = new ArrayList<>();

        allCategories.add(new Category(1,"Seafood", R.drawable.lobster_svgrepo_com));
        allCategories.add(new Category(1,"Pasta", R.drawable.spaghetti_svgrepo_com));
        allCategories.add(new Category(1,"Desert", R.drawable.icecream_svgrepo_com));
        allCategories.add(new Category(1,"Meat", R.drawable.meat_on_bone_svgrepo_com));
        allCategories.add(new Category(1,"Drinks", R.drawable.cocktail_svgrepo_com));
        allCategories.add(new Category(1,"Pizza", R.drawable.pizza_svgrepo_com_1_));
        allCategories.add(new Category(1,"Noodles", R.drawable.noodles_svgrepo_com));

        return allCategories;
    }
}
