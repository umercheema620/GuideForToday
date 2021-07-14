package com.example.cityguide.HomeAdaptors.Events;

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
import com.example.cityguide.PlaceData;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EventAdaptor extends FirebaseRecyclerAdapter<EventHelperClass, EventAdaptor.myviewholder> {
    Context context;
    public EventAdaptor(@NonNull FirebaseRecyclerOptions<EventHelperClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventAdaptor.myviewholder holder, int position, @NonNull EventHelperClass model) {
        holder.Featuredtitle.setText(model.getName());
        holder.Featureddesc.setText(model.getDesc());
        Glide.with(holder.Featuredimage.getContext()).load(model.getImageUrl()).into(holder.Featuredimage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaceData.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("image", model.getImageUrl());
            intent.putExtra("category", model.getActivities());
            intent.putExtra("EventorPlace",2);
            intent.putExtra("latitude",Double.toString(model.getLatitude()));
            intent.putExtra("longitude",Double.toString(model.getLongitude()));
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public EventAdaptor.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new EventAdaptor.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView Featuredimage;
        TextView Featuredtitle, Featureddesc;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Featuredimage = itemView.findViewById(R.id.event_image);
            Featuredtitle = itemView.findViewById(R.id.event_title);
            Featureddesc = itemView.findViewById(R.id.event_description);
        }
    }
}
