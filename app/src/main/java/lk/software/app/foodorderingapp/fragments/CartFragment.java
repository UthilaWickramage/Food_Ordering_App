package lk.software.app.foodorderingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import lk.software.app.foodorderingapp.adapters.OrderAdapter;
import lk.software.app.foodorderingapp.model.Order_Item;


public class CartFragment extends Fragment {
    ArrayList<Order_Item> order_items;
    LayoutInflater layoutInflater;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    OrderAdapter orderAdapter;
    FirebaseStorage firebaseStorage;
    private static CartFragment cartFragment;
    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment getInstance(){
        if(cartFragment==null){
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
        order_items = new ArrayList<>();
        loadOrders();
        orderAdapter = new OrderAdapter(requireContext(), firebaseStorage, order_items);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);



        view.findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }




    public void loadOrders() {
        firebaseFirestore.collection("orders").document(firebaseAuth.getCurrentUser().getUid()).collection("order_items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                Order_Item orderItems = snapshot.toObject(Order_Item.class);
                                order_items.add(orderItems);

                            }
                            orderAdapter.notifyDataSetChanged();
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