package com.example.sheldon.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.sheldon.instagramclone.Login.LoginActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.FireBaseMethods;
import com.example.sheldon.instagramclone.Util.SectionStateAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by sheldon on 7/8/2017.
 * The account settings page
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final int PROFILE_ACTIVITY = 2;
    private Context mContext;
    public SectionStateAdapter adapter;
    private RelativeLayout mLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        mContext = AccountSettingsActivity.this;
        ImageView back = (ImageView) findViewById(R.id.settingsBack);
        mLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AccountSettingsActivity", "onClick: returning to ProfileActivity ");
                finish();
            }
        });
        setUpList();
        setUpAdapter();
        setUpListListener();
        setUpBottomNav();
        getIncomingIntent();
    }

    /**
     * Initialize adapter
     */
    private void setUpAdapter() {
        adapter = new SectionStateAdapter(getSupportFragmentManager());
        adapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));
        adapter.addFragment(new SignOutFragment(), getString(R.string.sign_out));
    }

    /**
     * Display fragment at index in the viewpager
     * @param index the fragment index
     */
    public void setUpViewPager(int index) {
        // hide the previous layout so that only the new fragment is shown
        mLayout.setVisibility(View.GONE);

        ViewPager vp = (ViewPager) findViewById(R.id.container);
        vp.setAdapter(adapter);
        vp.setCurrentItem(index);

    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        FireBaseMethods fireBaseMethods = new FireBaseMethods(AccountSettingsActivity.this);
        boolean hasImage = intent.hasExtra(getString(R.string.selected_image));
        boolean hasBitmap = intent.hasExtra(getString(R.string.selected_bitmap));
        boolean hasReturn = intent.hasExtra(getString(R.string.return_fragment));

        if(hasReturn) {
            if(hasImage) {
                fireBaseMethods.uploadImage(getString(R.string.new_profile_photo), null, 0, intent.getStringExtra(getString(R.string.selected_image)), null);
            }
            if(hasBitmap) {
                fireBaseMethods.uploadImage(getString(R.string.new_profile_photo), null, 0, null, (Bitmap)intent.getParcelableExtra(getString(R.string.selected_bitmap)));
            }
        }

        if(intent.hasExtra(getString(R.string.calling_activity))) {
            setUpViewPager(0);
        }
    }
    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(AccountSettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /**
     * Display fragment in list on click
     */
    private void setUpListListener() {
        ListView lv = (ListView) findViewById(R.id.accountSettingsList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("In AccountSettings", "onItemClick: position is " + position);
                if(position == 1) {
                    signOut();
                }
                else {
                    setUpViewPager(position);
                }
            }
        });
    }

    /**
     * Establish list elements and link to adapter
     */
    private void setUpList() {
        ListView lv = (ListView)findViewById(R.id.accountSettingsList);
        ArrayList<String> settingsContent = new ArrayList<>();
        settingsContent.add(getString(R.string.edit_profile));
        settingsContent.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, settingsContent);
        lv.setAdapter(adapter);
    }

    /**
     * Set up the bottom navigation with the appropriate item selected
     */
    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(PROFILE_ACTIVITY);
        item.setCheckable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
