package com.example.sheldon.instagramclone.Search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SearchActivity extends AppCompatActivity {
    private static final int SEARCH_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpBottomNav();

    }

    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(SEARCH_ACTIVITY);
        item.setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
