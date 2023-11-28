package lk.software.app.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import lk.software.app.foodorderingapp.model.Product;

public class ProductActivity extends AppCompatActivity {
    public static final String TAG = ProductActivity.class.getName();
   private  Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            product =  intent.getSerializableExtra("product",Product.class);
        }else{
            product = (Product) intent.getSerializableExtra("product");
        }
        Bundle extras = intent.getExtras();
        Uri productImage1 = (Uri) extras.get("productImage");
        Log.d(TAG,product.getName());
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
        category.setText(product.getCategory());
        price.setText("Rs."+price_formatted);
        prepare_time.setText(String.valueOf(product.getPrepare_time()));
        rating.setText(String.valueOf(product.getRating()));
        person_per_serve.setText(String.valueOf(product.getPerson_per_serve()));
        description.setText(product.getDescription());
        Picasso.get().load(productImage1).centerCrop()
                        .resize(380,280).into(productImage);
        Log.d(TAG,product.getImage());
        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.addToCartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}