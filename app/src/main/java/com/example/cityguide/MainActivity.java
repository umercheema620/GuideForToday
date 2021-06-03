package com.example.cityguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.Common.CategoriesFunction;
import com.example.cityguide.Common.Internet;
import com.example.cityguide.Common.LoginSignup.Login;
import com.example.cityguide.Common.LoginSignup.WelcomeScreen;
import com.example.cityguide.Database.PlaceHelperClass;
import com.example.cityguide.Database.Remember2;
import com.example.cityguide.Database.SessionManager;
import com.example.cityguide.HomeAdaptors.Categories.CategoriesAdaptor;
import com.example.cityguide.HomeAdaptors.Categories.CategoriesHelperClass;
import com.example.cityguide.HomeAdaptors.Featured.FeaturedAdaptor;
import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.HomeAdaptors.MostViewed.MostViewedAdapter;
import com.example.cityguide.HomeAdaptors.MostViewed.MostViewedHelperClass;
import com.example.cityguide.HomeAdaptors.NewSpots.NewSpotsAdaptor;
import com.example.cityguide.HomeAdaptors.NewSpots.NewSpotsHelperClass;
import com.example.cityguide.HomeAdaptors.ProfileHelperClass;
import com.example.cityguide.HomeAdaptors.WishList.WishListAdapter;
import com.example.cityguide.HomeAdaptors.WishList.WishListHelperClass;
import com.example.cityguide.User.Categories;
import com.example.cityguide.User.ChatBot;
import com.example.cityguide.User.Profile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    RecyclerView featuredRecyclerView, mostViewedRecyclerView, categoriesRecyclerView;
    RecyclerView.Adapter adapter;
    MostViewedAdapter Madaptor;
    FeaturedAdaptor Fadaptor;
    DatabaseReference placeadded;
    ImageView menuIcon;
    LinearLayout contentView;
    double latitude,longitude;
    LocationManager mLocationManager;
    private LinearLayout wishListHolder;
    String count;


    public boolean isPermissionGranted;

    static final float END_SCALE = 0.7f;

    //Drawer
    DrawerLayout drawerlayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Hooks
        featuredRecyclerView = findViewById(R.id.featured_recycler);
        mostViewedRecyclerView = findViewById(R.id.most_viewed_recycler);
        categoriesRecyclerView = findViewById(R.id.categories_recyclerView);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        placeadded = FirebaseDatabase.getInstance().getReference("Places");
        wishListHolder = findViewById(R.id.wish_list_holder);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.checkLogin()) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            // Getting the wish list
            String userId = sessionManager.getUserDetail().get("name");
            FirebaseDatabase.getInstance().getReference("Users/" + userId + "/wishList").get().addOnSuccessListener(dataSnapshot -> {

                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                for (DataSnapshot wishListPlace : data) {
                    Map<String, Object> wishListPlaceMap = (Map<String, Object>) wishListPlace.getValue();

                    String placeName = wishListPlaceMap.get("place").toString();

                    // load categories and description
                    FirebaseDatabase.getInstance().getReference("Places/" + placeName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Map<String, Object> place = (Map<String, Object>) dataSnapshot.getValue();

                            if (place == null) {
                                return;
                            }

                            View view = inflater.inflate(R.layout.most_viewed_card_design, null);

                            ((TextView) view.findViewById(R.id.most_title)).setText(place.get("name").toString());
                            ((TextView) view.findViewById(R.id.most_description)).setText(place.get("description").toString());
                            System.out.println(place.get("rating"));

                            MainActivity.LoadImageURL loadImageURL = new MainActivity.LoadImageURL((ImageView) view.findViewById(R.id.most_image));
                            loadImageURL.execute(place.get("imageUrl").toString());

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 0, 0, 30);

                            view.setLayoutParams(params);

                            view.setClickable(true);
                            view.setOnClickListener(v -> {
                                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new PlaceFragment(place.get("name").toString(), place.get("imageUrl").toString())).addToBackStack(null).commit();
                            });

                            wishListHolder.addView(view);
                        }
                    });
                }

            }).addOnFailureListener(e -> {
                Toast.makeText(getApplication(), "failure", Toast.LENGTH_SHORT).show();
            });
        }
        else {
            findViewById(R.id.wish_list_area).setVisibility(View.GONE);
        }
        //Drawer Menu Hooks
        drawerlayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navigation_view);

        Internet object = new Internet();

        if(!object.Connected(this)){
            showCustomDialog();
        }

        checkAppPermission();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria cri = new Criteria();
        String provider = mLocationManager.getBestProvider(cri, false);
        if (provider != null && !provider.equals("")) {
            Location location1 = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(provider,2000,1,this);
            if(location1 != null){
                onLocationChanged(location1);
            }
            else {
                Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
            }
        }else{
        }

        findViewById(R.id.search_bar_layout).setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        navigationDrawer();

        //Recycler View Functions
        mostViewedRecycler();
        featuredRecycler();
        CategoriesRecycler();

//        placeadded.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                PlaceHelperClass placeaddNot = snapshot.getValue(PlaceHelperClass.class);
//                String userid = placeaddNot.getUser();
//                SessionManager session = new SessionManager(MainActivity.this);
//                if(userid != null && session.checkLogin()) {
//                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                    Remember2 session1 = new Remember2(MainActivity.this);
//                    count = session1.getDetail();
//                    if(count != null) {
//                        if (user.equals(userid) && count.equals("0")) {
//                            notification();
//                            count = "1";
//                            session1.createRememberSession(count);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void notification() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentTitle("Place Approved")
                .setSmallIcon(R.drawable.email)
                .setAutoCancel(true)
                .setContentText("Your Place has been approved for our App");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }

    public void showCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please connect to the internet")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                });
        builder.show();
    }

    private void checkAppPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener(){

            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),"");
                i.setData(uri);
                startActivity(i);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void navigationDrawer() {
        //Navigation
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerlayout.isDrawerVisible(GravityCompat.START))
                    drawerlayout.closeDrawer(GravityCompat.START);
                else drawerlayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerlayout.isDrawerVisible(GravityCompat.START))
            drawerlayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void animateNavigationDrawer() {

        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    private void CategoriesRecycler() {

        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CategoriesHelperClass> categoriesList = new ArrayList<>();
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffbdecfc, 0xff10c0fc});
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7e0ff, 0xffdc82fa});

        categoriesList.add(new CategoriesHelperClass(R.drawable.hospital_category, gradient2, "Hospitals","hospital"));
        categoriesList.add(new CategoriesHelperClass(R.drawable.restaurant_category, gradient1, "Restaurant", "Restaurant"));

        adapter = new CategoriesAdaptor(categoriesList,latitude,longitude,this);
        categoriesRecyclerView.setAdapter(adapter);
    }

    private void mostViewedRecycler() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);

        mostViewedRecyclerView.setHasFixedSize(true);
        mostViewedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<MostViewedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<MostViewedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places"),MostViewedHelperClass.class)
                        .build();

        Madaptor = new MostViewedAdapter(options,year,month,date);
        mostViewedRecyclerView.setAdapter(Madaptor);
    }

    private void featuredRecycler() {

        featuredRecyclerView.setHasFixedSize(true);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FirebaseRecyclerOptions<FeaturedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Places"),FeaturedHelperClass.class)
                .build();
        Fadaptor = new FeaturedAdaptor(options);
        featuredRecyclerView.setAdapter(Fadaptor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Madaptor.startListening();
        Fadaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Madaptor.stopListening();
        Fadaptor.stopListening();
    }


    public void AddPlacePage(View view){

        SessionManager session = new SessionManager(MainActivity.this);
        if(!session.checkLogin()){
            Intent intent1 = new Intent(getApplicationContext(),WelcomeScreen.class);
            intent1.putExtra("latitude",latitude);
            intent1.putExtra("longitude",longitude);
            startActivity(intent1);
        }else if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified() && session.checkLogin()){
            Toast.makeText(this, "Your email is not verified", Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(new Intent(getApplicationContext(), AddPlace.class));
        }
    }
    public void GoCategories(View view){
        startActivity(new Intent(getApplicationContext(), Categories.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_categories:
                Intent intent1 = new Intent(getApplicationContext(),Categories.class);
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("longitude",longitude);
                startActivity(intent1);
                break;
            case R.id.nav_bot:
                startActivity(new Intent(getApplicationContext(), ChatBot.class));
                break;
            case R.id.nav_search:
                startActivity(new Intent(getApplicationContext(),Search.class));
                break;
            case R.id.nav_logout:
                SessionManager session = new SessionManager(MainActivity.this);
                if(session.checkLogin()) {
                    session.LogOut();
                    Toast.makeText(this, "You have logged out", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_missing:
                session = new SessionManager(MainActivity.this);
                if(!session.checkLogin()){
                    startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
                }else if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    Toast.makeText(this, "Your email is not verified", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), AddPlace.class));
                }
                break;
            case R.id.nav_login:
                session = new SessionManager(MainActivity.this);
                if(!session.checkLogin()){
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.putExtra("comingfrom","main");
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Already Logged in", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent2);
                break;
        }

        return true;
    }

    public void GetRestaurantList(View view){
        CategoriesFunction restaurant = new CategoriesFunction(this,latitude,longitude);
        restaurant.Restaurant();
    }

    public void GetMonumentList(View view){
        CategoriesFunction monument = new CategoriesFunction(this,latitude,longitude);
        monument.Monuments();
    }

    public void GetMosqueList(View view){
        CategoriesFunction mosque = new CategoriesFunction(this,latitude,longitude);
        mosque.Mosque();
    }

    public void GetGamingList(View view){
        CategoriesFunction gaming = new CategoriesFunction(this,latitude,longitude);
        gaming.Gaming();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void GoToFeatured(View view) {
        Intent intent = new Intent(getApplicationContext(),Featured_list.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
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