package com.example.datingapp2.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.datingapp2.CardStackAdapter;
import com.example.datingapp2.ItemModel;
import com.example.datingapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private DatabaseReference currentUserDB;

    private static final String adminID = "WCXTevD8pBf5cJ5cPCZlIIUrc0q2";
    private DatabaseReference adminDB = root.child(adminID);
    List<DatabaseReference> UserInfos = new ArrayList<>();

    View viewRoot;
    private CardStackLayoutManager manager;
    List<ItemModel> data = new ArrayList<>();
    private CardStackAdapter adapter;

    boolean isEmpty = false;
    int count;
    int position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.getString("UserName", "empty").equals("empty")) {
            editor.remove("UserName");
        }

        initFirebase();

        //init(viewRoot);

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

        getAllUsersID();
    }

    private void getAllUsersID() {
        adminDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.child("users").getChildren()) {
                    String ID = user.getValue().toString();

                    DatabaseReference userInfo = FirebaseDatabase.getInstance()
                            .getReference().child("Users").child(ID);

                    UserInfos.add(userInfo);
                }
                getAllUsersData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllUsersData() {
        count = UserInfos.size();
        for (DatabaseReference userInfo : UserInfos) {
            userInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ItemModel item = new ItemModel();

                    String imageLink = "";
                    String nickname = "";
                    String username = "";
                    String age = "";
                    String thanhpho = "";
                    String khoangcach = "";

                    if (snapshot.child("profileImagesUrl").getValue() != null) {
                        imageLink = snapshot.child("profileImagesUrl").getValue().toString();
                        item.setImageLink(imageLink);
                    } else {
                        isEmpty = true;
                    }

                    if (snapshot.child("NickName").getValue() != null) {
                        nickname = snapshot.child("NickName").getValue().toString();
                        item.setNickName(nickname);
                    } else {
                        isEmpty = true;
                    }

                    if (snapshot.child("UserName").getValue() != null) {
                        username = snapshot.child("UserName").getValue().toString();
                        item.setUserName(username);
                    } else {
                        isEmpty = true;
                    }

                    if (snapshot.child("Age").getValue() != null) {
                        age = snapshot.child("Age").getValue().toString();
                        item.setAge(age);
                    } else {
                        isEmpty = true;
                    }

                    if (snapshot.child("Thanhpho").getValue() != null) {
                        thanhpho = snapshot.child("Thanhpho").getValue().toString();
                        item.setCity(thanhpho);
                    } else {
                        item.setCity("");
                    }

                    if (snapshot.child("Khoangcach").getValue() != null) {
                        khoangcach = snapshot.child("Khoangcach").getValue().toString();
                        item.setKhoangcach(khoangcach);
                    } else {
                        item.setKhoangcach("2 Km");
                    }

                    data.add(item);

                    if (count == 1) {
                        Log.d("DashboardFragment", "finish getAllUsersData() from firebase");
                        initView(viewRoot); // init View
                    }
                    count--;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initView(View root) {
        CardStackView cardStackView = root.findViewById(R.id.card_stack_view);

        setupCardStackView(cardStackView);
    }

    private void setupCardStackView(CardStackView cardStackView) {
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);

                if (direction == Direction.Right) {
                    int p = position;
                    String username = data.get(p).getUserName();
                    Log.d("position", p + " Name=" + username);
                    position++;
                    Toast.makeText(getContext(), "LIKE " + data.get(p).getNickName(), Toast.LENGTH_SHORT).show();

                    //
                    Map<String, Object> userInfo = new HashMap<>();

                    userInfo.put("profileImagesUrl", data.get(p).getImageLink());
                    userInfo.put("UserName", username);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Matches")
                            .child(sharedPref.getString("UserName", "empty"))
                            .child(username) // matches with
                            .updateChildren(userInfo);

                }
                if (direction == Direction.Top) {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left) {
                    int p = position;
                    String name = data.get(p).getNickName();
                    Log.d("position", p + " Name=" + name);
                    position++;
                    Toast.makeText(getContext(), "NOPE " + name, Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                // adapter.getItemCount() - adapter.getItemCount()
                // adapter.getItemCount() - 5
//                if (manager.getTopPosition() == adapter.getItemCount() - adapter.getItemCount()) {
//                    paginate();
//                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardCanceled: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardDisappeared: " + position + ", name: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);

        adapter = new CardStackAdapter(data, getActivity());
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        Collections.shuffle(data);
        Log.d("Data's size", data.size() + "");
    }

//    private void paginate() {
//        List<ItemModel> Old = adapter.getItems();
//        List<ItemModel> New = new ArrayList<>(data);
//        CardStackCallback callback = new CardStackCallback(Old, New);
//
//        DiffUtil.DiffResult diffUtil = DiffUtil.calculateDiff(callback);
//        adapter.setItems(New);
//        diffUtil.dispatchUpdatesTo(adapter);
//    }

//    private List<ItemModel> getData() {
//        List<ItemModel> items = new ArrayList<>();
//
////        items.add(new ItemModel(R.drawable.sample1, "Markonah", "24", "Jember", "10"));
////        items.add(new ItemModel(R.drawable.sample2, "Marpuah", "20", "Malang", "10"));
////        items.add(new ItemModel(R.drawable.sample3, "Sukijah", "27", "Jonggol", "10"));
////        items.add(new ItemModel(R.drawable.sample4, "Markobar", "19", "Bandung", "10"));
////        items.add(new ItemModel(R.drawable.sample5, "Marmut", "25", "Hutan", "10"));
//
//        Log.d("DashboardFragment", "finish getData() from local");
//
//        return items;
//    }
}