package com.radiostations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.radiostations.radioplayer_service.SlideAd_Service;

public class Radio_Detail_Activity extends AppCompatActivity implements Next_Prev_Callback {

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
        Radio_Pager_Adapter pagerAdapter = new Radio_Pager_Adapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Radio_Detail_Activity.this);
                int slideAD = sharedPreferences.getInt("SLIDE_AD", 0) + 1;
                SlideAd_Service.putInt(Radio_Detail_Activity.this, slideAD);
                if (slideAD >= 15) {
                    MobileAds.initialize(Radio_Detail_Activity.this, initializationStatus -> {
                    });
                    InterstitialAd mInterstitialAd = new InterstitialAd(Radio_Detail_Activity.this);
                    mInterstitialAd.setAdUnitId(getResources().getString(R.string.intrestial_ad));
                    mInterstitialAd.loadAd(new AdRequest.Builder().addKeyword("Insurance").build());
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        SlideAd_Service.putInt(Radio_Detail_Activity.this, 0);
                        mInterstitialAd = new InterstitialAd(Radio_Detail_Activity.this);
                        mInterstitialAd.setAdUnitId(getResources().getString(R.string.intrestial_ad));
                        mInterstitialAd.loadAd(new AdRequest.Builder().addKeyword("Insurance").build());
                    } else {
                        SlideAd_Service.putInt(Radio_Detail_Activity.this, slideAD);
                        mInterstitialAd = new InterstitialAd(Radio_Detail_Activity.this);
                        mInterstitialAd.setAdUnitId(getResources().getString(R.string.intrestial_ad));
                        mInterstitialAd.loadAd(new AdRequest.Builder().addKeyword("Insurance").build());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setCurrentItem(position);
    }


    @Override
    public void nextcallback(int i) {
        pager.setCurrentItem(i);
    }

}
