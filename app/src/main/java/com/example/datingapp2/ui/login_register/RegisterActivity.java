package com.example.datingapp2.ui.login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datingapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userID;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Users");
    private DatabaseReference currentUserDB;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private static final String adminID = "WCXTevD8pBf5cJ5cPCZlIIUrc0q2";
    private DatabaseReference adminDB = root.child(adminID).child("users");

    private Context mContext = this;
    private EditText edtUserName, edtPassword, edtPhoneNumber, edtNickName, edtAge;
    private CheckBox cbxAgree;
    private TextView tvAgree, tvExistingUser;
    private Button btnRegister;

//    private String emailRegex = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//    private Pattern emailPattern = Pattern.compile(emailRegex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFirebase();

        initView();

        setupListener();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
//                    Toast.makeText(RegisterActivity.this, "LoginActivity onAuthStateChanged", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(mContext, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    return;
                }
            }
        };
    }

    private void initView() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);

        edtNickName = findViewById(R.id.edtNickName);
        edtAge = findViewById(R.id.edtAge);

        cbxAgree = findViewById(R.id.cbxAgree);
        tvAgree = findViewById(R.id.tvAgree);
        tvAgree.setText(Html.fromHtml("I have read and agree to the " + "<a href = 'https://nguyentruonghanblog.blogspot.com/2022/10/policy-han-nguyen-built-dating-app-app.html'> Terms of Conditions</a>"));
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());

        btnRegister = findViewById(R.id.btnRegister);

        tvExistingUser = findViewById(R.id.tvExistingUser);
    }

    private void setupListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = edtUserName.getText().toString().trim();
                String Password = edtPassword.getText().toString().trim();
                String PhoneNumber = edtPhoneNumber.getText().toString().trim();

                String NickName = edtNickName.getText().toString().trim();
                String Age = edtAge.getText().toString().trim();

                Boolean isAgree = cbxAgree.isChecked();

                if (validateInput(UserName, Password, PhoneNumber, NickName, Age, isAgree)) {
                    String UserName_fakeEmail = UserName + "@lucky.com";

                    mAuth.createUserWithEmailAndPassword(UserName_fakeEmail, Password)
                            .addOnCompleteListener(
                                    RegisterActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Store to Firebase
                                                userID = mAuth.getCurrentUser().getUid();

                                                currentUserDB = root.child(userID);

                                                Map userInfo = new HashMap<>();
                                                userInfo.put("UserName", UserName);
                                                userInfo.put("PassWord", Password);
                                                userInfo.put("PhoneNumber", PhoneNumber);
                                                userInfo.put("NickName", NickName);
                                                userInfo.put("Age", Age);
                                                userInfo.put("profileImagesUrl", "https://firebasestorage.googleapis.com/v0/b/datingapp-135e3.appspot.com/o/profileImages%2FdefaultImageAvatar%2Fdefaultavatar.png?alt=media&token=ffbc138f-64eb-4c79-9515-04cf090e423c");

                                                //
                                                currentUserDB.updateChildren(userInfo);

                                                //
                                                adminDB.child(UserName).setValue(userID);

                                                //
                                                Map<String, Object> userinfo = new HashMap<>();
                                                userinfo.put("ID", userID);
                                                userinfo.put("UserName", UserName);
                                                userinfo.put("profileImagesUrl", "https://firebasestorage.googleapis.com/v0/b/datingapp-135e3.appspot.com/o/profileImages%2FdefaultImageAvatar%2Fdefaultavatar.png?alt=media&token=ffbc138f-64eb-4c79-9515-04cf090e423c");
                                                userinfo.put("NickName", NickName);
                                                userinfo.put("Age", Age);

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("UsersClone")
                                                        .child(UserName)
                                                        .updateChildren(userinfo);

                                                // Success
                                                Toast.makeText(mContext, "Signed up successfully", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                                return;
                                            }
                                        }
                                    });
                }
            }
        });

        tvExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean validateInput(String UserName, String Password, String PhoneNumber, String NickName, String Age, Boolean isAgree) {
        if (UserName == null || UserName.isEmpty()) {
            Toast.makeText(this, "User name must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Password == null || Password.isEmpty()) {
            Toast.makeText(this, "Password must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        } else if (PhoneNumber == null || PhoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone number must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        } else if (NickName == null || NickName.isEmpty()) {
            Toast.makeText(this, "NickName number must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Age == null || Age.isEmpty()) {
            Toast.makeText(this, "Age number must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }

            /*if (Email == null || Email.isEmpty()) {
                Toast.makeText(this, "Email must be filled out", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Matcher matcher = emailPattern.matcher(Email);
                if (!matcher.matches()) {
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }*/

        if (isAgree == false) {
            Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
