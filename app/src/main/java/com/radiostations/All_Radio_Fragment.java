package com.radiostations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.radiostations.radio_favorites.FavoriteDatabase;
import com.radiostations.radio_recent.RecentDatabase;

import java.util.List;
import java.util.Objects;

public class All_Radio_Fragment extends Fragment {

    public static FavoriteDatabase favoriteDatabase;
    public static RecentDatabase recentDatabase;

    public static Radio_Recycler_Adapter adapter;
    public static List<NepalNewsItem.NepalNews.NepaliRadios> radioItems;

    private RecyclerView all_Radio_Recycler;

    public All_Radio_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_radio, container, false);
        all_Radio_Recycler = view.findViewById(R.id.all_Radio_Recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        all_Radio_Recycler.setLayoutManager(layoutManager);

        try {
            favoriteDatabase = Room.databaseBuilder(Objects.requireNonNull(this.getContext()), FavoriteDatabase.class, "favouritedatabase").allowMainThreadQueries().build();
            recentDatabase = Room.databaseBuilder(this.getContext(), RecentDatabase.class, "recentdatabse").allowMainThreadQueries().build();
            radioItems = Radio_Activity.radioItems;
            adapter = new Radio_Recycler_Adapter(All_Radio_Fragment.this.getContext(), radioItems);
            All_Radio_Fragment.this.all_Radio_Recycler.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
