package com.example.cityguide.HomeAdaptors.WishList;

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
import com.example.cityguide.PlaceData;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class WishListAdapter extends FirebaseRecyclerAdapter<WishListHelperClass, WishListAdapter.myviewholder> {

    Context context;
    public WishListAdapter(@NonNull FirebaseRecyclerOptions<WishListHelperClass> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull WishListAdapter.myviewholder holder, int position, @NonNull WishListHelperClass model) {
        holder.Placestitle.setText(model.getName());
        holder.Placesdesc.setText(model.getDescription());
        Glide.with(holder.Placesimage.getContext()).load(model.getImageUrl()).into(holder.Placesimage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaceData.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("image", model.getImageUrl());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public WishListAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card_design, parent, false);
        return new WishListAdapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView Placesimage;
        TextView Placestitle, Placesdesc;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Placesimage = itemView.findViewById(R.id.most_image);
            Placestitle = itemView.findViewById(R.id.most_title);
            Placesdesc = itemView.findViewById(R.id.most_description);
        }
    }
}
