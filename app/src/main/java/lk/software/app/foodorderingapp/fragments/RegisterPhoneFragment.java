package lk.software.app.foodorderingapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;

public class RegisterPhoneFragment extends Fragment {
    RegisterPhoneFragmentListener listener;

    public interface RegisterPhoneFragmentListener {
        void switchFragmentToVerifyOTPFragment(Fragment fragment);
    }

    private FirebaseAuth firebaseAuth;

    private String mobileVerificationId;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private ProgressDialog progressDialog;
    EditText otpNumber;
    private static RegisterPhoneFragment registerPhoneFragment;

    public RegisterPhoneFragment() {
        // Required empty public constructor
    }

    public static RegisterPhoneFragment getInstance() {
        if (registerPhoneFragment == null) {
            registerPhoneFragment = new RegisterPhoneFragment();
        }
        return registerPhoneFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        EditText editText = view.findViewById(R.id.editTextNumber);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editText.getText().toString();


                signInWithPhone(mobile);

            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.i(RegisterActivity.TAG, phoneAuthCredential.getSmsCode());
                Log.i(RegisterActivity.TAG, String.valueOf(phoneAuthCredential));

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e(RegisterActivity.TAG, e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(RegisterActivity.TAG, "verification_id" + s);
                Toast.makeText(getActivity(), "OTP code sent", Toast.LENGTH_SHORT).show();
                mobileVerificationId = s;
                resendingToken = forceResendingToken;
                listener.switchFragmentToVerifyOTPFragment(new VerfiyOTPFragment(mobileVerificationId));

            }
        };


    }

    private void signInWithPhone(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+94" + mobile)
                .setTimeout(90L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterPhoneFragmentListener) {
            listener = (RegisterPhoneFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_phone, container, false);
    }

}