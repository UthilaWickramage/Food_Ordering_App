package lk.software.app.foodorderingapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import lk.software.app.foodorderingapp.HomeActivity;
import lk.software.app.foodorderingapp.R;
import lk.software.app.foodorderingapp.RegisterActivity;
import lk.software.app.foodorderingapp.model.User;

public class RegisterEmailFragment extends Fragment {
    private static RegisterEmailFragment registerEmailFragment;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private RegisterEmailFragmentListener listener;

    public interface RegisterEmailFragmentListener {
        void updateUI(FirebaseUser user);
    }

    public static final String TAG = RegisterEmailFragment.class.getName();

    public RegisterEmailFragment() {
        // Required empty public constructor
    }

    public static RegisterEmailFragment getInstance() {
        if (registerEmailFragment == null) {
            registerEmailFragment = new RegisterEmailFragment();
        }
        return registerEmailFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = view.findViewById(R.id.editTextTextEmailAddress2);
        EditText passwordText = view.findViewById(R.id.editTextTextPassword2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        Button signup = view.findViewById(R.id.button);
        Button signin = view.findViewById(R.id.button3);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();
                String password = passwordText.getText().toString();

                if(email.isEmpty()){
                    editText.setError("email cannot be empty");
                    return;

                }

                if(password.length()<6){
                    passwordText.setError("password must be at least six character long");
                    return;
                }
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Sending Email");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "user created successfully");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                                    signup.setVisibility(View.GONE);

                                    signin.setVisibility(View.VISIBLE);
                                } else {
                                    Log.e(TAG, "error occurred");
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Problem with the email", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();
                String password = passwordText.getText().toString();
                progressDialog.setMessage("signing in");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    if (currentUser != null) {

                                        String person_email = currentUser.getEmail();
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
                                                                newUser.setRegister_date(saveCurrentDate);
                                                                newUser.setRegister_time(saveCurrentTime);

                                                                firebaseFirestore.collection("customers").document(currentUser.getUid()).set(newUser)
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(requireContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        });
                                                            }
                                                        }

                                                    }
                                                });
                                    } else {
                                        Toast.makeText(requireContext(), "No user", Toast.LENGTH_SHORT).show();
                                    }
                                    listener.updateUI(firebaseAuth.getCurrentUser());
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterEmailFragmentListener) {
            listener = (RegisterEmailFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_email, container, false);
        view.findViewById(R.id.button3).setVisibility(View.INVISIBLE);
        return view;
    }
}