package com.example.sheldon.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.GridImageAdapter;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 *  Created by sheldon on 7/8/2017.
 *  The profile page
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int PROFILE_ACTIVITY = 4;
    private static final int NUM_COLUMNS = 4;
    private ProgressBar mProgressBar;
    private ImageView mProfilePhoto;
    private Context mContext;
    private BottomNavigationViewEx botNavView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Currently in profile activity");
        setContentView(R.layout.activity_profile);
        initFragment();
//        botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
//        setUpActivityWidgets();
//        setProfilePhoto();
//        setUpBottomNav();
//        setUpToolbar();
//        tempGridSetUp();

    }

    private void initFragment() {
        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
//
//    private void tempGridSetUp() {
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://i.imgur.com/mr7QEn4.jpg");
//        imgURLs.add("https://i.imgur.com/CMIngP5.jpg");
//        imgURLs.add("https://i.imgur.com/ml3dPcQ.jpg");
//        imgURLs.add("https://i.imgur.com/lfKEIoL.jpg");
//        imgURLs.add("https://i.imgur.com/jxxrQBp.jpg");
//        imgURLs.add("https://i.imgur.com/zkIlkoK.png");
//        imgURLs.add("https://i.imgur.com/399rOCK.jpg");
//        imgURLs.add("https://i.imgur.com/zE7xY6m.jpg");
//        imgURLs.add("https://i.imgur.com/oVG06v0.jpg");
//        imgURLs.add("https://i.imgur.com/69RAh5r.jpg");
//        imgURLs.add("https://i.imgur.com/PcBVp3H.jpg");
//        imgURLs.add("https://i.imgur.com/vsA41Vd.jpg");
//        imgURLs.add("https://i.imgur.com/c7uXAlW.png");
//        imgURLs.add("https://i.imgur.com/YNCIDhE.png");
//        imgURLs.add("https://i.imgur.com/vIw0BXG.jpg");
//        setUpGridView(imgURLs);
//
//
//    }
//
//    /**
//     * Set up a grid of images with a given list of image urls
//     * @param imgURLs the image urls
//     */
//    private void setUpGridView(ArrayList<String> imgURLs) {
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
//        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//        int imageWidth = screenWidth / NUM_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//        gridView.setAdapter(adapter);
//    }
//
//    /**
//     * load the user profile photo
//     */
//    private void setProfilePhoto() {
//        UniversalImageLoader.setImage("http://www.freeiconspng.com/uploads/profile-icon-28.png", mProfilePhoto, mProgressBar, "");
//    }
//
//    /**
//     * initialize widgets
//     */
//    private void setUpActivityWidgets() {
//        mProgressBar = (ProgressBar)findViewById(R.id.profileProgressBar);
//        mProgressBar.setVisibility(View.GONE);
//        mProfilePhoto = (ImageView)findViewById(R.id.profile_image);
//        mContext = ProfileActivity.this;
//    }
//
//    /**
//     * Set up the bottom navigation with the appropriate item selected
//     */
//    private void setUpBottomNav() {
//        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
//        BottomNavHelper.disableAnimation(botNavView);
//        BottomNavHelper.enableNavBar(this, botNavView);
//        Menu menu = botNavView.getMenu();
//        MenuItem item = menu.getItem(PROFILE_ACTIVITY);
//        item.setChecked(true);
//
//    }
//
//    /**
//     * Set up navigation to edit profile
//     */
//    private void setUpToolbar() {
//        ImageView ellipses = (ImageView) findViewById(R.id.settingsIcon);
//        ellipses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

}