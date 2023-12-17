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

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
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
private String searchText = "";
    private ArrayList<Product> products;

    private ProductAdapter productAdapter;

    private static BrowseFragment browseFragment;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public BrowseFragment(String searchText) {

        this.searchText = searchText;

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
        if(!this.searchText.isEmpty()){
            loadProductsWithCondition(view);
        }else{
            loadProducts();
        }

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
        CollectionReference collectionReference = firebaseFirestore.collection("products");

                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        products.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Product product = snapshot.toObject(Product.class);
                            product.setProductDocumentId(snapshot.getId());
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }
    public void loadProductsWithCondition(View view) {
        CollectionReference collectionReference = firebaseFirestore.collection("products");
       collectionReference.where(Filter.or(
               Filter.equalTo("category_name",searchText),
               Filter.equalTo("name",searchText)
                       ))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        products.clear();
                        if(!value.getDocuments().isEmpty()){
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Product product = snapshot.toObject(Product.class);
                                product.setProductDocumentId(snapshot.getId());
                                products.add(product);
                            }
                        }else{
                            LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie);

                            lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }
}
