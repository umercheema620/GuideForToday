package com.example.cityguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.Common.MapShow;
import com.example.cityguide.Database.PlaceHelperClass;
import com.example.cityguide.Database.Remember2;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddPlace extends AppCompatActivity implements LocationListener {

    ImageView main;
    AutoCompleteTextView categories;
    Button AddPlaceBtn;
    TextInputLayout PlaceName, PlaceCategory, PlaceDescription;
    TextInputEditText editplaceN, editplaceD;
    AutoCompleteTextView editplaceC;
    String location, placename, placecategory, placedescription, placename1, placecategory1, placedescription1, lat1="31.468578", lang1="74.8475783", userid;
    ImageView image;
    Uri imageuri;
    boolean featured;
    StorageReference storageReference;
    ProgressBar progressBar;
    private DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Review");
    private DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Users");
    public double latitude, longitude;
    boolean isPermissionGranted;
    LocationManager mLocationManager;
    int year,month,date,approve=0,disapprove=0,voted=0;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersdRef = rootRef.child("Users");
    ArrayList<Double> array = new ArrayList<>();
    ArrayList<Double> array2 = new ArrayList<>();
    ArrayList<String> array3 = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        main = findViewById(R.id.cancelToMain);
        AddPlaceBtn = findViewById(R.id.AddPlaceDatabase);
        progressBar = findViewById(R.id.progressBaradd);
        categories = findViewById(R.id.add_category);
        PlaceName = findViewById(R.id.place_name);
        PlaceCategory = findViewById(R.id.place_category);
        PlaceDescription = findViewById(R.id.place_desc);
        location = getIntent().getStringExtra("locationP");
        lat1 = getIntent().getStringExtra("latitude");
        lang1 = getIntent().getStringExtra("longitude");
        featured = false;
        editplaceN = findViewById(R.id.editplacename);
        editplaceD = findViewById(R.id.editplacedesc);
        editplaceC = findViewById(R.id.add_category);
        image = findViewById(R.id.imagegallery);
        storageReference = FirebaseStorage.getInstance().getReference();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
            Toast.makeText(this, "Provider null", Toast.LENGTH_SHORT).show();
        }


        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DAY_OF_MONTH);

        String[] options = {"Gaming", "Restaurant", "Gym", "Monument","Cinema", "Mosque", "Park", "Ice-cream Parlor","hospital", "other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.option_item, options);
        categories.setText(arrayAdapter.getItem(0).toString(), false);

        categories.setAdapter(arrayAdapter);

        placename = getIntent().getStringExtra("nameP");
        placecategory = getIntent().getStringExtra("categoryP");
        placedescription = getIntent().getStringExtra("DescriptionP");

        editplaceN.setText(placename);
        editplaceD.setText(placedescription);
        editplaceC.setText(placecategory);

    }

    private void checkAppPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {

            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                i.setData(uri);
                startActivity(i);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public void AddPicture(View view){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2 && data != null){
            imageuri = data.getData();
            image.setImageURI(imageuri);
        }
    }

    private void uploadImage(Uri imageuri) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    double latitude = ds.child("latitude").getValue(double.class);
                    array.add(latitude);
                    double longitude = ds.child("longitude").getValue(double.class);
                    array2.add(longitude);
                    array3.add(ds.getKey());
                }
                if (lat1 != null) {
                    for (int i = 0; i < array.size(); i++) {
                        String uid = array3.get(i);

                        DecimalFormat df = new DecimalFormat("#.#");
                        df.setRoundingMode(RoundingMode.CEILING);
                        float pk = (float) (180.0f / Math.PI);

                        float a1 = array.get(i).floatValue() / pk;
                        float a2 = array2.get(i).floatValue() / pk;

                        float str1 = Float.parseFloat(lat1);
                        float str2 = Float.parseFloat(lang1);

                        float b1 = str1 / pk;
                        float b2 = str2 / pk;

                        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
                        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
                        double t3 = Math.sin(a1) * Math.sin(b1);
                        double tt = Math.acos(t1 + t2 + t3);

                        double value = (6366000 * tt) / 1000;
                        String value2 = df.format(value);

                        if(Double.parseDouble(value2) <= 10){
                            System.out.println(featured);
                            StorageReference image = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));
                            image.putFile(imageuri).addOnSuccessListener(taskSnapshot -> image.getDownloadUrl().addOnSuccessListener(uri ->{
                                PlaceHelperClass AddNewUser = new PlaceHelperClass(placename,placedescription,uri.toString(),placecategory,location,lat1,lang1,userid,year,month,date,approve,disapprove,voted,featured);
                                notification.child(uid).child("notification").child(placename).setValue(AddNewUser);
                            }));
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);

        String count = "0";
        StorageReference image = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));
        image.putFile(imageuri).addOnSuccessListener(taskSnapshot -> image.getDownloadUrl().addOnSuccessListener(uri -> {
            PlaceHelperClass AddNewUser = new PlaceHelperClass(placename,placedescription,uri.toString(),placecategory,location,lat1,lang1,userid,year,month,date,approve,disapprove,voted,featured);
            rootNode.child(placename).setValue(AddNewUser);
            Toast.makeText(AddPlace.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            Remember2 session1 = new Remember2(AddPlace.this);
            session1.createRememberSession(count);
            finish();
        })).addOnProgressListener(snapshot -> {
            progressBar.setVisibility(View.VISIBLE);
            AddPlaceBtn.setVisibility(View.INVISIBLE);
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            AddPlaceBtn.setVisibility(View.VISIBLE);
            Toast.makeText(AddPlace.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri MUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(MUri));
    }

    public void GotoMain(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void GoToMap(View view){
        placename1 = PlaceName.getEditText().getText().toString().trim();
        placecategory1 = PlaceCategory.getEditText().getText().toString().trim();
        placedescription1 = PlaceDescription.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), MapShow.class);
        intent.putExtra("latitude1",latitude);
        intent.putExtra("longitude1",longitude);
        intent.putExtra("nameP",placename1);
        intent.putExtra("categoryP",placecategory1);
        intent.putExtra("DescriptionP",placedescription1);
        startActivity(intent);
    }

    public void GoPlaceData(View view){

        if(imageuri != null){
            uploadImage(imageuri);
        }else {
            Toast.makeText(this, "Please include an image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}