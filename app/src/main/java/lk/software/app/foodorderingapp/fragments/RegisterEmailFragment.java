package lk.software.app.foodorderingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lk.software.app.foodorderingapp.HomeActivity;
import lk.software.app.foodorderingapp.R;

public class RegisterEmailFragment extends Fragment {
    private static RegisterEmailFragment registerEmailFragment;
    FirebaseAuth firebaseAuth;
    private RegisterEmailFragmentListener listener;
    public interface RegisterEmailFragmentListener{
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();
                String password = passwordText.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "user created successfully");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    Toast.makeText(getActivity(), "Please verify your email", Toast.LENGTH_LONG).show();
                                    listener.updateUI(user);
                                } else {
                                    Log.e(TAG, "error occurred");

                                    Toast.makeText(getContext(), "User creation failed", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegisterEmailFragmentListener){
            listener = (RegisterEmailFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_email, container, false);
    }
}