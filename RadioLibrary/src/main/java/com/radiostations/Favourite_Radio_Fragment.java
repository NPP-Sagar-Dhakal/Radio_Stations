package com.radiostations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.radiostations.radio_favorites.Favorite_Radio_Adapter;
import com.radiostations.radio_favorites.Favorites_Radio_Items;

import java.util.Collections;
import java.util.List;


public class Favourite_Radio_Fragment extends Fragment {

    public Favourite_Radio_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_radio, container, false);
        RecyclerView favourite_Radio_Recycler = view.findViewById(R.id.all_Radio_Recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        favourite_Radio_Recycler.setLayoutManager(layoutManager);
        try {
            List<Favorites_Radio_Items> favoriteLists = All_Radio_Fragment.favoriteDatabase.favoriteDao().getFavoriteData();
            Collections.reverse(favoriteLists);
            Favorite_Radio_Adapter adapter = new Favorite_Radio_Adapter(Favourite_Radio_Fragment.this.getContext(), favoriteLists);
            favourite_Radio_Recycler.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

}
