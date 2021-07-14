package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private List<Map<String, Object>> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        LinearLayout linearLayout = findViewById(R.id.search_results_holder);

        FirebaseDatabase.getInstance().getReference("Places").get().addOnSuccessListener(dataSnapshot -> {
            Iterable<DataSnapshot> data = dataSnapshot.getChildren();

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            for (DataSnapshot snapshot : data) {
                Map<String, Object> placeMap = (Map<String, Object>) snapshot.getValue();

                Map<String, Object> place = new HashMap<>();

                View view = inflater.inflate(R.layout.most_viewed_card_design, null);

                place.put("name", snapshot.getKey().toLowerCase());
                place.put("category", placeMap.get("category").toString());
                place.put("rating", (placeMap.get("rating") != null) ? placeMap.get("rating").toString() : "0");
                place.put("description", placeMap.get("description").toString());
                place.put("view", view);

                places.add(place);

                ((TextView) view.findViewById(R.id.most_title)).setText(place.get("name").toString());
                ((TextView) view.findViewById(R.id.most_description)).setText(place.get("description").toString());

                LoadImageURL loadImageURL = new LoadImageURL((ImageView) view.findViewById(R.id.most_image));
                loadImageURL.execute(placeMap.get("imageUrl").toString());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 30);

                view.setLayoutParams(params);

                view.setClickable(true);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), PlaceData.class);
                    intent.putExtra("name",placeMap.get("name").toString());
                    intent.putExtra("image", placeMap.get("imageUrl").toString());
                    intent.putExtra("category", placeMap.get("category").toString());
                    intent.putExtra("EventorPlace",1);
                    intent.putExtra("latitude", placeMap.get("latitude").toString());
                    intent.putExtra("longitude", placeMap.get("longitude").toString());
                    startActivity(intent);
                });

                view.setVisibility(View.GONE);
                linearLayout.addView(view);
            }
        });

        ((EditText) findViewById(R.id.search_place_edit_text)).addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {

                String text = cs.toString().toLowerCase().trim();

                if (text.length() == 0) {
                    for (Map<String, Object> place : places) {
                        ((View) place.get("view")).setVisibility(View.GONE);
                    }
                    return;
                }

                for (Map<String, Object> place : places) {
                    if (place.get("name").toString().startsWith(text)) {
                        ((View) place.get("view")).setVisibility(View.VISIBLE);
                    }
                    else {
                        ((View) place.get("view")).setVisibility(View.GONE);
                    }
                }

            }
            public void afterTextChanged(Editable editable) { }
            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) { }
        });
    }

    private class LoadImageURL extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        LoadImageURL(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmp = null;
            try {
                URL url = new URL(urlDisplay);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                bmp = null;
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null)
                imageView.setImageBitmap(result);
        }
    }
}