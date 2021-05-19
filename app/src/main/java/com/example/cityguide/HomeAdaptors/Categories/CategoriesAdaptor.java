package com.example.cityguide.HomeAdaptors.Categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.R;
import com.example.cityguide.place_list;

import java.util.ArrayList;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.CategoriesViewHolder>{

    ArrayList<CategoriesHelperClass> categories;
    String category;
    double latitude,longitude;
    Context context;

    public CategoriesAdaptor(ArrayList<CategoriesHelperClass> categories,double latitude, double longitude,Context context) {
        this.categories = categories;
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card,parent,false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {

        CategoriesHelperClass categoriesHelperClass = categories.get(position);

        holder.image.setImageResource(categoriesHelperClass.getImage());
        holder.title.setText(categoriesHelperClass.getTitle());
        holder.card.setBackground(categoriesHelperClass.getGradient());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = categoriesHelperClass.getCategory();
                Intent intent = new Intent(context, place_list.class);
                intent.putExtra("comingfrom",category);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        RelativeLayout card;
        TextView title;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.categories_image);
            title = itemView.findViewById(R.id.categories_title);
            card = itemView.findViewById(R.id.card_relative_background);
        }
    }
}
