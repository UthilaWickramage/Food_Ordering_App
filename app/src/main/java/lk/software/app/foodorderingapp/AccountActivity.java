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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    public static final int EMAIL_CODE = 0;
    public static final int PHONE_CODE = 1;
    public static final int NAME_CODE = 2;

    private FirebaseStorage firebaseStorage;
    EditText txt;
    FirebaseUser currentUser;
    private Uri imagePath;
    ImageButton profile_img;
    User user;
    ProgressDialog progressDialog;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        requestCallPermissions();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(AccountActivity.this);
        TextView name = findViewById(R.id.textView19);
        TextView email = findViewById(R.id.textView30);
        TextView phone = findViewById(R.id.textView23);
        TextView textView = findViewById(R.id.textView12);
        FrameLayout frameLayout = findViewById(R.id.frameLayout2);
        View view = getLayoutInflater().inflate(R.layout.call_banner, frameLayout, true);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: 0718263689"));
                startActivity(intent);
            }
        });
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
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, LocationActivity.class));
            }
        });
        findViewById(R.id.constraintLayout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
        firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().toObject(User.class);
                            if (user != null) {

                                textView.setText(user.getFull_name());
                                if (user.getFull_name() != null) {
                                    name.setText(user.getFull_name());
                                }
                                if (user.getProfile_img() != null) {
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

                showBottomSheetDialog(NAME_CODE);
            }


        });
        findViewById(R.id.imageView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(PHONE_CODE);

            }
        });

        findViewById(R.id.imageView15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(EMAIL_CODE);

            }
        });

    }

    private void showBottomSheetDialog(int CODE) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        //ConstraintLayout constraint = bottomSheetDialog.findViewById(R.id.constraint);
        EditText input = bottomSheetDialog.findViewById(R.id.editTextText3);
        Button save = bottomSheetDialog.findViewById(R.id.button5);
        Button cancel = bottomSheetDialog.findViewById(R.id.button6);

        save.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String value = input.getText().toString();
                if (input.getText().toString().isEmpty()) {
                    input.setError("Please enter value");
                    return;
                }
                if (CODE == 0) {

                    updateUserEmail(value);
                }
                if (CODE == 1) {
                    updateUserPhone(value);
                }
                if (CODE == 2) {
                    updateUserName(value);
                }
                bottomSheetDialog.dismiss();
                Toast.makeText(AccountActivity.this, "Profile update Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
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

    public void requestCallPermissions() {
        String callPermission = Manifest.permission.CALL_PHONE;
        int grant = ContextCompat.checkSelfPermission(getApplicationContext(), callPermission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = new String[1];
            permissionList[0] = callPermission;
            ActivityCompat.requestPermissions(this, permissionList, 1);

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
                                    if (user.getProfile_img() == null) {
                                        imageId = UUID.randomUUID().toString();
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

