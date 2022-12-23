package com.example.datingapp2.ui.profile;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.datingapp2.R;
import com.example.datingapp2.ui.login_register.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //private ProfileViewModel notificationsViewModel;

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference currentUserDB;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    private StorageReference filePathProfileImages;

    private View viewRoot;
    private ImageView profileImage1, profileImage2, profileImage3, profileImage4;
    private TextView tvName;
    private Button btnSettings, btnEdit, btnLogout;

    int profileImageClicked = 0;
    private Uri resultUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //notificationsViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        this.viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.getString("UserName", "empty").equals("empty")) {
            editor.remove("UserName");
        }

        initFirebase();

        initView();

        setupListener();

        //final TextView tvProfile = root.findViewById(R.id.tvProfile);

//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                tvProfile.setText(s);
//            }
//        });

        return viewRoot;
    }


    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            this.userID = mAuth.getCurrentUser().getUid();
        } else {
            // Handle failures
            // ...
            // Go back to Login Activity.
        }

        this.currentUserDB = root.child(this.userID);
        getUserData();
    }

    private void getUserData() {
        currentUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    editor.putString("UserName", dataSnapshot.child("UserName").getValue().toString());
                    editor.commit();

                    String profileImagesUrl = "";

                    if (dataSnapshot.child("profileImagesUrl").getValue() != null) {
                        profileImagesUrl = dataSnapshot.child("profileImagesUrl").getValue().toString();
                        Picasso.get().load(profileImagesUrl).into(profileImage1);
                    } else {
                        Picasso.get().load(R.drawable.defaultavatar).into(profileImage1);
                    }

                    String nickname = dataSnapshot.child("NickName").getValue().toString();
                    String age = dataSnapshot.child("Age").getValue().toString();
                    tvName.setText(nickname + ", " + age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        profileImage1 = this.viewRoot.findViewById(R.id.profileImage1);
        tvName = this.viewRoot.findViewById(R.id.tvName);
        btnSettings = this.viewRoot.findViewById(R.id.btnSettings);
        btnEdit = this.viewRoot.findViewById(R.id.btnEdit);
        btnLogout = this.viewRoot.findViewById(R.id.btnLogout);

        /*profileImage2 = this.viewRoot.findViewById(R.id.profileImage2);
        profileImage2.setOnClickListener(this);

        profileImage3 = this.viewRoot.findViewById(R.id.profileImage3);
        profileImage3.setOnClickListener(this);

        profileImage4 = this.viewRoot.findViewById(R.id.profileImage4);
        profileImage4.setOnClickListener(this);*/
    }

    private void setupListener() {
        profileImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageClicked = 1;

                if (!checkPermissions()) {
                    Toast.makeText(getContext(), "Please allow access to continue!", Toast.LENGTH_SHORT).show();
                    requestPermissions();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    if (profileImageClicked == 1) {
                        startActivityForResult(intent, 1);
                    } /*else if (profileImageClicked == 2) {
                        startActivityForResult(intent, 2);
                    } else if (profileImageClicked == 3) {
                        startActivityForResult(intent, 3);
                    } else if (profileImageClicked == 4) {
                        startActivityForResult(intent, 4);
                    }*/
                }
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent2);
                getActivity().getFragmentManager().popBackStack();
                return;
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().getFragmentManager().popBackStack();
                return;
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent3);
                getActivity().getFragmentManager().popBackStack();
                mAuth.signOut();
                return;
            }
        });
    }

    public boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                if (this.profileImageClicked == 1) {
                    startActivityForResult(intent, 1);
                } /*else if (this.profileImageClicked == 2) {
                    startActivityForResult(intent, 2);
                } else if (this.profileImageClicked == 3) {
                    startActivityForResult(intent, 3);
                } else if (this.profileImageClicked == 4) {
                    startActivityForResult(intent, 4);
                }*/
            } else {
                Toast.makeText(getContext(), "Please allow access to continue!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                this.resultUri = imageUri;

                if (this.resultUri != null) {
                    profileImage1.setImageURI(resultUri);
                    //Glide.clear(profileImage1);
                    Glide.with(getContext()).load(resultUri).into(profileImage1);

                    uploadProfileImagesToFirebase();
                }
            }
        } /*else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                this.resultUri = imageUri;

                if (this.resultUri != null) {
                    profileImage2.setImageURI(resultUri);
                    Glide.clear(profileImage2);
                    Glide.with(getContext()).load(resultUri).into(profileImage2);

                    uploadProfileImagesToFirebase();
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                this.resultUri = imageUri;

                if (this.resultUri != null) {
                    profileImage3.setImageURI(resultUri);
                    Glide.clear(profileImage3);
                    Glide.with(getContext()).load(resultUri).into(profileImage3);

                    uploadProfileImagesToFirebase();
                }
            }
        } else if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                this.resultUri = imageUri;

                if (this.resultUri != null) {
                    profileImage4.setImageURI(resultUri);
                    Glide.clear(profileImage4);
                    Glide.with(getContext()).load(resultUri).into(profileImage4);

                    uploadProfileImagesToFirebase();
                }
            }
        }*/
    }

    private void uploadProfileImagesToFirebase() {
        // Upload
        String imageName = this.resultUri.getLastPathSegment();

        this.filePathProfileImages = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(this.userID)
                .child(this.userID);

        UploadTask uploadTask = filePathProfileImages.putFile(this.resultUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filePathProfileImages.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    //String imgName = resultUri.getLastPathSegment();

                    //currentUserDB.child("profileImagesUrl").child(imgName).setValue(downloadUri.toString());
                    currentUserDB.child("profileImagesUrl").setValue(downloadUri.toString());

                    //
                    Map<String, Object> userinfo = new HashMap<>();
                    userinfo.put("profileImagesUrl", downloadUri);

                    FirebaseDatabase.getInstance().getReference()
                            .child("UsersClone")
                            .child(sharedPref.getString("UserName", "empty"))
                            .updateChildren(userinfo);

                    Toast.makeText(getContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(getContext(), "Uploaded failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}