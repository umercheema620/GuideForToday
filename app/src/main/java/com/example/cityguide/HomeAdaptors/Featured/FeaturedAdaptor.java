package com.example.cityguide.HomeAdaptors.Featured;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.PlaceData;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class FeaturedAdaptor extends FirebaseRecyclerAdapter<FeaturedHelperClass,FeaturedAdaptor.myviewholder> {
    Context context;
    public FeaturedAdaptor(@NonNull FirebaseRecyclerOptions<FeaturedHelperClass> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull FeaturedHelperClass model) {
        holder.Featuredtitle.setText(model.getName());
        holder.Featureddesc.setText(model.getDescription());
        holder.rating.setRating(model.getRating());
        Glide.with(holder.Featuredimage.getContext()).load(model.getImageUrl()).into(holder.Featuredimage);

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
