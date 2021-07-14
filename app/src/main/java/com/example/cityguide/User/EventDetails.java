package com.example.cityguide.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.Database.SessionManager;
import com.example.cityguide.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventDetails extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private boolean going = false;
    private String activities,description,name,date1,time1;
    private SessionManager sessionManager;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");

    public EventDetails() {
    }
    public EventDetails(String name) {
        this.name = name;
    }

    public static EventDetails newInstance(String param1, String param2) {
        EventDetails fragment = new EventDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sessionManager = new SessionManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        this.view = view;
        TextView catLabel = view.findViewById(R.id.event_cat_label);
        TextView descLabel = view.findViewById(R.id.event_desc_label);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);

        if (!sessionManager.checkLogin()) {
            view.findViewById(R.id.Going_box).setVisibility(View.GONE);
        }else{
            String userId = sessionManager.getUserDetail().get("name");
            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/eventList").get().addOnSuccessListener(dataSnapshot -> {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> wishListedPlace = (Map<String, Object>) data.getValue();
                    if (wishListedPlace.get("place").toString().equals(EventDetails.this.name)) {
                        going = true;
                        ((ImageView) view.findViewById(R.id.eventlist_image)).setImageResource(R.drawable.thumbup);
                    }
                }
            });
        }

        FirebaseDatabase.getInstance().getReference("Events/" + this.name).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                activities = data.get("activities").toString();
                description = data.get("desc").toString();
                date1 = data.get("date").toString();
                time1 = data.get("time").toString();

                catLabel.setText(activities);
                descLabel.setText(description);
                date.setText(date1);
                time.setText(time1);
            }
        });

        view.findViewById(R.id.eventlist_image).setOnClickListener(v -> {
            System.out.println(this.name);
            ref.child(this.name).child("going").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    Integer score = currentData.getValue(Integer.class);
                    System.out.println(score);
                    if (score == null) {
                        currentData.setValue(score + 1);
                        return Transaction.success(currentData);
                    }
                    if (score != null){
                        currentData.setValue(score + 1);
                    }
                    return Transaction.success(currentData);
                }
                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                }
            });
            addToEventList();
        });
        return view;
    }

    private void addToEventList() {
        if (sessionManager.checkLogin() && !going) {

            Map<String, String> userDetails = sessionManager.getUserDetail();
            String userId = userDetails.get("name");

            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/eventList").get().addOnSuccessListener(dataSnapshot -> {

                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                ArrayList<Map<String, Object>> wishList = new ArrayList<>();

                for (DataSnapshot wishListPlace : data) {
                    Map<String, Object> wishListPlaceMap = (Map<String, Object>) wishListPlace.getValue();
                    wishList.add(wishListPlaceMap);
                }

                Map<String, Object> newWishListedPlaceMap = new HashMap<>();
                newWishListedPlaceMap.put("place", this.name);

                wishList.add(newWishListedPlaceMap);

                Map<String, Object> wishListMap = new HashMap<>();
                wishListMap.put("eventList", wishList);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + userId);
                ref.updateChildren(wishListMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        going = true;
                        ((ImageView) view.findViewById(R.id.eventlist_image)).setImageResource(R.drawable.thumbup);
                    }
                });

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            });
        }
    }
}