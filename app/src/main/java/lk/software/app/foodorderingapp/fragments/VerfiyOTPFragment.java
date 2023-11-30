package lk.software.app.foodorderingapp.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.receivers.OTPReceiver;

public class VerfiyOTPFragment extends Fragment {
    private VerifyOTPFragmentListener verifyOTPFragmentListener;
    private static VerfiyOTPFragment verifyOTPFragment;

    private FirebaseAuth firebaseAuth;
    private static String mobileVerificationId;

    private ProgressDialog progressDialog;

    public interface VerifyOTPFragmentListener {

        void updateUItoHome(FirebaseUser user);

        void registerOTPReceiver(OTPReceiver receiver, IntentFilter intentFilter);

        void requestSMSPermissions();
    }

    public VerfiyOTPFragment() {
        // Required empty public constructor
    }

    public VerfiyOTPFragment(String verificationId) {
        mobileVerificationId = verificationId;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        verifyOTPFragmentListener.requestSMSPermissions();
        firebaseAuth = FirebaseAuth.getInstance();
        EditText editText = view.findViewById(R.id.editTextNumber2);
        Button button = view.findViewById(R.id.button2);
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        OTPReceiver otpReceiver = new OTPReceiver();
        verifyOTPFragmentListener.registerOTPReceiver(otpReceiver, intentFilter);
        otpReceiver.setEditText(editText, button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Verifying...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                verifyOTP(editText.getText().toString());
            }
        });
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mobileVerificationId, otp);
        signInWithPhoneAuth(credential);
    }

    private void signInWithPhoneAuth(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            AuthResult authResult = task.getResult();
                            FirebaseUser user = authResult.getUser();
                            verifyOTPFragmentListener.updateUItoHome(user);
                        }
                    }
                });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof VerifyOTPFragmentListener) {
            verifyOTPFragmentListener = (VerifyOTPFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verfiy_o_t_p, container, false);
    }
}