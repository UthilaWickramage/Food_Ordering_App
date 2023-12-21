package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lk.software.app.foodorderingapp.adapters.RatingAdapter;
import lk.software.app.foodorderingapp.model.Rating;
import lk.software.app.foodorderingapp.model.User;

public class RatingActivity extends AppCompatActivity {
    private  FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private ArrayList<Rating> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        ratings = new ArrayList<>();

                String productId = getIntent().getStringExtra("productId");
                String productName = getIntent().getStringExtra("productName");

                TextView name = findViewById(R.id.textView43);
                name.setText(productName);

                findRatings(productId);
                Log.d("list size onCreate", String.valueOf(ratings.size()));



        findViewById(R.id.imageView19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void findRatings(String productId) {
        firebaseFirestore.collection("ratings").document(productId).collection("ratingByUsers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            Rating rating = documentSnapshot.toObject(Rating.class);
                            rating.setCustomerId(documentSnapshot.getId());


                            ratings.add(rating);
                            Log.d("size",String.valueOf(ratings.size()));



                        }
                        RatingAdapter ratingAdapter = new RatingAdapter(RatingActivity.this,firebaseStorage,firebaseFirestore,ratings);
                        RecyclerView recyclerView = findViewById(R.id.ratingRecycler);
                        Log.d("size",String.valueOf(ratings.size()));
                        recyclerView.setAdapter(ratingAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(RatingActivity.this));


                    }
                });
    }


}
