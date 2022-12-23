package com.example.datingapp2.ui.matches;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.datingapp2.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesFragment extends Fragment {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    FirebaseAuth mAuth;
    String userID;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Users");
    DatabaseReference currentUserDB;
    List<MatchModel> data = new ArrayList<>();

    View viewRoot;

    //private MatchesViewModel matchesViewModel;
    GridView listOfMatches;

    Query query;
    FirebaseListOptions<MatchModel> options;
    FirebaseListAdapter<MatchModel> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_matches, container, false);

        sharedPref = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.getString("UserName", "empty").equals("empty")) {
            editor.remove("UserName");
        }

        initFirebase();

        initView();

        setupListView();

        //matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);

        //recyclerView = root.findViewById(R.id.recyclerView);
        //items = initData();
        //adapter = new ItemAdapter(items, this);
        //recyclerView.setAdapter(adapter);

       /* final TextView tvMatches = root.findViewById(R.id.tvMatches);

        matchesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvMatches.setText(s);
            }
        });*/
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

//                    query = FirebaseDatabase.getInstance().getReference()
//                            .child("Chats")
//                            .child(sharedPref.getString("UserName", "empty"))
//                            .child("tin"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        listOfMatches = viewRoot.findViewById(R.id.listOfMatches);
    }

    private void setupListView() {
        query = FirebaseDatabase.getInstance().getReference()
                .child("Matches")
                .child(sharedPref.getString("UserName", "empty"));
        //.child("Ngoc Hai"); // who you like

        options = new FirebaseListOptions.Builder<MatchModel>()
                .setQuery(query, MatchModel.class)
                .setLayout(R.layout.match_item)
                .build();


        adapter = new FirebaseListAdapter<MatchModel>(options) {
            @Override
            protected void populateView(View v, MatchModel model, int position) {
                // Get references to the views of message.xml
                ImageView ivAvatar = v.findViewById(R.id.ivAvatar);
                ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "" + model.getUserName(), Toast.LENGTH_SHORT).show();
                    }
                });

                TextView tvName = v.findViewById(R.id.tvName);

                // Set their data
                Glide.with(getContext()).load(model.getProfileImagesUrl()).into(ivAvatar);
                tvName.setText(model.getUserName());
            }
        };

        listOfMatches.setAdapter(adapter);
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