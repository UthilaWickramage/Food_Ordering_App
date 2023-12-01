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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.software.app.foodorderingapp.HomeActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.adapters.CartAdapter;
import lk.software.app.foodorderingapp.model.Cart_Item;

public class CartFragment extends Fragment {
    ArrayList<Cart_Item> cartItems;
    LayoutInflater layoutInflater;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

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
        loadOrders();
        cartAdapter = new CartAdapter(requireContext(), firebaseStorage, cartItems);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);
        final double[] subTotal = new double[1];
        final double[] cartTotal = new double[1];
        cartItems.forEach(cartItem -> {
            subTotal[0] = cartItem.getTotalProductPrice() + subTotal[0];
            cartTotal[0] = cartItem.getTotalProductPrice() + cartTotal[0];
        });
        TextView subTotalTextView = view.findViewById(R.id.subTotal);
        TextView totalTextView = view.findViewById(R.id.cartTotal);
        TextView deliveryTotalTextView = view.findViewById(R.id.deliveryTotal);
        subTotalTextView.setText("Rs." + String.valueOf(subTotal[0]));
        totalTextView.setText("Rs." + String.valueOf(cartTotal[0]));
        deliveryTotalTextView.setText("Rs. 0.00");
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

            }
        });



    }


    public void loadOrders() {
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