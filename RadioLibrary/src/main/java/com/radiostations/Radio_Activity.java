package com.radiostations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radiostations.radioplayer_service.Disclaimer_Dialog;
import com.radiostations.radioplayer_service.RadioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Radio_Activity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private RadioService service;
    public static List<RadioItems.NepalNews.NepaliRadios> radioItems;
    SharedPreferences sharedPreferences;
    CoordinatorLayout mainLayout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Radio_Activity.this);
        progressDialog = new ProgressDialog(Radio_Activity.this);
        progressDialog.setMessage("Fetching database first time. Please Wait !"); // Setting Message
        progressDialog.setTitle("Fetching Database"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        String responses = sharedPreferences.getString("radioJsonData", "");
        String databaseLink = sharedPreferences.getString("databaseLink", "");

        if (Objects.requireNonNull(responses).equals("")) {
            progressDialog.show();
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference().child("Online Radio").child("Nepali Radio");
            scoresRef.keepSynced(false);
            DatabaseReference jokeslinks = FirebaseDatabase.getInstance().getReference().child("Online Radio").child("Nepali Radio");
            jokeslinks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String databaseUrl = Objects.requireNonNull(dataSnapshot.child("database").getValue()).toString();

                        fetchRadioData(databaseUrl, !Objects.requireNonNull(databaseLink).equals(databaseUrl));
                        FirebaseDatabase.getInstance().goOffline();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            if (getIntent().getBooleanExtra("realtimeURL", false)) {
                try {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference().child("Online Radio").child("Nepali Radio");
                scoresRef.keepSynced(false);
                DatabaseReference jokeslinks = FirebaseDatabase.getInstance().getReference().child("Online Radio").child("Nepali Radio");
                jokeslinks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String databaseUrl = Objects.requireNonNull(dataSnapshot.child("database").getValue()).toString();
                            fetchRadioData(databaseUrl, !Objects.requireNonNull(databaseLink).equals(databaseUrl));
                            FirebaseDatabase.getInstance().goOffline();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            } else {
                parseJson(responses);
            }
        }

        mainLayout = findViewById(R.id.mainLayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Radio_Activity.this);

        service = new RadioService();
        Toolbar radio_Toolbar = findViewById(R.id.radio_Toolbar);
        setSupportActionBar(radio_Toolbar);

        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchRadioData(String databaseUrl, boolean b) {
        String responses = sharedPreferences.getString("radioJsonData", "");
        if (Objects.requireNonNull(responses).equals("")) {
            progressDialog.show();
            AsyncTask.execute(() -> {
                StringRequest stringRequest = new StringRequest(databaseUrl, response -> {
                    sharedResponse(response, "radioJsonData");
                    parseJson(response);
                }, error -> {
                    //Todo
                    Toast.makeText(Radio_Activity.this, "No Internet Connection/Server is busy. Please close the app and try again later.", Toast.LENGTH_LONG).show();
                });
                RequestQueue queue = Volley.newRequestQueue(Radio_Activity.this);
                queue.add(stringRequest);
            });

        } else {
            parseJson(responses);
            if (b) {
                AsyncTask.execute(() -> {
                    StringRequest stringRequest = new StringRequest(databaseUrl, response -> {
                        sharedResponse(response, "radioJsonData");
                        sharedResponse(databaseUrl, "databaseLink");
                    }, error -> {

                    });
                    RequestQueue queue = Volley.newRequestQueue(Radio_Activity.this);
                    queue.add(stringRequest);
                });
            }
        }
    }

    private void parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        RadioItems newsItems = gson.fromJson(response, RadioItems.class);
        try {
            radioItems = newsItems.NepalNews.get(0).NepaliRadios;
            TabLayout radio_TabLayout = findViewById(R.id.radio_Tab);
            ViewPager radio_Main_ViewPager = findViewById(R.id.radio_ViewPager);
            setupViewPgar(radio_Main_ViewPager);
            radio_TabLayout.setupWithViewPager(radio_Main_ViewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void sharedResponse(@NonNull String response, String key) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = m.edit();
        editor.putString(key, response);
        editor.apply();
    }

    private void setupViewPgar(ViewPager pager) {
        Main_Radio_ViewPager_Adapter pager_adapter = new Main_Radio_ViewPager_Adapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager_adapter.addFragment(new All_Radio_Fragment(), "All");
        pager_adapter.addFragment(new Favourite_Radio_Fragment(), "Favourites");
        pager_adapter.addFragment(new Recent_Radio_Fragment(), "Recent");
        pager.setAdapter(pager_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.radio_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.radio_Search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        List<RadioItems.NepalNews.NepaliRadios> stringList = new ArrayList<>();

        for (RadioItems.NepalNews.NepaliRadios string : All_Radio_Fragment.radioItems) {
            if (string.stationName.toLowerCase().contains(userInput)) {
                stringList.add(string);
            }
        }
        try {
            All_Radio_Fragment.adapter.updateList(stringList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.radio_timer:
                PopupMenu popup = new PopupMenu(Radio_Activity.this, findViewById(R.id.radio_timer));
                popup.setOnMenuItemClickListener(item1 -> {
                    switch (item1.getItemId()) {
                        case R.id.ten_minutes:
                            service.sleepTimer(this, 10);
                            return true;
                        case R.id.twentyfive_minutes:
                            service.sleepTimer(this, 25);
                            return true;

                        case R.id.fourty_minutes:
                            service.sleepTimer(this, 40);
                            return true;

                        case R.id.sixty_minutes:
                            service.sleepTimer(this, 60);
                            return true;

                    }
                    return true;
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.radio_timer_menu, popup.getMenu());
                popup.show();
                break;
            case R.id.more_rate:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Radio_Activity.this, R.style.MyAlertDialogStyle);
                alertDialogBuilder.setMessage("If you are facing any problems, please send us a message else, please give us 5-star ratings. It helps us to do more hard work on this app.");

                alertDialogBuilder.setPositiveButton("Rate us", (dialog, which) -> {
                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                });
                alertDialogBuilder.setNegativeButton("Send Problems", (dialog, which) -> {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cherrydigital.care@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Problems & Feedback from-- " + getPackageName());
                    email.putExtra(Intent.EXTRA_TEXT, "Note : Dont't clear the subject please,\n\n");
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Send Mail"));
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.more_invite:
                Intent txtIntent = new Intent(Intent.ACTION_SEND);
                txtIntent.setType("text/plain");
                txtIntent.putExtra(Intent.EXTRA_TEXT, "One of the best app to listen online radio stations. Download now from Google Play Store \n\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(txtIntent, "Share"));
                break;

            case R.id.add_new:
                Intent add_new = new Intent(Intent.ACTION_SEND);
                add_new.putExtra(Intent.EXTRA_EMAIL, new String[]{"cherrydigital.care@gmail.com"});
                add_new.putExtra(Intent.EXTRA_SUBJECT, "New Radio Suggestion from " + getPackageName());
                add_new.putExtra(Intent.EXTRA_TEXT, "Note : Dont't clear the subject please,\n\nRadio Name = \n\nRadio Details = ");
                add_new.setType("message/rfc822");
                startActivity(Intent.createChooser(add_new, "Send Mail"));
                break;
            case R.id.more_disclaimer:
                Disclaimer_Dialog exampleDialog = new Disclaimer_Dialog();
                exampleDialog.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.more_privacy:
                String url = "https://sites.google.com/view/hamronepal-privacy-policy/home";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.more_send:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cherrydigital.care@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Problems & Feedback from" + getPackageName());
                email.putExtra(Intent.EXTRA_TEXT, "Note : Dont't clear the subject please,\n\n");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Send Mail"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
