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

import java.util.ArrayList;

import java.util.concurrent.RecursiveAction;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private FirebaseStorage firebaseStorage;
    private ArrayList<Category> categories;

    public CategoryAdapter() {

    }

    public CategoryAdapter(Context context, FirebaseStorage firebaseStorage, ArrayList<Category> categories) {
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textView.setText(categories.get(position).getName());
        firebaseStorage.getReference("categoryImages/" + category.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).centerCrop().resize(100, 100).into(holder.imageView);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            textView = itemView.findViewById(R.id.textView8);
        }
    }
}
