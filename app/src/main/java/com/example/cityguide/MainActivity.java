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
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.cityguide.HomeAdaptors.ProfileHelperClass;
import com.example.cityguide.HomeAdaptors.WishList.WishListAdapter;
import com.example.cityguide.HomeAdaptors.WishList.WishListHelperClass;
import com.example.cityguide.User.Categories;
import com.example.cityguide.User.Profile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    RecyclerView featuredRecyclerView, mostViewedRecyclerView, categoriesRecyclerView, wishListRecyclerView;
    RecyclerView.Adapter adapter;
    MostViewedAdapter Madaptor;
    WishListAdapter wishListAdapter;
    FeaturedAdaptor Fadaptor;
    DatabaseReference placeadded;
    ImageView menuIcon;
    LinearLayout contentView;
    double latitude,longitude;
    LocationManager mLocationManager;


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
        wishListRecyclerView = findViewById(R.id.wishlist_recyclerView);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        placeadded = FirebaseDatabase.getInstance().getReference("Places");


        //Drawer Menu Hooks
        drawerlayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navigation_view);

        Internet object = new Internet();

        if(!object.Connected(this)){
            showCustomDialog();
        }

        checkAppPermission();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkAppPermission();

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
        wishListRecyclerView();

        placeadded.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PlaceHelperClass placeaddNot = snapshot.getValue(PlaceHelperClass.class);
                String userid = placeaddNot.getUser();
                SessionManager session = new SessionManager(MainActivity.this);
                if(userid != null && session.checkLogin()) {
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String count;
                    Remember2 session1 = new Remember2(MainActivity.this);
                    count = session1.getDetail();
                    if(count != null) {
                        if (user.equals(userid) && count.equals("0")) {
                            notification();
                            count = "1";
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void wishListRecyclerView() {

        wishListRecyclerView.setHasFixedSize(true);
        wishListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String current = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference wish;
        String name;
//        for(int i =0;i<5 ; i++){
//             wish = FirebaseDatabase.getInstance().getReference().child("Users");
//             wish.child(current).child("wishList").child(Integer.toString(i)).addValueEventListener(new ValueEventListener() {
//                 @Override
//                 public void onDataChange(@NonNull DataSnapshot snapshot) {
//                     ProfileHelperClass profile = snapshot.getValue(ProfileHelperClass.class);
//                     if(profile != null) {
//                         int j=0;
//                        name = profile.name;
//                     }
//                 }
//
//                 @Override
//                 public void onCancelled(@NonNull DatabaseError error) {
//
//                 }
//             });
//        }

        FirebaseRecyclerOptions<WishListHelperClass> options =
                new FirebaseRecyclerOptions.Builder<WishListHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places"),WishListHelperClass.class)
                        .build();
        wishListAdapter = new WishListAdapter(options);
        wishListRecyclerView.setAdapter(wishListAdapter);
    }

    private void mostViewedRecycler() {

        mostViewedRecyclerView.setHasFixedSize(true);
        mostViewedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<MostViewedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<MostViewedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places"),MostViewedHelperClass.class)
                        .build();

        Madaptor = new MostViewedAdapter(options);
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
        wishListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Madaptor.stopListening();
        Fadaptor.stopListening();
        wishListAdapter.stopListening();
    }


    public void AddPlacePage(View view){

        SessionManager session = new SessionManager(MainActivity.this);
        if(!session.checkLogin()){
            startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
        }else if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
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
}