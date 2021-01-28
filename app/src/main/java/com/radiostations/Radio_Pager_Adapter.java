package com.radiostations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


class Radio_Pager_Adapter extends FragmentPagerAdapter {

    Radio_Pager_Adapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    public Fragment getItem(int arg0) {
        Radio_Detail_Fragement radioDetailFragement = new Radio_Detail_Fragement();
        Bundle data = new Bundle();
        data.putInt("current_page", arg0 + 1);
        radioDetailFragement.setArguments(data);
        return radioDetailFragement;
    }


    public int getCount() {
        try {
            return Radio_Recycler_Adapter.radioItems.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
