package com.example.sheldon.instagramclone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int PROFILE_ACTIVITY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpBottomNav();
        setUpToolbar();

    }

    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(PROFILE_ACTIVITY);
        item.setChecked(true);

    }

    private void setUpToolbar() {
        ImageView ellipses = (ImageView) findViewById(R.id.settingsIcon);
        ellipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}