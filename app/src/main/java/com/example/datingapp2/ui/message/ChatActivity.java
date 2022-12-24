package com.example.datingapp2.ui.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datingapp2.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    FirebaseAuth mAuth;
    String userID;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Users");
    DatabaseReference currentUserDB;

    ListView listOfMessages;
    EditText edtInput;
    FloatingActionButton btnSend;

    Query query;
    FirebaseListOptions<ChatMessage> options;
    FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedPref = getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        initFirebase();

        initView();

        setupListener();
        setupListView();

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
//                    query = FirebaseDatabase.getInstance().getReference()
//                            .child("Chats")
//                            .child(sharedPref.getString("UserName", "empty"))
//                            .child("tin"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void initView() {
        listOfMessages = (ListView) findViewById(R.id.listOfMessages);
        edtInput = findViewById(R.id.edtInput);
        btnSend = findViewById(R.id.btnSend);
    }

    private void setupListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtInput.getText().toString().trim() != null && !edtInput.getText().toString().trim().isEmpty()) {
                    Log.d("insidechat", "from " + sharedPref.getString("UserName", "empty") + "to " + sharedPref.getString("ChatWith", "empty") + " (me)");
                    FirebaseDatabase.getInstance().getReference()
                            .child("Chats")
                            .child(sharedPref.getString("UserName", "empty"))
                            .child(sharedPref.getString("ChatWith", "empty")) // who you chat with
                            .push()
                            .setValue(new ChatMessage(sharedPref.getString("UserName", "empty"), edtInput.getText().toString().trim(), getTime())
                            );

                    FirebaseDatabase.getInstance().getReference()
                            .child("Chats")
                            .child(sharedPref.getString("ChatWith", "empty"))
                            .child(sharedPref.getString("UserName", "empty"))
                            .push()
                            .setValue(new ChatMessage(sharedPref.getString("UserName", "empty"), edtInput.getText().toString().trim(), getTime())
                            );
                    edtInput.setText("");
                }
            }
        });
    }

    private void setupListView() {
        query = FirebaseDatabase.getInstance().getReference()
                .child("Chats")
                .child(sharedPref.getString("UserName", "empty"))
                .child(sharedPref.getString("ChatWith", "empty"));

        options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);

                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                // Set their text
                messageUser.setText(model.getMessageUser());
                messageText.setText(model.getMessageText());
                messageTime.setText(model.getMessageTime());

                scrollMyListViewToBottom();
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    private void scrollMyListViewToBottom() {
        listOfMessages.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listOfMessages.setSelection(listOfMessages.getCount() - 1);
            }
        });
    }

    public String getTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date currentLocalTime = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        String currentTime = dateFormat.format(currentLocalTime);
        return currentTime;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}