package com.radiostations.radio_recent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.radiostations.Next_Prev_Callback;
import com.radiostations.R;
import com.radiostations.Radio_Activity;
import com.radiostations.radioplayer_service.SlideAd_Service;

public class Recent_Detail_Activity extends AppCompatActivity implements Next_Prev_Callback {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_radio_detail);

        int position = getIntent().getIntExtra("position", 0);
        pager = findViewById(R.id.radioViewPager);
        Recent_Pager_Adapter pagerAdapter = new Recent_Pager_Adapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            InterstitialAd mInterstitialAd;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Recent_Detail_Activity.this);
                int slideAD = sharedPreferences.getInt("SLIDE_AD", 0) + 1;
                SlideAd_Service.putInt(Recent_Detail_Activity.this, slideAD);
                if (slideAD == 15) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(Recent_Detail_Activity.this, getResources().getString(R.string.interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            interstitialAd.show(Recent_Detail_Activity.this);
                            SlideAd_Service.putInt(Recent_Detail_Activity.this, 0);
                            super.onAdLoaded(interstitialAd);
                        }
                    });
                } else if (slideAD >= 20) {
                    SlideAd_Service.putInt(Recent_Detail_Activity.this, 0);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(Recent_Detail_Activity.this, getResources().getString(R.string.interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            interstitialAd.show(Recent_Detail_Activity.this);
                            super.onAdLoaded(interstitialAd);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void nextcallback(int i) {
        pager.setCurrentItem(i);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Recent_Detail_Activity.this, Radio_Activity.class);
        startActivity(intent);
    }

}
