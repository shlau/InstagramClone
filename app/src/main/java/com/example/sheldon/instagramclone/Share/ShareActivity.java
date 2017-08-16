package com.example.sheldon.instagramclone.Share;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.Permissions;
import com.example.sheldon.instagramclone.Util.SectionStateAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ShareActivity extends AppCompatActivity {
    private static final int SHARE_ACTIVITY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if(!permissionsGranted(Permissions.PERMISSIONS)) {
            verifyPermissions(Permissions.PERMISSIONS);
        }
        else {
            setUpViewPager();
        }
        //setUpBottomNav();

    }

    private void setUpViewPager() {
        SectionStateAdapter adapter = new SectionStateAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment(), "Gallery");
        adapter.addFragment(new PhotoFragment(), "Photo");
        ViewPager pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);

        TabLayout bottomTabs = (TabLayout) findViewById(R.id.bottom_tabs);
        bottomTabs.setupWithViewPager(pager);
        bottomTabs.getTabAt(0).setText("GALLERY");
        bottomTabs.getTabAt(1).setText("PHOTO");


    }
    private boolean permissionsGranted(String[] permissions) {
        for(int i = 0; i < permissions.length; i++) {
            if(!permissionGranted(permissions[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean permissionGranted(String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(ShareActivity.this, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(ShareActivity.this, permissions, 1);
    }

    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(SHARE_ACTIVITY);
        item.setChecked(true);

    }
}