package lk.software.app.foodorderingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.adapters.CategoryAdapter;
import lk.software.app.foodorderingapp.adapters.ProductAdapter;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

public class BrowseFragment extends Fragment {
    public static final String TAG = BrowseFragment.class.getName();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private ArrayList<Product> products;

    private ProductAdapter productAdapter;

    private static BrowseFragment browseFragment;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment getInstance() {
        if (browseFragment == null) {
            browseFragment = new BrowseFragment();
        }
        return browseFragment;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView productRecycleView = view.findViewById(R.id.recyclerViewBrowseProducts);
        products = new ArrayList<>();
        loadProducts();
        productAdapter = new ProductAdapter(requireActivity().getApplicationContext(), firebaseStorage, products);
        GridLayoutManager productGridLayoutManger = new GridLayoutManager(requireActivity().getApplicationContext(), 2);
        Log.d(TAG, String.valueOf(requireActivity().getApplicationContext()));

        productRecycleView.setLayoutManager(productGridLayoutManger);
        productRecycleView.setAdapter(productAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        return view;
    }

    public void loadProducts() {
        firebaseFirestore.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        products.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Product product = snapshot.toObject(Product.class);
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }
}
