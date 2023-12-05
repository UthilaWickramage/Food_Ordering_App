package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingapp.adapters.CategoryAdapter;
import lk.software.app.foodorderingapp.fragments.AccountFragment;
import lk.software.app.foodorderingapp.fragments.BrowseFragment;
import lk.software.app.foodorderingapp.fragments.CartFragment;
import lk.software.app.foodorderingapp.fragments.HomeFragment;
import lk.software.app.foodorderingapp.fragments.SearchFragment;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;
import lk.software.app.foodorderingapp.model.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener, SearchFragment.SearchFragmentListener, CategoryAdapter.AdapterListener {

    public static final String TAG = HomeActivity.class.getName();
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FirebaseUser currentUser;
    int searchContainer;
    int fragmentContainer;
    TextView textView;

    ImageView imageView;

    @Override
    protected void onStart() {
        super.onStart();
        loadFragment(searchContainer, SearchFragment.getInstance());
        loadFragment(fragmentContainer, HomeFragment.getInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        fragmentContainer = R.id.fragmentContainer;
        searchContainer = R.id.searchContainer;
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
textView = findViewById(R.id.address);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

if(currentUser!=null){
    loadDeliverToDetails();

}else{
    textView.setText("Select Location");
}

findViewById(R.id.home_profile_img).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(currentUser!=null){
            startActivity(new Intent(HomeActivity.this,AccountActivity.class));
        }
    }
});
        imageView = findViewById(R.id.home_profile_img);
        loadProfileImage();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Menu navMenu = navigationView.getMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentUser != null) {
                    navMenu.findItem(R.id.sideNavBrowseProduct).setVisible(true);
                    navMenu.findItem(R.id.sideNavCart).setVisible(true);
                    navMenu.findItem(R.id.sideNavOrder).setVisible(true);
                    navMenu.findItem(R.id.sideNavAccount).setVisible(true);
                    navMenu.findItem(R.id.sideNavLogout).setVisible(true);
                    navMenu.findItem(R.id.sideNavLogin).setVisible(false);

                } else {
                    navMenu.findItem(R.id.sideNavLogin).setVisible(true);
                }
                drawerLayout.open();
            }


        });

        loadFragment(searchContainer, SearchFragment.getInstance());
        loadFragment(fragmentContainer, HomeFragment.getInstance());
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        findViewById(R.id.linearLayoutLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    startActivity(new Intent(HomeActivity.this,LocationActivity.class));

                }else{
                    Toast.makeText(HomeActivity.this,"Login first",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadDeliverToDetails() {

        firebaseFirestore.collection("customers").document(currentUser.getUid()).get()

                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       User user = documentSnapshot.toObject(User.class);
if(user!=null){
    textView.setText(user.getCity()+","+user.getArea());

}
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(HomeActivity.TAG,"failed");
                    }
                });

    }

    private void loadProfileImage() {
        if (currentUser != null) {
            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {

                                    if (user.getProfile_img() != null) {
                                        firebaseStorage.getReference("profileImages/" + user.getProfile_img()).getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Transformation transformation = new MaskTransformation(HomeActivity.this, R.drawable.profile_image_background);
                                                        Picasso.get().load(uri).transform(transformation).centerCrop()
                                                                .resize(40, 40).into(imageView);
                                                    }
                                                });
                                    } else {
                                        Log.d(RegisterActivity.TAG, "not successful");

                                    }
                                } else {
                                    Log.d(RegisterActivity.TAG, "not successful");

                                }
                            } else {
                                Log.d(RegisterActivity.TAG, "not successful");
                            }
                        }


                    });
        } else {
            Log.d(RegisterActivity.TAG, "no user");

        }
    }

    private void loadFragment(int fragmentContainerView, Fragment
            fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.bottomNavBrowsing) {
            loadFragment(fragmentContainer, BrowseFragment.getInstance());
            loadFragment(searchContainer, SearchFragment.getInstance());
        } else if (itemId == R.id.bottomNavCart) {
            if(currentUser!=null){
                loadFragment(fragmentContainer, CartFragment.getInstance());
                removeFragment(SearchFragment.getInstance());
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Go to Login?");
                alertDialog.setMessage("You need to login to create a cart");
                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.show();
            }

        } else if (itemId == R.id.bottomNavProfile) {
            if(currentUser!=null){
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Go to Login?");
                alertDialog.setMessage("You need to login to view your account");
                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.show();
            }


        } else {
            loadFragment(fragmentContainer, HomeFragment.getInstance());
            loadFragment(searchContainer, SearchFragment.getInstance());
        }



        if (itemId == R.id.sideNavHome) {
            loadFragment(fragmentContainer, HomeFragment.getInstance());
            loadFragment(searchContainer, SearchFragment.getInstance());
        } else if (itemId == R.id.sideNavBrowseProduct) {
            loadFragment(fragmentContainer, BrowseFragment.getInstance());
            loadFragment(searchContainer, SearchFragment.getInstance());
        } else if (itemId == R.id.sideNavCart) {
            if(currentUser!=null){
                loadFragment(fragmentContainer, CartFragment.getInstance());
                removeFragment(SearchFragment.getInstance());
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Go to Login?");
                alertDialog.setMessage("You need to login to create a cart");
                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.show();
            }

        } else if (itemId == R.id.sideNavOrder) {
            if(currentUser!=null){
                startActivity(new Intent(HomeActivity.this,AccountActivity.class));

            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Go to Login?");
                alertDialog.setMessage("You need to login to view your orders");
                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.show();
            }

        } else if (itemId == R.id.sideNavAccount) {
            if(currentUser!=null){
                startActivity(new Intent(HomeActivity.this,AccountActivity.class));

            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Go to Login?");
                alertDialog.setMessage("You need to login to view your account");
                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.show();
            }
        } else if(itemId==R.id.sideNavLogin) {
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));

        }else if(itemId==R.id.sideNavLogout) {
            Log.e(RegisterActivity.TAG,"Working logout");
            firebaseAuth.signOut();
            recreate();

        }

        return true;
    }

    @Override
    public void passSearchText(String searchText) {
        loadFragment(fragmentContainer, new BrowseFragment(searchText));
    }


    @Override
    public void passCategory(String name) {
        loadFragment(fragmentContainer, new BrowseFragment(name));

    }


}


