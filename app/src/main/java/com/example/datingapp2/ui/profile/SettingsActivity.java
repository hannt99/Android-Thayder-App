package com.example.datingapp2.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datingapp2.R;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference currentUserDB;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    private EditText edt_Sodienthoai, edt_Toancau;
    private SwitchMaterial switch_Toancau;
    private TextView tv_khoangcachkm, tv_dotuoi;
    private SeekBar sb_khoangcach;
    private RangeSlider sb_dotuoi;
    private Button btnSave;

    private String tuoidau = "18";
    private String tuoicuoi = "70";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                    if (dataSnapshot.child("PhoneNumber").getValue() != null) {
                        String sdt = dataSnapshot.child("PhoneNumber").getValue().toString();
                        edt_Sodienthoai.setText(sdt);
                    }
                    if (dataSnapshot.child("Khoangcach").getValue() != null) {
                        String s = dataSnapshot.child("Khoangcach").getValue().toString();
                        tv_khoangcachkm.setText(s);
                    } else {
                        tv_khoangcachkm.setText("2 Km");
                    }

                    if (dataSnapshot.child("Dotuoi").getValue() != null) {
                        tuoidau = dataSnapshot.child("Dotuoi").child("Tuoidau").getValue().toString();
                        tuoicuoi = dataSnapshot.child("Dotuoi").child("Tuoicuoi").getValue().toString();
                        tv_dotuoi.setText(tuoidau + " - " + tuoicuoi + " Tuổi");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        edt_Sodienthoai = findViewById(R.id.edt_Sodienthoai);
        edt_Toancau = findViewById(R.id.edt_Toancau);

        switch_Toancau = findViewById(R.id.switch_Toancau);

        tv_khoangcachkm = findViewById(R.id.tv_khoangcachkm);

        sb_khoangcach = findViewById(R.id.sb_khoangcach);

        tv_dotuoi = findViewById(R.id.tv_dotuoi);

        sb_dotuoi = findViewById(R.id.sb_dotuoi);

        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListener() {
        switch_Toancau.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(SettingsActivity.this, "switch changed", Toast.LENGTH_SHORT).show();
            }
        });

        sb_khoangcach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_khoangcachkm.setText(String.valueOf(i) + " Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_dotuoi.setStepSize(1.0f);
        sb_dotuoi.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List<Float> values = slider.getValues();
                tuoidau = String.valueOf(Math.round(values.get(0)));
                tuoicuoi = String.valueOf(Math.round(values.get(1)));
                tv_dotuoi.setText(tuoidau + " - " + tuoicuoi + " Tuổi");
                return;
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List<Float> values = slider.getValues();
                tuoidau = String.valueOf(Math.round(values.get(0)));
                tuoicuoi = String.valueOf(Math.round(values.get(1)));
                tv_dotuoi.setText(tuoidau + " - " + tuoicuoi + " Tuổi");
                return;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDataToFirebase();
                finish();
            }
        });
    }

    private void uploadDataToFirebase() {
        currentUserDB.child("Khoangcach").setValue(tv_khoangcachkm.getText().toString().trim());
        currentUserDB.child("Dotuoi").child("Tuoidau").setValue(tuoidau);
        currentUserDB.child("Dotuoi").child("Tuoicuoi").setValue(tuoicuoi);

        //
    }
}