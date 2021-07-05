package com.example.cityguide;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cityguide.Database.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Information extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private float rating;
    private TextView ratingLabel;
    private View view;
    private boolean rated = false, wishListed = false;
    private String category,description,name;
    private SessionManager sessionManager;

    public Information() {
    }

    public Information(String name) {
        this.name = name;
    }

    public static Information newInstance(String param1, String param2) {
        Information fragment = new Information();
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
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        this.view = view;

        ratingLabel = ((TextView)view.findViewById(R.id.rating_label));
        TextView catLabel = view.findViewById(R.id.place_cat_label);
        TextView descLabel = view.findViewById(R.id.place_desc_label);

        if (!sessionManager.checkLogin()) {
            view.findViewById(R.id.comment_box_view).setVisibility(View.GONE);
            view.findViewById(R.id.rate_box).setVisibility(View.GONE);
            view.findViewById(R.id.wishlist_box).setVisibility(View.GONE);
        }
        else {
            String userId = sessionManager.getUserDetail().get("name");
            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/ratedPlaces").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Map<String, Object> ratedPlace = (Map<String, Object>) data.getValue();
                        if (ratedPlace.get("place").toString().equals(Information.this.name)) {
                            rated = true;
                            long rating = (long)ratedPlace.get("rating");

                            ratingLabel.setText(Float.toString(rating));
                            if (rating > 0) {
                                ((ImageView)view.findViewById(R.id.star_image_1)).setImageResource(R.drawable.star_active);
                            }
                            if (rating > 1) {
                                ((ImageView)view.findViewById(R.id.star_image_2)).setImageResource(R.drawable.star_active);
                            }
                            if (rating > 2) {
                                ((ImageView)view.findViewById(R.id.star_image_3)).setImageResource(R.drawable.star_active);
                            }
                            if (rating > 3) {
                                ((ImageView)view.findViewById(R.id.star_image_4)).setImageResource(R.drawable.star_active);
                            }
                            if (rating > 4) {
                                ((ImageView)view.findViewById(R.id.star_image_5)).setImageResource(R.drawable.star_active);
                            }

                            break;
                        }
                    }
                }
            });

            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/wishList").get().addOnSuccessListener(dataSnapshot -> {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> wishListedPlace = (Map<String, Object>) data.getValue();
                    if (wishListedPlace.get("place").toString().equals(Information.this.name)) {
                        wishListed = true;
                        ((ImageView) view.findViewById(R.id.wishlist_image)).setImageResource(R.drawable.wishlist_active);
                    }
                }
            });
        }

        LinearLayout commentsHolder = (LinearLayout) view.findViewById(R.id.comments_holder);

        // load comments
        FirebaseDatabase.getInstance().getReference("Places/" + this.name + "/comments").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                for (DataSnapshot snapshot : data) {

                    Map<String, Object> comment = (Map<String, Object>) snapshot.getValue();

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = 20;

                    LinearLayout commentSection = new LinearLayout(getContext());
                    commentSection.setLayoutParams(params);
                    commentSection.setOrientation(LinearLayout.VERTICAL);

                    TextView usernameText = new TextView(getContext());
                    usernameText.setTextSize(14);

                    TextView commentText = new TextView(getContext());
                    commentText.setTextSize(16);
                    commentText.setText((comment.get("text") != null) ? comment.get("text").toString() : "");
                    commentText.setTextColor(Color.parseColor("#000000"));

                    String time = comment.get("time").toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:m");
                    String dateString = formatter.format(new Date(Long.parseLong(time)));

                    usernameText.setText((comment.get("username") != null) ? comment.get("username").toString() + "  " + dateString : "  " + dateString);

                    commentSection.addView(usernameText);
                    commentSection.addView(commentText);

                    commentsHolder.addView(commentSection);
                }
            }
        });

        // load categories and description
        FirebaseDatabase.getInstance().getReference("Places/" + this.name).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                category = data.get("category").toString();
                description = data.get("description").toString();

                catLabel.setText(category);
                descLabel.setText(description);

                if (data.containsKey("rating")) {
                    rating = Float.parseFloat(data.get("rating").toString());
                }
                else {
                    rating = 0;
                }

                ratingLabel.setText(Float.toString(rating));
            }
        });

        ImageView star1 = view.findViewById(R.id.star_image_1);
        ImageView star2 = view.findViewById(R.id.star_image_2);
        ImageView star3 = view.findViewById(R.id.star_image_3);
        ImageView star4 = view.findViewById(R.id.star_image_4);
        ImageView star5 = view.findViewById(R.id.star_image_5);

        star1.setOnClickListener(v -> {
            setRating(1);
        });

        star2.setOnClickListener(v -> {
            setRating(2);
        });

        star3.setOnClickListener(v -> {
            setRating(3);
        });

        star4.setOnClickListener(v -> {
            setRating(4);
        });

        star5.setOnClickListener(v -> {
            setRating(5);
        });

        EditText commentTextBox = view.findViewById(R.id.comment_text_box);

        view.findViewById(R.id.send_comment_btn).setOnClickListener(v -> {
            String text = commentTextBox.getText().toString().trim();

            if (text.length() == 0) {
                return;
            }

            if (sessionManager.checkLogin()) {

                Map<String, String> userDetails = sessionManager.getUserDetail();

                String userId = userDetails.get("name");
                String userName = userDetails.get(SessionManager.KEY_USERNAME);

                Map<String, Object> comment = new HashMap<>();

                Map<String, Object> commentDetails = new HashMap<>();
                commentDetails.put("text", text);
                commentDetails.put("time", ServerValue.TIMESTAMP);
                commentDetails.put("user", userId);
                commentDetails.put("username", userName);

                comment.put(UUID.randomUUID().toString(), commentDetails);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Places/" + this.name + "/comments");
                ref.updateChildren(comment).addOnSuccessListener(aVoid -> {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = 20;

                    LinearLayout commentSection = new LinearLayout(getContext());
                    commentSection.setLayoutParams(params);
                    commentSection.setOrientation(LinearLayout.VERTICAL);

                    TextView usernameText = new TextView(getContext());
                    usernameText.setTextSize(14);
                    usernameText.setText(userName);

                    TextView commentText = new TextView(getContext());
                    commentText.setTextSize(16);
                    commentText.setText(text);
                    commentText.setTextColor(Color.parseColor("#000000"));

                    commentSection.addView(usernameText);
                    commentSection.addView(commentText);

                    commentsHolder.addView(commentSection);

                    commentTextBox.setText("");

                }).addOnFailureListener(e -> {
                    Log.d("Saad", "saad");
                });
            }

        });

        view.findViewById(R.id.wishlist_image).setOnClickListener(v -> {
            addToWishList();
        });

        return view;
    }
    private void addToWishList() {
        if (sessionManager.checkLogin() && !wishListed) {

            Map<String, String> userDetails = sessionManager.getUserDetail();
            String userId = userDetails.get("name");

            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/wishList").get().addOnSuccessListener(dataSnapshot -> {

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
                wishListMap.put("wishList", wishList);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + userId);
                ref.updateChildren(wishListMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        wishListed = true;
                        ((ImageView) view.findViewById(R.id.wishlist_image)).setImageResource(R.drawable.wishlist_active);
                    }
                });

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setRating(float stars) {
        if (sessionManager.checkLogin() && !rated) {

            Map<String, String> userDetails = sessionManager.getUserDetail();
            String userId = userDetails.get("name");

            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/ratedPlaces").get().addOnSuccessListener(dataSnapshot -> {

                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                ArrayList<Map<String, Object>> ratedPlacesList = new ArrayList<>();

                for (DataSnapshot ratedPlace : data) {
                    Map<String, Object> ratedPlaceMap = (Map<String, Object>) ratedPlace.getValue();
                    ratedPlacesList.add(ratedPlaceMap);
                }

                Map<String, Object> newRatedPlaceMap = new HashMap<>();
                newRatedPlaceMap.put("rating", stars);
                newRatedPlaceMap.put("place", this.name);

                ratedPlacesList.add(newRatedPlaceMap);

                Map<String, Object> ratedPlacesListMap = new HashMap<>();
                ratedPlacesListMap.put("ratedPlaces", ratedPlacesList);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + userId);
                ref.updateChildren(ratedPlacesListMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (rating > 0)
                            rating = (rating + stars) / 2;
                        else
                            rating = stars;

                        rated = true;
                        ratingLabel.setText(Float.toString(rating));
                        if (stars > 0) {
                            ((ImageView)view.findViewById(R.id.star_image_1)).setImageResource(R.drawable.star_active);
                        }
                        if (stars > 1) {
                            ((ImageView)view.findViewById(R.id.star_image_2)).setImageResource(R.drawable.star_active);
                        }
                        if (stars > 2) {
                            ((ImageView)view.findViewById(R.id.star_image_3)).setImageResource(R.drawable.star_active);
                        }
                        if (stars > 3) {
                            ((ImageView)view.findViewById(R.id.star_image_4)).setImageResource(R.drawable.star_active);
                        }
                        if (stars > 4) {
                            ((ImageView)view.findViewById(R.id.star_image_5)).setImageResource(R.drawable.star_active);
                        }

                        Map<String, Object> newRatingMap = new HashMap<>();
                        newRatingMap.put("rating", rating);

                        FirebaseDatabase.getInstance().getReference("Places/" + name).updateChildren(newRatingMap).addOnSuccessListener(aVoid1 -> {
                            Toast.makeText(getContext(), "Rated Successfully.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            });

        }
    }
}