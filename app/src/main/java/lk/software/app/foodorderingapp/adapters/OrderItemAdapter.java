package lk.software.app.foodorderingapp.adapters;

import android.content.Context;
import android.net.Uri;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingapp.R;

import lk.software.app.foodorderingapp.model.Order_Item;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Order_Item> orderItems;

    private FirebaseStorage firebaseStorage;


    public OrderItemAdapter(Context context,FirebaseStorage firebaseStorage, ArrayList<Order_Item> orderItems) {
this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.orderItems = orderItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Item orderItem = orderItems.get(position);
        holder.titleText.setText(orderItem.getProduct_name());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.order_item_price.setText(decimalFormat.format(orderItem.getPrice()));
        holder.order_qty.setText("x"+String.valueOf(orderItem.getQuantity()));
        firebaseStorage.getReference("productImages/"+orderItem.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).centerCrop().resize(75,75).into(holder.imageView);
                    }
                });

        holder.deleteBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,deleteBtn;

        TextView titleText, order_item_price, order_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleText = itemView.findViewById(R.id.titleText);
            this.order_item_price = itemView.findViewById(R.id.order_item_price);
            this.order_qty = itemView.findViewById(R.id.order_qty);
            this.imageView = itemView.findViewById(R.id.order_item_image);
this.deleteBtn = itemView.findViewById(R.id.delete_cart);
        }
    }
}
