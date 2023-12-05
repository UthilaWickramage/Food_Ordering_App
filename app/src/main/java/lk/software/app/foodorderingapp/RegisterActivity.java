package lk.software.app.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import lk.software.app.foodorderingapp.fragments.RegisterEmailFragment;
import lk.software.app.foodorderingapp.fragments.RegisterFragment;
import lk.software.app.foodorderingapp.fragments.RegisterPhoneFragment;
import lk.software.app.foodorderingapp.fragments.VerfiyOTPFragment;
import lk.software.app.foodorderingapp.receivers.OTPReceiver;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.FragmentListener, RegisterPhoneFragment.RegisterPhoneFragmentListener, RegisterEmailFragment.RegisterEmailFragmentListener, VerfiyOTPFragment.VerifyOTPFragmentListener {
int fragmentContainerView;
public static final String TAG = RegisterActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       fragmentContainerView = R.id.registerFragmentContainer;

       switchFragment(RegisterFragment.getInstance());
        ImageView imageView = findViewById(R.id.go_back_registerFragment);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(RegisterFragment.getInstance());
            }
        });

        findViewById(R.id.singinTextview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }



    @Override
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void updateUIFromGoogleSignInToHome(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(RegisterActivity.this,AccountActivity.class));
            finish();
        }
    }

    @Override
    public void updateUItoHome(FirebaseUser user) {
        startActivity(new Intent(RegisterActivity.this, AccountActivity.class));
        finish();
    }

    @Override
    public void registerOTPReceiver(OTPReceiver receiver, IntentFilter intentFilter) {
        registerReceiver(receiver,intentFilter);

    }

    @Override
    public void requestSMSPermissions() {
        String smsPermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(getApplicationContext(), smsPermission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = new String[1];
            permissionList[0] = smsPermission;
            ActivityCompat.requestPermissions(this, permissionList, 1);

        }
    }

    @Override
    public void switchFragmentToVerifyOTPFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
                Toast.makeText(RegisterActivity.this, "Please verify or email", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(RegisterActivity.this, AccountActivity.class);

            startActivity(intent);
            finish();
        }
    }
}