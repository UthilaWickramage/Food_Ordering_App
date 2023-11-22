package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;

import java.util.List;

import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

public class HomeActivity extends AppCompatActivity {

    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layoutInflater = getLayoutInflater();
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        layoutInflater.inflate(R.layout.home_banner,frameLayout,true);
        List<Category> allCategories = Category.allCategories();
        List<Product> newProducts = Product.getNewProducts();

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<CategoryViewHolder>() {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = layoutInflater.inflate(R.layout.category_card, parent, false);
                return new CategoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
                holder.textView.setText(allCategories.get(position).getName());
                holder.imageView.setImageResource(allCategories.get(position).getImage());
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,CategoryActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return allCategories.size();
            }
        };

        RecyclerView.Adapter productAdapter = new RecyclerView.Adapter<ProductViewHolder>() {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = layoutInflater.inflate(R.layout.product_card, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
                holder.textView.setText(newProducts.get(position).getName());
                holder.textView2.setText(String.valueOf(newProducts.get(position).getPrice()));
                holder.imageView.setImageResource(newProducts.get(position).getImage());
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,ProductActivity.class);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return newProducts.size();
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycleView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView2 = findViewById(R.id.recycleView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(HomeActivity.this);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(productAdapter);
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;

    public CategoryViewHolder(@NonNull View v) {
        super(v);
        imageView = v.findViewById(R.id.imageView3);
        textView = v.findViewById(R.id.textView8);
    }
}

class ProductViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    TextView textView2;


    public ProductViewHolder(@NonNull View v) {
        super(v);
        imageView = v.findViewById(R.id.imageView4);
        textView = v.findViewById(R.id.textView9);
        textView2 = v.findViewById(R.id.textView15);

    }
}

