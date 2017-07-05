package com.example.sheldon.instagramclone.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.sheldon.instagramclone.HomeActivity;
import com.example.sheldon.instagramclone.LikesActivity;
import com.example.sheldon.instagramclone.ProfileActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.SearchActivity;
import com.example.sheldon.instagramclone.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by sheldon on 7/4/2017.
 */

public class BottomNavHelper {

    public static void disableAnimation(BottomNavigationViewEx navBar) {
        navBar.enableAnimation(false);
        navBar.enableItemShiftingMode(false);
        navBar.enableShiftingMode(false);
        navBar.setTextVisibility(false);
    }

    public static void enableNavBar(final Context context, BottomNavigationViewEx navBar) {
       navBar.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener()  {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_android:
                        Intent intent3 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_circle:
                        Intent intent4 = new Intent(context, ShareActivity.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_alert:
                        Intent intent5= new Intent(context, LikesActivity.class);
                        context.startActivity(intent5);
                        break;
                }
                return false;
            }

        });
    }
}
