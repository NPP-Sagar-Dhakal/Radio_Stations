package com.radiostations.radio_favorites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.radiostations.All_Radio_Fragment;
import com.radiostations.R;

import java.util.List;


public class Favorite_Radio_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    public static List<Favorites_Radio_Items> radioItems;

    public Favorite_Radio_Adapter(Context context, List<Favorites_Radio_Items> radioItems) {
        this.context = context;
        Favorite_Radio_Adapter.radioItems = radioItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.radio_recycler_item_layout, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderr, int position) {
        final RadioViewHolder holder = (RadioViewHolder) holderr;

        holder.radio_Name.setText(radioItems.get(position).stationName);
        holder.radio_Detail.setText(radioItems.get(position).stationDetail);

        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getColor(position);
        TextDrawable letterDrawable = TextDrawable.builder().buildRoundRect(radioItems.get(position).stationName.substring(0, 1).toUpperCase(), color, 8);
        try {
            Glide.with(context).load(radioItems.get(position).stationimage).placeholder(letterDrawable).error(letterDrawable).into(holder.radio_Image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.radioConstraint.setOnClickListener(v -> {
            Intent intent = new Intent(context, Fav_Detail_Activity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        if (All_Radio_Fragment.favoriteDatabase.favoriteDao().isFavorite(radioItems.get(position).stationName) == 1) {
            holder.addto_fav.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.addto_fav.setImageResource(R.drawable.ic_heart_empty);
        }
        holder.addto_fav.setOnClickListener(v -> {
            Favorites_Radio_Items favoriteList = new Favorites_Radio_Items();
            favoriteList.setStationName(radioItems.get(position).stationName);
            favoriteList.setStationDetail(radioItems.get(position).stationDetail);
            favoriteList.setStationimage(radioItems.get(position).stationimage);
            favoriteList.setStationLink(radioItems.get(position).stationLink);
            favoriteList.setStationLocation(radioItems.get(position).stationLocation);

            if (All_Radio_Fragment.favoriteDatabase.favoriteDao().isFavorite(radioItems.get(position).stationName) != 1) {
                holder.addto_fav.setImageResource(R.drawable.ic_heart_filled);
                All_Radio_Fragment.favoriteDatabase.favoriteDao().addData(favoriteList);
                Toast.makeText(context, "Added to Favorite List", Toast.LENGTH_SHORT).show();

            } else {
                holder.addto_fav.setImageResource(R.drawable.ic_heart_empty);
                All_Radio_Fragment.favoriteDatabase.favoriteDao().delete(favoriteList);
                Toast.makeText(context, "Removed from Favorite List", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return radioItems.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static class RadioViewHolder extends RecyclerView.ViewHolder {

        final TextView radio_Name;
        final TextView radio_Detail;
        final ImageView radio_Image;
        final ImageView addto_fav;
        final ConstraintLayout radioConstraint;

        RadioViewHolder(View itemView) {
            super(itemView);
            addto_fav = itemView.findViewById(R.id.favourite);
            radio_Name = itemView.findViewById(R.id.radio_name);
            radio_Detail = itemView.findViewById(R.id.radio_detail);
            radio_Image = itemView.findViewById(R.id.radio_image);
            radioConstraint = itemView.findViewById(R.id.radio_constraint);

        }
    }

}
