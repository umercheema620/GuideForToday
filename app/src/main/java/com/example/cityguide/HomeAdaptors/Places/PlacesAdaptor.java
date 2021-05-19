package com.example.cityguide.HomeAdaptors.Places;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.AddPlace;
import com.example.cityguide.MainActivity;
import com.example.cityguide.PlaceFragment;
import com.example.cityguide.R;
import com.example.cityguide.place_list;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PlacesAdaptor extends FirebaseRecyclerAdapter<PlacesHelperClass,PlacesAdaptor.myviewholder> {

    double latitude,longitude;
    public PlacesAdaptor(@NonNull FirebaseRecyclerOptions<PlacesHelperClass> options,double latitude, double longitude) {
        super(options);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PlacesHelperClass model) {

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        float pk = (float) (180.0f/Math.PI);

        float a1 = (float) latitude/pk;
        float a2 = (float) longitude/pk;

        float str1 = Float.parseFloat(model.getLatitude());
        float str2 = Float.parseFloat(model.getLongitude());

        float b1 = str1/pk;
        float b2 = str2/pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        double value = (6366000 * tt)/1000;
        String value2 = df.format(value);

        holder.Placedistance.setText(value2);
        holder.Placestitle.setText(model.getName());
        holder.Placesdesc.setText(model.getDescription());
        Glide.with(holder.Placesimage.getContext()).load(model.getImageUrl()).into(holder.Placesimage);

        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.place_list,new PlaceFragment(model.getName(),model.getImageUrl())).addToBackStack(null).commit();
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView Placesimage;
        TextView Placestitle, Placesdesc,Placedistance;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Placesimage = (ImageView) itemView.findViewById(R.id.list_image);
            Placestitle = (TextView) itemView.findViewById(R.id.list_title);
            Placesdesc = (TextView) itemView.findViewById(R.id.list_description);
            Placedistance = (TextView) itemView.findViewById(R.id.distance);
        }
    }
}

