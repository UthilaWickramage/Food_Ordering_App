package lk.software.app.foodorderingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingapp.HomeActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.adapters.CategoryAdapter;
import lk.software.app.foodorderingapp.adapters.ProductAdapter;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

public class HomeFragment extends Fragment{

    public static final String TAG = HomeFragment.class.getName();

private FirebaseFirestore firebaseFirestore;
private FirebaseStorage firebaseStorage;
private ArrayList<Category> categories;
private ArrayList<Product> products;

private CategoryAdapter categoryAdapter;

private ProductAdapter productAdapter;
private static HomeFragment homeFragment;

public interface HomeFragmentListener{
    void switchToBrowseFragment(String name);
}
    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static HomeFragment getInstance() {
        if(homeFragment==null){
            homeFragment = new HomeFragment();
        }

        return homeFragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        RecyclerView categoryRecyclerView = view.findViewById(R.id.recycleView1);
        loadCategories();
        categoryAdapter = new CategoryAdapter(getContext(),firebaseStorage,categories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        Log.d(TAG, String.valueOf(requireActivity().getApplicationContext()));
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);

        RecyclerView productRecycleView = view.findViewById(R.id.recycleView2);
        products = new ArrayList<>();
        loadProducts();
        productAdapter = new ProductAdapter(requireActivity().getApplicationContext(),firebaseStorage,products);
        LinearLayoutManager newProductLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        Log.d(TAG, String.valueOf(requireActivity().getApplicationContext()));
        newProductLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        productRecycleView.setLayoutManager(newProductLayoutManager);
        productRecycleView.setAdapter(productAdapter);

        FrameLayout frameLayout = view.findViewById(R.id.frameLayout);
        getLayoutInflater().inflate(R.layout.home_banner,frameLayout,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        return view;
    }

    public void loadCategories(){
firebaseFirestore.collection("categories")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            categories.clear();
            for(DocumentSnapshot snapshot: value.getDocuments()){
                Category category = snapshot.toObject(Category.class);
                categories.add(category);
            }
            categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    public void loadProducts(){
        firebaseFirestore.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        products.clear();
                        for(DocumentSnapshot snapshot: value.getDocuments()){
                            Product product = snapshot.toObject(Product.class);
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }



}