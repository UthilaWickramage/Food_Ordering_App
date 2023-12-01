package lk.software.app.foodorderingapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lk.software.app.foodorderingapp.HomeActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;
import lk.software.app.foodorderingapp.adapters.CartAdapter;
import lk.software.app.foodorderingapp.model.Cart_Item;
import lk.software.app.foodorderingapp.model.Order;
import lk.software.app.foodorderingapp.model.Order_Item;

public class CartFragment extends Fragment {
    private static ArrayList<Cart_Item> cartItems;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private static ScrollView scrollView;
    private static LottieAnimationView lottieAnimationView;

    private  static  TextView totalTextView;

    private static TextView subTotalTextView;
    CartAdapter cartAdapter;
    FirebaseStorage firebaseStorage;
    private static CartFragment cartFragment;

    public CartFragment() {
        // Required empty public constructor

    }

    public static CartFragment getInstance() {
        if (cartFragment == null) {
            cartFragment = new CartFragment();
        }


        return cartFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        cartItems = new ArrayList<>();
        loadCartItems();
        scrollView = view.findViewById(R.id.scrollView);
        lottieAnimationView = view.findViewById(R.id.lottie);

//        lottieAnimationView.animate().translationX(0).setDuration(5000);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        cartAdapter = new CartAdapter(requireContext(), firebaseStorage, cartItems);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);

        subTotalTextView = view.findViewById(R.id.subTotal);
        totalTextView = view.findViewById(R.id.cartTotal);
        TextView deliveryTotalTextView = view.findViewById(R.id.deliveryTotal);

        deliveryTotalTextView.setText("Rs. 0.00");
        calculateTotals();
        view.findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.checkoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(requireContext(), "add products to the cart first", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String saveCurrentTime, saveCurrentDate;
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
                        saveCurrentDate = currentDate.format(calendar.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentTime.format(calendar.getTime());
                        Order order = new Order();
                        order.setCurrentSaveDate(saveCurrentDate);
                        order.setCurrentSaveTime(saveCurrentTime);
                        final double[] orderTotal = new double[1];
                        cartItems.forEach(cartItem -> {
                            orderTotal[0] = cartItem.getTotalProductPrice() + orderTotal[0];
                        });
                        order.setTotalOrderPrice(orderTotal[0]);

                        firebaseFirestore.collection("orders").document(currentUser.getUid()).collection("order_list").add(order)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        cartItems.forEach(cartItem -> {
                                            Order_Item order_item = new Order_Item();
                                            order_item.setProduct_name(cartItem.getName());
                                            order_item.setPrice(cartItem.getPrice());
                                            order_item.setQuantity(cartItem.getQuantity());
                                            order_item.setTotalProductPrice(cartItem.getTotalProductPrice());
                                            order_item.setImage(cartItem.getImage());

                                            firebaseFirestore.collection("orders").document(currentUser.getUid())
                                                    .collection("order_list").document(task.getResult().getId()).collection("order_items")
                                                    .add(order_item)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(requireContext(), "Order saved", Toast.LENGTH_SHORT).show();
                                                        cartItems.clear();
                                                        cartAdapter.notifyDataSetChanged();
                                                        calculateTotals();
                                                        setCartVisibility();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        });
                                    }


                                });
                    }
                }
            }
        });
setCartVisibility();
    }


    public static void calculateTotals(){
        final double[] subTotal = new double[1];
        final double[] cartTotal = new double[1];
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        cartItems.forEach(cartItem -> {
            subTotal[0] = cartItem.getTotalProductPrice() + subTotal[0];
            cartTotal[0] = cartItem.getTotalProductPrice() + cartTotal[0];
        });
        subTotalTextView.setText("Rs." +decimalFormat.format(subTotal[0]));
        totalTextView.setText("Rs." + decimalFormat.format(cartTotal[0]));
    }
    public static void setCartVisibility(){

        if(cartItems.isEmpty()){
            scrollView.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
        }
    }

    public void loadCartItems() {
        firebaseFirestore.collection("carts").document(firebaseAuth.getCurrentUser().getUid()).collection("cart_items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                String id = snapshot.getId();
                                Cart_Item cart_item = snapshot.toObject(Cart_Item.class);
                                cartItems.add(cart_item);
                                cart_item.setDocumentId(id);

                            }
                            cartAdapter.notifyDataSetChanged();
                            setCartVisibility();
                            calculateTotals();
                        }

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
}