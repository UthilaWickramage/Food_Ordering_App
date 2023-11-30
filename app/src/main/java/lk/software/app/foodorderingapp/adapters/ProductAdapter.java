package lk.software.app.foodorderingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingapp.ProductActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    FirebaseStorage firebaseStorage;
    ArrayList<Product> products;
    Uri uriImage;

    public ProductAdapter() {

    }

    public ProductAdapter(Context context, FirebaseStorage firebaseStorage, ArrayList<Product> products) {
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.prepare_time.setText(String.valueOf(products.get(position).getPrepare_time()));
        holder.rating.setText(String.valueOf(products.get(position).getRating()));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        holder.price.setText("Rs." + decimalFormat.format(products.get(position).getPrice()));
        firebaseStorage.getReference("productImages/" + products.get(position).getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(150, 150).centerCrop().into(holder.product_image);
                        uriImage = uri;
                    }
                });
        holder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("product", products.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView name, prepare_time, rating, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_card_image);
            name = itemView.findViewById(R.id.product_card_name);
            prepare_time = itemView.findViewById(R.id.product_card_prepare_time);
            rating = itemView.findViewById(R.id.product_card_rating);
            price = itemView.findViewById(R.id.product_card_price);

        }
    }
}
