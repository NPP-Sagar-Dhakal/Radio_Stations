package com.radiostations.radio_favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class Fav_Pager_Adapter extends FragmentPagerAdapter {

    Fav_Pager_Adapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    public Fragment getItem(int arg0) {
        Fav_Detail_Fragement radioDetailFragement = new Fav_Detail_Fragement();
        Bundle data = new Bundle();
        data.putInt("current_page", arg0 + 1);
        radioDetailFragement.setArguments(data);
        return radioDetailFragement;
    }

    public int getCount() {
        return Favorite_Radio_Adapter.radioItems.size();
    }

}
