package com.example.cityguide.HomeAdaptors.Places;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.PlaceData;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PlacesAdaptor2 extends FirebaseRecyclerAdapter<FeaturedHelperClass,PlacesAdaptor2.myviewholder> {

    double latitude,longitude;
    Context context;
    public PlacesAdaptor2(@NonNull FirebaseRecyclerOptions<FeaturedHelperClass> options, double latitude, double longitude, Context context) {
        super(options);
        this.latitude = latitude;
        this.longitude = longitude;
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull FeaturedHelperClass model) {

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
            Intent intent = new Intent(context, PlaceData.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("image", model.getImageUrl());
            intent.putExtra("category", model.getCategory());
            intent.putExtra("latitude",model.getLatitude());
            intent.putExtra("longitude",model.getLongitude());
            context.startActivity(intent);
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
