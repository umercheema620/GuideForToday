package com.example.cityguide.HomeAdaptors.Featured;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.PlaceFragment;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class FeaturedAdaptor extends FirebaseRecyclerAdapter<FeaturedHelperClass,FeaturedAdaptor.myviewholder> {
    public FeaturedAdaptor(@NonNull FirebaseRecyclerOptions<FeaturedHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull FeaturedHelperClass model) {
        holder.Featuredtitle.setText(model.getName());
        holder.Featureddesc.setText(model.getDescription());
        holder.rating.setRating(model.getRating());
        Glide.with(holder.Featuredimage.getContext()).load(model.getImageUrl()).into(holder.Featuredimage);

        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new PlaceFragment(model.getName(),model.getImageUrl())).addToBackStack(null).commit();
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView Featuredimage;
        TextView Featuredtitle, Featureddesc;
        RatingBar rating;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Featuredimage = itemView.findViewById(R.id.featured_image);
            Featuredtitle = itemView.findViewById(R.id.featured_title);
            Featureddesc = itemView.findViewById(R.id.featured_description);
            rating = itemView.findViewById(R.id.featured_rating);
        }
    }


}
