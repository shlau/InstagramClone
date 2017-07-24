package com.example.sheldon.instagramclone.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.sheldon.instagramclone.Home.HomeActivity;
import com.example.sheldon.instagramclone.Likes.LikesActivity;
import com.example.sheldon.instagramclone.Profile.ProfileActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Search.SearchActivity;
import com.example.sheldon.instagramclone.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by sheldon on 7/4/2017.
 * Setup functionality for the bottom navigation
 */

public class BottomNavHelper {

    /**
     * Disable navigation bar text and animation
     * @param navBar The navigation bar
     */
    public static void disableAnimation(BottomNavigationViewEx navBar) {
        navBar.enableAnimation(false);
        navBar.enableItemShiftingMode(false);
        navBar.enableShiftingMode(false);
        navBar.setTextVisibility(false);
    }

    /**
     * Enables navigation bar functionality by setting a menu item listener.
     * @param context The current class associated with the activity
     * @param navBar The navigation bar
     */
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
