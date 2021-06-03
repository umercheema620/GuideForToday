package com.example.cityguide.HomeAdaptors.NewSpots;

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
import com.example.cityguide.PlaceFragment;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NewSpotsAdaptor extends FirebaseRecyclerAdapter<NewSpotsHelperClass, NewSpotsAdaptor.myviewholder> {

    int year,month,day;

    public NewSpotsAdaptor(@NonNull FirebaseRecyclerOptions<NewSpotsHelperClass> options,int year,int month,int day) {
        super(options);
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull NewSpotsAdaptor.myviewholder holder, int position, @NonNull NewSpotsHelperClass model) {
        LocalDate current = LocalDate.of(this.year,this.month,this.day);
        LocalDate placedate = LocalDate.of(model.getYear(),model.getMonth(),model.getDay());

        long days = ChronoUnit.DAYS.between(placedate,current);
        System.out.println(days);
        if(days <= 3) {
            holder.newSpotCard.setVisibility(View.VISIBLE);
        }

            holder.Placestitle.setText(model.getName());
            holder.Placesdesc.setText(model.getDescription());
            holder.mRatingBar.setRating(model.getRating());
            Glide.with(holder.Placesimage.getContext()).load(model.getImageUrl()).into(holder.Placesimage);

            holder.itemView.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new PlaceFragment(model.getName(), model.getImageUrl())).addToBackStack(null).commit();
            });

    }

    @NonNull
    @Override
    public NewSpotsAdaptor.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent, false);
        return new NewSpotsAdaptor.myviewholder(view);
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
