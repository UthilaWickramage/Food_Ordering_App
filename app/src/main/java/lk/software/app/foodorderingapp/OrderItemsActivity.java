package lk.software.app.foodorderingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingapp.adapters.OrderItemAdapter;
import lk.software.app.foodorderingapp.model.OrderStatusEnum;
import lk.software.app.foodorderingapp.model.Order_Item;

public class OrderItemsActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<Order_Item> orderItems;
    private String documentId;
FirebaseAuth firebaseAuth;

FirebaseUser currentUser;
    private Order_Item orderItem;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
firebaseAuth= FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        orderItems = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        documentId = getIntent().getExtras().getString("documentId");
        RecyclerView recyclerView = findViewById(R.id.order_item_recycler);
        orderItems = new ArrayList<>();
        loadOrderItems();
        orderItemAdapter = new OrderItemAdapter(OrderItemsActivity.this, firebaseStorage, orderItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderItemsActivity.this);
        recyclerView.setAdapter(orderItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        TextView order_made = findViewById(R.id.textView16);
        TextView totalOrderPrice = findViewById(R.id.textView19);


        order_made.setText(getIntent().getExtras().getString("date"));

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        totalOrderPrice.setText(decimalFormat.format(Double.parseDouble(getIntent().getExtras().getString("price"))));
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        TextView status = findViewById(R.id.textView12);
        String orderStatus = getIntent().getExtras().getString("status");
        status.setText(orderStatus);
        if (orderStatus.equals(OrderStatusEnum.PENDING.toString())) {

            frameLayout.setBackgroundColor(getColor(R.color.light_blue));
            status.setTextColor(getColor(R.color.blue));
        } else if (orderStatus.equals(OrderStatusEnum.ACCEPTED.toString())) {
            frameLayout.setBackgroundColor(getColor(R.color.light_yellow));
            status.setTextColor(getColor(R.color.dark_yellow));

        } else if (orderStatus.equals(OrderStatusEnum.DELIVERED.toString())) {
           frameLayout.setBackgroundColor(getColor(R.color.light_green));
            status.setTextColor(getColor(R.color.green));

        } else if (orderStatus.equals(OrderStatusEnum.REJECTED.toString())) {
            frameLayout.setBackgroundColor(getColor(R.color.light_red));
            status.setTextColor(getColor(R.color.red));

        }

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadOrderItems() {
        firebaseFirestore.collection("orders").document(currentUser.getUid()).collection("order_list").document(documentId).collection("order_items")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        orderItems.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            orderItem = snapshot.toObject(Order_Item.class);

                            orderItems.add(orderItem);
                        }
                        orderItemAdapter.notifyDataSetChanged();
                    }

                });
    }
}