package com.radiostations;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.radiostations.radio_recent.Recent_All_Radio_Adapter;
import com.radiostations.radio_recent.Recent_Radio_Items;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recent_Radio_Fragment extends Fragment {


    public Recent_Radio_Fragment() {
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
            List<Recent_Radio_Items> recentRadioItems = All_Radio_Fragment.recentDatabase.favoriteDao().getFavoriteData();
            SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            String recentradio = m.getString("currentradio", "");
            String newCurrentRadio = recentRadioItems.get(0).stationName;
            if (!Objects.requireNonNull(recentradio).equals(newCurrentRadio)) {
                Collections.reverse(recentRadioItems);
            }
            Recent_All_Radio_Adapter adapter = new Recent_All_Radio_Adapter(Recent_Radio_Fragment.this.getContext(), recentRadioItems);
            favourite_Radio_Recycler.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
