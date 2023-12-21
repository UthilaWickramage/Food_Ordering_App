package lk.software.app.foodorderingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingapp.AccountActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.model.Rating;
import lk.software.app.foodorderingapp.model.User;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    Context context;
    FirebaseStorage firebaseStorage;
    ArrayList<Rating> ratings;
    FirebaseFirestore firebaseFirestore;
    Uri uriImage;

    public RatingAdapter(Context context, FirebaseStorage firebaseStorage, FirebaseFirestore firebaseFirestore, ArrayList<Rating> ratings) {
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.ratings = ratings;
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rating_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = ratings.get(position);
Log.d("size",String.valueOf(ratings.size()));
        holder.message.setText(rating.getReview());
        holder.rating.setText(String.valueOf(rating.getRating()));
        firebaseFirestore.collection("customers").document(rating.getCustomerId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        User user = value.toObject(User.class);
                        holder.customer_name.setText(user.getFull_name());
                        firebaseStorage.getReference("profileImages/" + user.getProfile_img()).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Transformation transformation = new MaskTransformation(context, R.drawable.profile_image_background);
                                        Picasso.get().load(uri).transform(transformation).centerCrop()
                                                .resize(60, 60).into(holder.profileImage);

                                    }
                                });
                    }
                });

    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name, message, rating;
        ImageView profileImage, star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.textView40);
            message = itemView.findViewById(R.id.textView41);
            rating = itemView.findViewById(R.id.textView42);
            profileImage = itemView.findViewById(R.id.imageView17);
            star = itemView.findViewById(R.id.imageView20);
        }
    }
}
