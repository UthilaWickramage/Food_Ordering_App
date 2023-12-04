package lk.software.app.foodorderingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.software.app.foodorderingapp.adapters.OrderAdapter;
import lk.software.app.foodorderingapp.model.Order;

public class OrderActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orders;

    private Order order;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    NotificationManager notificationManager;
    private String channel_id = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        firebaseFirestore = FirebaseFirestore.getInstance();
        orders = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        RecyclerView recyclerView = findViewById(R.id.orderRecycler);
        loadOrders();
        orderAdapter = new OrderAdapter(OrderActivity.this, orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderActivity.this);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        requestPermissions(new String[]{
                Manifest.permission.POST_NOTIFICATIONS,


        }, 10);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //we need to create a channel in sdk higher than 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id, "INFO", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setDescription("Sample channel");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            channel.setVibrationPattern(new long[]{0, 1000, 1000, 1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);


            notificationManager.getNotificationChannels().forEach(c -> {
                Log.i(RegisterActivity.TAG, c.toString());
            });
        }
    }

    private void loadOrders() {
        firebaseFirestore.collection("orders").document(currentUser.getUid()).collection("order_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        orders.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            order = snapshot.toObject(Order.class);
                            order.setDocumentId(snapshot.getId());

                            orders.add(order);
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String statusChangeListen =  documentChange.getDocument().getString("orderStatus");
                            if(statusChangeListen!=null){
                                if(statusChangeListen.equals("ACCEPTED")){
                                    Intent intent =new Intent(OrderActivity.this, AccountActivity.class);

                                    PendingIntent pendingIntent =
                                            PendingIntent.getActivity(OrderActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                                            .setSmallIcon(R.drawable.baseline_delivery_dining_24)
                                            .setAutoCancel(true)
                                            .setContentTitle("Your order accepted")
                                            .setContentText("Dear customer, congratulations! your order has been accepted")
                                            .setColor(Color.YELLOW)
                                            .setContentIntent(pendingIntent)
                                            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                            .build();

                                    notificationManager.notify(1, notification);
                                }
                                if(statusChangeListen.equals("REJECTED")){
                                    Intent intent =new Intent(OrderActivity.this, AccountActivity.class);

                                    PendingIntent pendingIntent =
                                            PendingIntent.getActivity(OrderActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                                            .setSmallIcon(R.drawable.baseline_delivery_dining_24)
                                            .setAutoCancel(true)
                                            .setContentTitle("Your order is rejected")
                                            .setContentText("Dear customer, Sorry! your order is rejected")
                                            .setColor(Color.RED)
                                            .setContentIntent(pendingIntent)
                                            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                            .build();

                                    notificationManager.notify(1, notification);
                                }
                                if(statusChangeListen.equals("DELIVERED")){
                                    Intent intent =new Intent(OrderActivity.this, AccountActivity.class);

                                    PendingIntent pendingIntent =
                                            PendingIntent.getActivity(OrderActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                                            .setSmallIcon(R.drawable.baseline_delivery_dining_24)
                                            .setAutoCancel(true)
                                            .setContentTitle("Your order is Delivered")
                                            .setContentText("Dear customer, enjoy your new product!")
                                            .setColor(Color.BLUE)
                                            .setContentIntent(pendingIntent)
                                            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                            .build();

                                    notificationManager.notify(1, notification);
                                }
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                });
    }
}