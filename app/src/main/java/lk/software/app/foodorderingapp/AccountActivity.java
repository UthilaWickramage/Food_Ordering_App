package lk.software.app.foodorderingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingapp.model.User;

public class AccountActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private FirebaseStorage firebaseStorage;
    EditText txt;
    FirebaseUser currentUser;
    private Uri imagePath;
    ImageButton profile_img;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(AccountActivity.this);
        TextView name = findViewById(R.id.textView19);
        TextView email = findViewById(R.id.textView30);
        TextView phone = findViewById(R.id.textView23);
        TextView textView = findViewById(R.id.textView12);
        profile_img = findViewById(R.id.imageView4);
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });


        firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null) {
                                textView.setText(user.getFull_name());
                                if (user.getFull_name() != null) {
                                    name.setText(user.getFull_name());
                                }
                                if(user.getProfile_img()!=null){
                                    firebaseStorage.getReference("profileImages/" + user.getProfile_img()).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Transformation transformation = new MaskTransformation(AccountActivity.this, R.drawable.profile_image_background);
                                                    Picasso.get().load(uri).transform(transformation).centerCrop()
                                                            .resize(100, 100).into(profile_img);

                                                }
                                            });
                                }
                                if (user.getPhone() != null) {
                                    phone.setText(user.getPhone());
                                }
                                if (user.getEmail() != null) {
                                    email.setText(user.getEmail());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "User null", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

        findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(AccountActivity.this);
                final EditText editTextName1 = new EditText(AccountActivity.this);
                alertName.setTitle("Edit your name");

                alertName.setView(editTextName1);

                editTextName1.setBackgroundResource(R.drawable.text_field);
                alertName.setMessage("Enter your name");


                alertName.setView(editTextName1);

                alertName.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editTextName1.getText().toString();// variable to collect user input
                                updateUserName(s);
                            }
                        }
                );

                alertName.show();

            }


        });
        findViewById(R.id.imageView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(RegisterActivity.TAG, currentUser.getProviderData().get(0).getProviderId());
//                if (currentUser.getProviderData().get(0).getProviderId().equals("Phone")) {
//                    Toast.makeText(AccountActivity.this, "You can't edit your phone number", Toast.LENGTH_LONG).show();
//                    return;
//                }
                AlertDialog.Builder alertName = new AlertDialog.Builder(AccountActivity.this);
                final EditText editTextName1 = new EditText(AccountActivity.this);
                alertName.setTitle("Edit your Phone Number");

                alertName.setView(editTextName1);

                editTextName1.setBackgroundResource(R.drawable.text_field);
                alertName.setMessage("Enter your phone number");


                alertName.setView(editTextName1);

                alertName.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editTextName1.getText().toString();// variable to collect user input
                                updateUserPhone(s);
                            }
                        }
                );

                alertName.show();

            }
        });

        findViewById(R.id.imageView15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(RegisterActivity.TAG, firebaseAuth.getAccessToken(false).getResult().getSignInProvider());
//                if (firebaseAuth.getAccessToken(false).getResult().getSignInProvider().equals("password") || firebaseAuth.getAccessToken(false).getResult().getSignInProvider().equals("google.com")) {
//                    Toast.makeText(AccountActivity.this, "You can't edit your email address", Toast.LENGTH_LONG).show();
//                    return;
//                }
                AlertDialog.Builder alertName = new AlertDialog.Builder(AccountActivity.this);
                final EditText editTextName1 = new EditText(AccountActivity.this);
                alertName.setTitle("Edit your Email");

                alertName.setView(editTextName1);

                editTextName1.setBackgroundResource(R.drawable.text_field);
                alertName.setMessage("Enter your email address");


                alertName.setView(editTextName1);

                alertName.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = editTextName1.getText().toString();// variable to collect user input
                                updateUserEmail(s);
                            }
                        }
                );

                alertName.show();
            }
        });

    }


    private void updateUserName(String getInput) {

        if (currentUser != null) {


            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    user.setFull_name(getInput);
                                    updateUsersCollection(user);
                                }
                            }

                        }
                    });
        }
    }

    private void updateUserEmail(String getInput) {

        if (currentUser != null) {


            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    user.setEmail(getInput);


                                    updateUsersCollection(user);
                                }
                            }

                        }
                    });
        }
    }

    private void updateUserPhone(String getInput) {

        if (currentUser != null) {


            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    user.setPhone(getInput);


                                    updateUsersCollection(user);
                                }
                            }

                        }
                    });
        }


    }

    private void updateUserPicture() {

        if (currentUser != null) {


            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                String imageId;
                                if (user != null) {
                                    if(user.getProfile_img()==null){
                                       imageId= UUID.randomUUID().toString();
                                        user.setProfile_img(imageId);
                                    }
                                    imageId = user.getProfile_img();

                                    updateUsersCollection(user);


                                        if (imagePath != null) {

                                            StorageReference reference = firebaseStorage.getReference("profileImages")
                                                    .child(imageId);
                                            reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    progressDialog.dismiss();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                    progressDialog.setMessage("Uploading... " + (int) progress + "% done");
                                                }
                                            });
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AccountActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }


                    });
        }


    }

    private void updateUsersCollection(User user) {
        firebaseFirestore.collection("customers").document(currentUser.getUid()).set(user);


    }

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null) {

                        imagePath = result.getData().getData();
                        Transformation transformation = new MaskTransformation(AccountActivity.this, R.drawable.profile_image_background);
                        Picasso.get().load(imagePath).transform(transformation).centerCrop()
                                .resize(100, 100).into(profile_img);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountActivity.this);
                        alertDialog.setTitle("You selected Profile Picture");
                        alertDialog.setMessage("Do you want to upload this profile picture");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog.setMessage("Uploading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                updateUserPicture();

                            }
                        });
                        alertDialog.show();

                    } else {
                        Toast.makeText(AccountActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }

                }
            }
    );
}

