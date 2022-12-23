package com.example.datingapp2.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datingapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private DatabaseReference currentUserDB;

    private EditText edt_Gioithieubanthan, edt_Sothich, edt_Cunghoangdao, edt_Giaoduc, edt_Kieutinhcach, edt_Giaotiep, edt_Giadinh,
            edt_Thucung, edt_Tapluyen, edt_Doan, edt_Ngu, edt_Ttxh, edt_Chucdanh, edt_Congty, edt_Truong, edt_Thanhpho, edt_Music, edt_Gioitinh,
            edt_Khuynhhuong;

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initFirebase();

        initView();

        setupListener();
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
                    if (dataSnapshot.child("Gioithieubanthan").getValue() != null) {
                        String s = dataSnapshot.child("Gioithieubanthan").getValue().toString();
                        edt_Gioithieubanthan.setText(s);
                    } else {
                        edt_Gioithieubanthan.setText("");
                    }

                    if (dataSnapshot.child("Sothich").getValue() != null) {
                        String s = dataSnapshot.child("Sothich").getValue().toString();
                        edt_Sothich.setText(s);
                    } else {
                        edt_Sothich.setText("");
                    }

                    if (dataSnapshot.child("Cunghoangdao").getValue() != null) {
                        String s = dataSnapshot.child("Cunghoangdao").getValue().toString();
                        edt_Cunghoangdao.setText(s);
                    } else {
                        edt_Cunghoangdao.setText("");
                    }

                    if (dataSnapshot.child("Giaoduc").getValue() != null) {
                        String s = dataSnapshot.child("Giaoduc").getValue().toString();
                        edt_Giaoduc.setText(s);
                    } else {
                        edt_Giaoduc.setText("");
                    }

                    if (dataSnapshot.child("Kieutinhcach").getValue() != null) {
                        String s = dataSnapshot.child("Kieutinhcach").getValue().toString();
                        edt_Kieutinhcach.setText(s);
                    } else {
                        edt_Kieutinhcach.setText("");
                    }

                    if (dataSnapshot.child("Giaotiep").getValue() != null) {
                        String s = dataSnapshot.child("Giaotiep").getValue().toString();
                        edt_Giaotiep.setText(s);
                    } else {
                        edt_Giaotiep.setText("");
                    }

                    if (dataSnapshot.child("Giadinh").getValue() != null) {
                        String s = dataSnapshot.child("Giadinh").getValue().toString();
                        edt_Giadinh.setText(s);
                    } else {
                        edt_Giadinh.setText("");
                    }

                    if (dataSnapshot.child("Thucung").getValue() != null) {
                        String s = dataSnapshot.child("Thucung").getValue().toString();
                        edt_Thucung.setText(s);
                    } else {
                        edt_Thucung.setText("");
                    }

                    if (dataSnapshot.child("Tapluyen").getValue() != null) {
                        String s = dataSnapshot.child("Tapluyen").getValue().toString();
                        edt_Tapluyen.setText(s);
                    } else {
                        edt_Tapluyen.setText("");
                    }

                    if (dataSnapshot.child("Doan").getValue() != null) {
                        String s = dataSnapshot.child("Doan").getValue().toString();
                        edt_Doan.setText(s);
                    } else {
                        edt_Doan.setText("");
                    }

                    if (dataSnapshot.child("Ngu").getValue() != null) {
                        String s = dataSnapshot.child("Ngu").getValue().toString();
                        edt_Ngu.setText(s);
                    } else {
                        edt_Ngu.setText("");
                    }

                    if (dataSnapshot.child("Ttxh").getValue() != null) {
                        String s = dataSnapshot.child("Ttxh").getValue().toString();
                        edt_Ttxh.setText(s);
                    } else {
                        edt_Ttxh.setText("");
                    }

                    if (dataSnapshot.child("Chucdanh").getValue() != null) {
                        String s = dataSnapshot.child("Chucdanh").getValue().toString();
                        edt_Chucdanh.setText(s);
                    } else {
                        edt_Chucdanh.setText("");
                    }

                    if (dataSnapshot.child("Congty").getValue() != null) {
                        String s = dataSnapshot.child("Congty").getValue().toString();
                        edt_Congty.setText(s);
                    } else {
                        edt_Congty.setText("");
                    }

                    if (dataSnapshot.child("Truong").getValue() != null) {
                        String s = dataSnapshot.child("Truong").getValue().toString();
                        edt_Truong.setText(s);
                    } else {
                        edt_Truong.setText("");
                    }

                    if (dataSnapshot.child("Thanhpho").getValue() != null) {
                        String s = dataSnapshot.child("Thanhpho").getValue().toString();
                        edt_Thanhpho.setText(s);
                    } else {
                        edt_Thanhpho.setText("");
                    }

                    if (dataSnapshot.child("Music").getValue() != null) {
                        String s = dataSnapshot.child("Music").getValue().toString();
                        edt_Music.setText(s);
                    } else {
                        edt_Music.setText("");
                    }

                    if (dataSnapshot.child("Gioitinh").getValue() != null) {
                        String s = dataSnapshot.child("Gioitinh").getValue().toString();
                        edt_Gioitinh.setText(s);
                    } else {
                        edt_Gioitinh.setText("");
                    }

                    if (dataSnapshot.child("Khuynhhuong").getValue() != null) {
                        String s = dataSnapshot.child("Khuynhhuong").getValue().toString();
                        edt_Khuynhhuong.setText(s);
                    } else {
                        edt_Khuynhhuong.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        edt_Gioithieubanthan = findViewById(R.id.edt_Gioithieubanthan);
        edt_Sothich = findViewById(R.id.edt_Sothich);
        edt_Cunghoangdao = findViewById(R.id.edt_Cunghoangdao);
        edt_Giaoduc = findViewById(R.id.edt_Giaoduc);
        edt_Kieutinhcach = findViewById(R.id.edt_Kieutinhcach);
        edt_Giaotiep = findViewById(R.id.edt_Giaotiep);
        edt_Giadinh = findViewById(R.id.edt_Giadinh);
        edt_Thucung = findViewById(R.id.edt_Thucung);
        edt_Tapluyen = findViewById(R.id.edt_Tapluyen);
        edt_Doan = findViewById(R.id.edt_Doan);
        edt_Ngu = findViewById(R.id.edt_Ngu);
        edt_Ttxh = findViewById(R.id.edt_Ttxh);
        edt_Chucdanh = findViewById(R.id.edt_Chucdanh);
        edt_Congty = findViewById(R.id.edt_Congty);
        edt_Truong = findViewById(R.id.edt_Truong);
        edt_Thanhpho = findViewById(R.id.edt_Thanhpho);
        edt_Music = findViewById(R.id.edt_Music);
        edt_Gioitinh = findViewById(R.id.edt_Gioitinh);
        edt_Khuynhhuong = findViewById(R.id.edt_Khuynhhuong);

        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDataToFirebase();
                finish();
            }
        });
    }

    private void uploadDataToFirebase() {
        currentUserDB.child("Gioithieubanthan").setValue(edt_Gioithieubanthan.getText().toString().trim());
        currentUserDB.child("Sothich").setValue(edt_Sothich.getText().toString().trim());
        currentUserDB.child("Cunghoangdao").setValue(edt_Cunghoangdao.getText().toString().trim());
        currentUserDB.child("Giaoduc").setValue(edt_Giaoduc.getText().toString().trim());
        currentUserDB.child("Kieutinhcach").setValue(edt_Kieutinhcach.getText().toString().trim());
        currentUserDB.child("Giaotiep").setValue(edt_Giaotiep.getText().toString().trim());
        currentUserDB.child("Giadinh").setValue(edt_Giadinh.getText().toString().trim());
        currentUserDB.child("Thucung").setValue(edt_Thucung.getText().toString().trim());
        currentUserDB.child("Tapluyen").setValue(edt_Tapluyen.getText().toString().trim());
        currentUserDB.child("Doan").setValue(edt_Doan.getText().toString().trim());
        currentUserDB.child("Ngu").setValue(edt_Ngu.getText().toString().trim());
        currentUserDB.child("Ttxh").setValue(edt_Ttxh.getText().toString().trim());
        currentUserDB.child("Chucdanh").setValue(edt_Chucdanh.getText().toString().trim());
        currentUserDB.child("Congty").setValue(edt_Congty.getText().toString().trim());
        currentUserDB.child("Truong").setValue(edt_Truong.getText().toString().trim());
        currentUserDB.child("Thanhpho").setValue(edt_Thanhpho.getText().toString().trim());
        currentUserDB.child("Music").setValue(edt_Music.getText().toString().trim());
        currentUserDB.child("Gioitinh").setValue(edt_Gioitinh.getText().toString().trim());
        currentUserDB.child("Khuynhhuong").setValue(edt_Khuynhhuong.getText().toString().trim());

        //
    }
}