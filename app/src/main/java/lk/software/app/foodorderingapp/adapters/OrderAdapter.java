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


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    FirebaseStorage firebaseStorage;
    ArrayList<Order_Item> orderItems;
    Uri uriImage;

    public OrderAdapter(Context context, FirebaseStorage firebaseStorage, ArrayList<Order_Item> orderItems){
        this.context = context;
        this.firebaseStorage = firebaseStorage;
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
        holder.textView1.setText(orderItems.get(position).getName());
        holder.textView2.setText(orderItems.get(position).getCategory());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.textView3.setText("Rs."+decimalFormat.format(orderItems.get(position).getTotalProductPrice()));

        holder.textView4.setText("x" + orderItems.get(position).getQuantity());
        firebaseStorage.getReference("productImages/" + orderItems.get(position).getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(75, 75).centerCrop().into(holder.imageView);
                        uriImage = uri;
                    }
                });
    }



    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        public ViewHolder(@NonNull View v) {
            super(v);
            imageView = v.findViewById(R.id.order_item_image);
            textView1 = v.findViewById(R.id.titleText);
            textView2 = v.findViewById(R.id.categoryText);
            textView3 = v.findViewById(R.id.order_item_price);
            textView4 = v.findViewById(R.id.order_qty);

        }
    }
}
