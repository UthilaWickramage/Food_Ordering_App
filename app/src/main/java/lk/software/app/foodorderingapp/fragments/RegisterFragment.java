package lk.software.app.foodorderingapp.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;
import lk.software.app.foodorderingapp.model.User;
import lk.software.app.foodorderingapp.model.UserStatusEnum;

public class RegisterFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    private FragmentListener listener;
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;


    public interface FragmentListener {
        void switchFragment(Fragment fragment);

        void updateUIFromGoogleSignInToHome(FirebaseUser user);
    }

    private static RegisterFragment registerFragment;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment getInstance() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        return registerFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        signInClient = Identity.getSignInClient(getContext());
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        AppCompatButton signupWithEmail = view.findViewById(R.id.signup_btn_email);
        AppCompatButton signupWithPhone = view.findViewById(R.id.signu_up_button_phone);
        AppCompatButton googleBtn = view.findViewById(R.id.sign_up_with_google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId("546692353922-9n26ccmen1ivk911e11t5crmr4t30cal.apps.googleusercontent.com")

                        .build();
                Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
                signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
                        if(signResultLauncher!=null){
                            signResultLauncher.launch(intentSenderRequest);

                        }
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

    private void firebaseAuthWithGoogle(String googleIdToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleIdToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);

        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String person_name = currentUser.getDisplayName();
                        String person_email = currentUser.getEmail();
                        Uri profile_img = currentUser.getPhotoUrl();
                        String customer_profile_img = UUID.randomUUID().toString();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MM.dd.yyyy");
                        String saveCurrentDate = currentDate.format(calendar.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        String saveCurrentTime = currentTime.format(calendar.getTime());




                        firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            User user = task.getResult().toObject(User.class);
                                            if(user==null){
                                                User newUser = new User();
                                                newUser.setEmail(person_email);
                                                newUser.setFull_name(person_name);
                                                newUser.setRegister_date(saveCurrentDate);
                                                newUser.setRegister_time(saveCurrentTime);
                                                newUser.setStatus(UserStatusEnum.UNBLOCKED.toString());
                                                listener.updateUIFromGoogleSignInToHome(currentUser);
                                                firebaseFirestore.collection("customers").document(currentUser.getUid()).set(newUser)

                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(requireContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }else{
                                                if(user.getStatus()!=null){
                                                    if(user.getStatus().equals(UserStatusEnum.BLOCKED.toString())){
                                                        Toast.makeText(requireContext(),"Your account is suspended",Toast.LENGTH_SHORT).show();

                                                    }else{
                                                        listener.updateUIFromGoogleSignInToHome(currentUser);

                                                    }
                                                }
                                            }
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(RegisterActivity.TAG, "Something went wrong");

                                    }
                                });
                    }else {
                        Toast.makeText(requireContext(),"No user",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(RegisterActivity.TAG, e.getMessage());
            }
        });
    }

    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential signInCredentialFromIntent = signInClient.getSignInCredentialFromIntent(intent);

            String googleIdToken = signInCredentialFromIntent.getGoogleIdToken();

            firebaseAuthWithGoogle(googleIdToken);
        } catch (ApiException e) {
            Log.e(RegisterActivity.TAG, e.getMessage());
        }
    }

    private final ActivityResultLauncher<IntentSenderRequest> signResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            handleSignInResult(o.getData());
                        }
                    });
}