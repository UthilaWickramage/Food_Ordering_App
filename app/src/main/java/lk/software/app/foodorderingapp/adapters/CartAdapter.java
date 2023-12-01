package lk.software.app.foodorderingapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;
import lk.software.app.foodorderingapp.fragments.CartFragment;
import lk.software.app.foodorderingapp.model.Cart_Item;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<Cart_Item> cartItems;
    Uri uriImage;

    public CartAdapter(Context context, FirebaseStorage firebaseStorage, ArrayList<Cart_Item> cartItems) {
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.cartItems = cartItems;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(cartItems.get(position).getName());

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.textView3.setText("Rs." + decimalFormat.format(cartItems.get(position).getTotalProductPrice()));

        holder.textView4.setText("x" + cartItems.get(position).getQuantity());
        firebaseStorage.getReference("productImages/" + cartItems.get(position).getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(75, 75).centerCrop().into(holder.imageView);
                        uriImage = uri;
                    }
                });

        holder.deleteBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(RegisterActivity.TAG,"working");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete this item?");
                alertDialog.setMessage("This can't be undone");
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("carts").document(firebaseAuth.getCurrentUser().getUid())
                                .collection("cart_items").document(cartItems.get(position).getDocumentId()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            cartItems.remove(cartItems.get(position));
                                            notifyDataSetChanged();
                                            CartFragment.setCartVisibility();
                                            CartFragment.calculateTotals();
                                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, "Something occured", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                    }
                });
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, deleteBin;
        TextView textView1;

        TextView textView3;
        TextView textView4;

        public ViewHolder(@NonNull View v) {
            super(v);
            imageView = v.findViewById(R.id.order_item_image);
            textView1 = v.findViewById(R.id.titleText);
            deleteBin = v.findViewById(R.id.delete_cart);
            textView3 = v.findViewById(R.id.order_item_price);
            textView4 = v.findViewById(R.id.order_qty);

        }
    }
}
