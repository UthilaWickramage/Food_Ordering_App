package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import lk.software.app.foodorderingapp.model.Cart_Item;
import lk.software.app.foodorderingapp.model.Product;
import lk.software.app.foodorderingapp.model.Rating;

public class ProductActivity extends AppCompatActivity {
    public static final String TAG = ProductActivity.class.getName();
    private FirebaseStorage firebaseStorage;
private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            product = intent.getSerializableExtra("product", Product.class);
        } else {
            product = (Product) intent.getSerializableExtra("product");
        }



        Log.d(TAG, product.getCategory_name());
        TextView name = findViewById(R.id.nameSingle);
        TextView category = findViewById(R.id.categorySingle);
        TextView price = findViewById(R.id.priceSingle);
        TextView prepare_time = findViewById(R.id.prepareTime);
        TextView rating = findViewById(R.id.product_rating);
        TextView person_per_serve = findViewById(R.id.person_per_serve);
        TextView description = findViewById(R.id.descriptionSingle);
        ImageView productImage = findViewById(R.id.productPicture);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String price_formatted = decimalFormat.format(product.getPrice());
        name.setText(product.getName());
        category.setText(product.getCategory_name());
        price.setText("Rs." + price_formatted);
        prepare_time.setText(String.valueOf(product.getPrepare_time()));

        person_per_serve.setText(String.valueOf(product.getPerson_per_serve()));
        description.setText(product.getDescription());

        firebaseFirestore.collection("ratings").document(product.getProductDocumentId())
                .collection("ratingByUsers").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                           int count = 0;
                           double totalRating = 0;
                                   double avg = 0;

                           for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                               Rating rating1 = snapshot.toObject(Rating.class);
                               if(rating1!=null){
                                   double productRating = rating1.getRating();
                                   count++;
                                   totalRating = totalRating+productRating;
                                   avg = totalRating/count;

                               }else{
                                   rating.setText(String.valueOf(avg));
                               }


                           }

                                rating.setText(String.valueOf(avg));
                            }
                        });


        firebaseStorage.getReference("productImages/" + product.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).centerCrop()
                                .resize(380, 280).into(productImage);
                    }
                });


        Log.d(TAG, product.getImage());

        findViewById(R.id.ratingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser()!=null){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductActivity.this);
                    View inflate = getLayoutInflater().inflate(R.layout.rating_layout, null, false);
                    alertDialog.setView(inflate);
                    alertDialog.setTitle("Rate this food");
                    alertDialog.setMessage("How would you rate this food?");

                    alertDialog.setPositiveButton("Submit",(dialog, which) -> {
                        RatingBar ratingBar = inflate.findViewById(R.id.ratingBar2);
                        HashMap<String, Float> map = new HashMap<>();
                        map.put("rating",ratingBar.getRating());
                        firebaseFirestore.collection("ratings").document(product.getProductDocumentId()).collection("ratingByUsers").document(firebaseAuth.getCurrentUser().getUid()).set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Thank you for your rating",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    });
                    alertDialog.show();
                }else {
                    Toast.makeText(getApplicationContext(),"You need to login to rate",Toast.LENGTH_SHORT).show();

                }

            }
        });
        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        TextView quantityTextView = findViewById(R.id.quantity);
        final int[] quantity = {1};
        quantity[0] = 1;
        quantityTextView.setText(String.valueOf(quantity[0]));
        findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] <= 10) {
                    quantity[0]++;
                    quantityTextView.setText(String.valueOf(quantity[0]));
                } else {
                    Toast.makeText(ProductActivity.this, "10 is the limit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    quantityTextView.setText(String.valueOf(quantity[0]));
                } else {
                    Toast.makeText(ProductActivity.this, "quantity should be at least 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.addToOrderBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveCurrentTime, saveCurrentDate;
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                TextView quantityTextView = findViewById(R.id.quantity);
                double totalProductPrice = product.getPrice()*Double.parseDouble(quantityTextView.getText().toString());

                Cart_Item cart_item = new Cart_Item(
                        product.getName(),
                        product.getCategory_name(),
                        product.getPrice(),
                        Integer.parseInt(quantityTextView.getText().toString()),
                        totalProductPrice,
                        saveCurrentDate,
                        saveCurrentTime,
                        product.getImage()
                );
                firebaseFirestore.collection("carts").document(firebaseAuth.getCurrentUser().getUid()).collection("cart_items").add(cart_item)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(ProductActivity.this,"Product added to Cart",Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
    }
}