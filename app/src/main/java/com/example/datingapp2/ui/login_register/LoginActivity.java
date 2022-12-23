package com.example.datingapp2.ui.login_register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datingapp2.MainActivity;
import com.example.datingapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Users");
    DatabaseReference currentUserDB;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private Context mContext = this;
    private TextInputEditText edtUserName, edtPassword;
    private Button btnLogin;
    private TextView tvTroubleLogging, tvSignUp, tvNeedHelp, tv1, tv2, tv3;

    private String emailPattern = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.getString("UserName", "empty").equals("empty")) {
            editor.remove("UserName");
        }

        if (!sharedPref.getString("ChatWith", "empty").equals("empty")) {
            editor.remove("ChatWith");
        }

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
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    return;
                }
            }
        };
        mAuth.signOut();
    }

    private void initView() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvTroubleLogging = findViewById(R.id.tvTroubleLogging);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvNeedHelp = findViewById(R.id.tvNeedHelp);

        tv1 = findViewById(R.id.tv1);
        CharSequence text1 = Html.fromHtml("Khi bấm Đăng nhâp, bạn đồng ý với "
                + "<a href = ''>Điều khoản</a>" + " của chúng tôi");
        tv1.setText(text1);

        tv2 = findViewById(R.id.tv2);
        CharSequence text2 = Html.fromHtml("Learn how we process your data in our "
                + "<a href = ''>Privacy</a>");
        tv2.setText(text2);

        tv3 = findViewById(R.id.tv3);
        CharSequence text3 = Html.fromHtml(" " + "<a href = ''>Policy</a>" + " and " + "<a href = ''>Cookies Policy.</a>");
        tv3.setText(text3);
    }

    private void setupListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = edtUserName.getText().toString().trim();
                String Password = edtPassword.getText().toString().trim();

                if (validateInput(UserName, Password)) {
                    String UserName_fakeEmail = UserName + "@lucky.com";

                    mAuth.signInWithEmailAndPassword(UserName_fakeEmail, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mContext, "Log in successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        return;
                                    }
                                }
                            });
                }
            }
        });

        tvTroubleLogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Trouble Logging", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(mContext, RegisterActivity.class);
//            startActivity(i);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Need Help", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(mContext, RegisterActivity.class);
//            startActivity(i);
            }
        });
    }

    private boolean validateInput(String UserName, String Password) {
        if (UserName == null || UserName.isEmpty()) {
            Toast.makeText(this, "User name must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Password == null || Password.isEmpty()) {
            Toast.makeText(this, "Password must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}