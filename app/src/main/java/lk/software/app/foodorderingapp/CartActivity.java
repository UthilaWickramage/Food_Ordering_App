package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lk.software.app.foodorderingapp.model.Order_Item;

public class CartActivity extends AppCompatActivity {

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        layoutInflater = getLayoutInflater();



        List<Order_Item> orderItems = Order_Item.getAllOrderItems();

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<OrderItemViewHolder>() {

            @NonNull
            @Override
            public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = layoutInflater.inflate(R.layout.order_item_layout, parent, false);
                return new OrderItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
                holder.textView1.setText(orderItems.get(position).getName());
                holder.textView2.setText(orderItems.get(position).getCategory());
                holder.textView3.setText(String.valueOf(orderItems.get(position).getPrice()));
                holder.textView4.setText("x" + orderItems.get(position).getQuantity());
                holder.imageView.setImageResource(orderItems.get(position).getImage());
            }

            @Override
            public int getItemCount() {
                return orderItems.size();
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}

class OrderItemViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    public OrderItemViewHolder(@NonNull View v) {
        super(v);
        imageView = v.findViewById(R.id.order_item_image);
        textView1 = v.findViewById(R.id.titleText);
        textView2 = v.findViewById(R.id.categoryText);
        textView3 = v.findViewById(R.id.order_item_price);
        textView4 = v.findViewById(R.id.order_qty);

    }
}