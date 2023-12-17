package lk.software.app.foodorderingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
private static SearchFragment searchFragment;
FirebaseFirestore firebaseFirestore;
ArrayList<String> arrayList;
private static SearchFragmentListener searchFragmentListener;
public interface SearchFragmentListener{
    void passSearchText(String searchText);
}
    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }
    public static SearchFragment getInstance() {
        if(searchFragment==null){
            searchFragment=new SearchFragment();
        }

        return searchFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    firebaseFirestore = FirebaseFirestore.getInstance();
        AutoCompleteTextView editText = view.findViewById(R.id.editTextText);
        arrayList = new ArrayList<>();
        arrayList.clear();
        firebaseFirestore.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for(DocumentSnapshot snapshot: value.getDocuments()){
                            Product product = snapshot.toObject(Product.class);

                            arrayList.add(product.getName());
                        }

                    }
                });
        firebaseFirestore.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for(DocumentSnapshot snapshot: value.getDocuments()){
                            Category category = snapshot.toObject(Category.class);

                            arrayList.add(category.getName());
                        }

                    }
                });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,arrayList);
     editText.setAdapter(arrayAdapter);
editText.setThreshold(1);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){

                    String searchText = editText.getText().toString();
                   searchFragmentListener.passSearchText(searchText);
                    return true;
                }


                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragmentListener){
            searchFragmentListener = (SearchFragmentListener) context;
         }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}