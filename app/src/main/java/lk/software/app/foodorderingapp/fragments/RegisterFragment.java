package lk.software.app.foodorderingapp.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;

public class RegisterFragment extends Fragment {

    private FragmentListener listener;
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;
     public interface FragmentListener{
        void switchFragment(Fragment fragment);
        void updateUIFromGoogleSignInToHome(FirebaseUser user);
    }
private static RegisterFragment registerFragment;
    public RegisterFragment() {
        // Required empty public constructor
    }
public static RegisterFragment getInstance(){
        if(registerFragment==null){
            registerFragment = new RegisterFragment();
        }
        return registerFragment;
}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       if(context instanceof FragmentListener){
           listener = (FragmentListener) context;
       }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        signInClient = Identity.getSignInClient(getContext());
        AppCompatButton signupWithEmail = view.findViewById(R.id.signup_btn_email);
        AppCompatButton signupWithPhone = view.findViewById(R.id.signu_up_button_phone);
        AppCompatButton googleBtn = view.findViewById(R.id.sign_up_with_google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .build();
                Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
                signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
                        signResultLauncher.launch(intentSenderRequest);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(RegisterActivity.TAG, e.getMessage());
                            }
                        });
            }
        });
        signupWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchFragment(RegisterEmailFragment.getInstance());
            }
        });
        signupWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchFragment(RegisterPhoneFragment.getInstance());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void firebaseAuthWithGoogle(String googleIdToken){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleIdToken,null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               listener.updateUIFromGoogleSignInToHome(firebaseAuth.getCurrentUser());
           }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(RegisterActivity.TAG, e.getMessage());
            }
        });
    }
    private void handleSignInResult(Intent intent){
        try{
            SignInCredential signInCredentialFromIntent = signInClient.getSignInCredentialFromIntent(intent);
       String googleIdToken = signInCredentialFromIntent.getGoogleIdToken();
       firebaseAuthWithGoogle(googleIdToken);
        }catch (ApiException e){
            Log.e(RegisterActivity.TAG, e.getMessage());
        }
    }
    private final ActivityResultLauncher<IntentSenderRequest> signResultLauncher=
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            handleSignInResult(o.getData());
                        }
                    });
}