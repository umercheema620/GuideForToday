package com.example.cityguide.Database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.R;
import com.example.cityguide.User.ReviewPlaceData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotificationAdaptor extends FirebaseRecyclerAdapter<PlaceHelperClass,NotificationAdaptor.myviewholder> {

    Context context;
    public NotificationAdaptor(@NonNull FirebaseRecyclerOptions<PlaceHelperClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PlaceHelperClass model) {

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReviewPlaceData.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("des",model.getDescription());
            intent.putExtra("image",model.getImageUrl());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView Placesimage;
        TextView Placestitle;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Placesimage = itemView.findViewById(R.id.not_image);
            Placestitle = itemView.findViewById(R.id.not_title);
        }
    }
}
