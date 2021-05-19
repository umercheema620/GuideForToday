package com.example.cityguide.HomeAdaptors.MostViewed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityguide.PlaceFragment;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class MostViewedAdapter extends FirebaseRecyclerAdapter<MostViewedHelperClass, MostViewedAdapter.myviewholder> {

    public MostViewedAdapter(@NonNull FirebaseRecyclerOptions<MostViewedHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull MostViewedHelperClass model) {
        holder.Placestitle.setText(model.getName());
        holder.Placesdesc.setText(model.getDescription());
        Glide.with(holder.Placesimage.getContext()).load(model.getImageUrl()).into(holder.Placesimage);

        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new PlaceFragment(model.getName(),model.getImageUrl())).addToBackStack(null).commit();
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

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Placesimage = itemView.findViewById(R.id.most_image);
            Placestitle = itemView.findViewById(R.id.most_title);
            Placesdesc = itemView.findViewById(R.id.most_description);
        }
    }
}
