package com.example.sheldon.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
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

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.SectionStateAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by sheldon on 7/8/2017.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final int PROFILE_ACTIVITY = 2;
    private Context mContext;
    private SectionStateAdapter adapter;
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
    }

    private void setUpAdapter() {
        adapter = new SectionStateAdapter(getSupportFragmentManager());
        adapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));
        adapter.addFragment(new SignOutFragment(), getString(R.string.sign_out));
    }

    private void setUpViewPager(int index) {
        mLayout.setVisibility(View.GONE);
        ViewPager vp = (ViewPager) findViewById(R.id.container);
        vp.setAdapter(adapter);
        vp.setCurrentItem(index);

    }

    private void setUpListListener() {
        ListView lv = (ListView) findViewById(R.id.accountSettingsList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUpViewPager(position);
            }
        });
    }

    private void setUpList() {
        ListView lv = (ListView)findViewById(R.id.accountSettingsList);
        ArrayList<String> settingsContent = new ArrayList<>();
        settingsContent.add(getString(R.string.edit_profile));
        settingsContent.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, settingsContent);
        lv.setAdapter(adapter);
    }

    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(PROFILE_ACTIVITY);
        item.setCheckable(true);
    }
}
