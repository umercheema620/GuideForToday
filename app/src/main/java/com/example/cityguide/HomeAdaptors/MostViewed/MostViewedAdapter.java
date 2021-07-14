package com.example.cityguide.HomeAdaptors.MostViewed;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.PlaceData;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class MostViewedAdapter extends FirebaseRecyclerAdapter<FeaturedHelperClass, MostViewedAdapter.myviewholder> {
    int year,month,day;
    Context context;
    long days;
    public MostViewedAdapter(@NonNull FirebaseRecyclerOptions<FeaturedHelperClass> options,int year,int month,int day, Context context) {
        super(options);
        this.year = year;
        this.month = month;
        this.day = day;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull FeaturedHelperClass model) {
        LocalDate current = LocalDate.of(this.year,this.month,this.day);
        LocalDate placedate = LocalDate.of(model.getYear(),model.getMonth(),model.getDay());
        days = ChronoUnit.DAYS.between(placedate,current);

        holder.Placestitle.setText(model.getName());
        holder.Placesdesc.setText(model.getDescription());
        holder.mRatingBar.setRating(model.getRating());
        Glide.with(holder.Placesimage.getContext()).load(model.getImageUrl()).into(holder.Placesimage);



        if(days < 3) {
            System.out.println(days);
            holder.newSpotCard.setVisibility(View.VISIBLE);
        }else{
            holder.newSpotCard.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaceData.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("image", model.getImageUrl());
            intent.putExtra("category", model.getCategory());
            intent.putExtra("EventorPlace",1);
            intent.putExtra("latitude",model.getLatitude());
            intent.putExtra("longitude",model.getLongitude());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MostViewedAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent, false);
        return new MostViewedAdapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView Placesimage;
        TextView Placestitle, Placesdesc;
        RatingBar mRatingBar;
        RelativeLayout newSpotCard;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Placesimage = itemView.findViewById(R.id.most_image);
            Placestitle = itemView.findViewById(R.id.most_title);
            Placesdesc = itemView.findViewById(R.id.most_description);
            mRatingBar = itemView.findViewById(R.id.most_rating);
            newSpotCard = itemView.findViewById(R.id.new_spot_card);
        }
    }
}
