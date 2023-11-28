package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lk.software.app.foodorderingapp.model.Product;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

//        List<Product> products = Product.getNewProducts();
//        LayoutInflater layoutInflater = getLayoutInflater();
//
//        RecyclerView.Adapter adapter = new RecyclerView.Adapter<ProductViewHolder>() {
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = layoutInflater.inflate(R.layout.product_card,parent,false);
//
//                return new ProductViewHolder(view);
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//                holder.textView.setText(products.get(position).getName());
//                holder.textView2.setText(String.valueOf(products.get(position).getPrice()));
//                holder.imageView.setImageResource(products.get(position).getImage());
//                holder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(CategoryActivity.this,ProductActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @Override
//            public int getItemCount() {
//                return products.size();
//            }
//        };
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView4);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this,2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(adapter);
}
}