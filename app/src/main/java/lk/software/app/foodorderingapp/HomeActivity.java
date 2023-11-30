package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import lk.software.app.foodorderingapp.adapters.CategoryAdapter;
import lk.software.app.foodorderingapp.fragments.AccountFragment;
import lk.software.app.foodorderingapp.fragments.BrowseFragment;
import lk.software.app.foodorderingapp.fragments.CartFragment;
import lk.software.app.foodorderingapp.fragments.HomeFragment;
import lk.software.app.foodorderingapp.fragments.SearchFragment;
import lk.software.app.foodorderingapp.model.Category;
import lk.software.app.foodorderingapp.model.Product;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener, SearchFragment.SearchFragmentListener ,CategoryAdapter.AdapterListener {

    public static final String TAG = HomeActivity.class.getName();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    int searchContainer;
    int fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentContainer = R.id.fragmentContainer;
        searchContainer = R.id.searchContainer;
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }


        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        loadFragment(searchContainer,SearchFragment.getInstance());
        loadFragment(fragmentContainer,HomeFragment.getInstance());
    }



    private void loadFragment(int fragmentContainerView, Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
    private void removeFragment(Fragment fragment){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId==R.id.bottomNavBrowsing) {
            loadFragment(fragmentContainer,BrowseFragment.getInstance());
          loadFragment(searchContainer,SearchFragment.getInstance());
        }else if(itemId == R.id.bottomNavCart) {
            loadFragment(fragmentContainer,CartFragment.getInstance());
            removeFragment(SearchFragment.getInstance());
        }else if(itemId==R.id.bottomNavProfile) {
            startActivity(new Intent(HomeActivity.this,AccountActivity.class));

        }else {
            loadFragment(fragmentContainer,HomeFragment.getInstance());
            loadFragment(searchContainer,SearchFragment.getInstance());
        }

        return true;
    }

    @Override
    public void passSearchText(String searchText) {
        loadFragment(fragmentContainer,new BrowseFragment(searchText));
    }




    @Override
    public void passCategory(String name) {
        loadFragment(fragmentContainer,new BrowseFragment(name));

    }
}


