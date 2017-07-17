package com.example.sheldon.instagramclone.Home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.SectionPagerAdapter;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final int HOME_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();
        setContentView(R.layout.activity_home);
        setUpBottomNav();
        setUpViewAdapter();
    }

    public void initImageLoader() {
        UniversalImageLoader imgLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(imgLoader.getConfig());
    }
    private void setUpBottomNav() {
        BottomNavigationViewEx botNavView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_view_bar);
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(this, botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(HOME_ACTIVITY);
        item.setCheckable(true);
    }

    private void setUpViewAdapter() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessagesFragment());
        ViewPager viewpager = (ViewPager) findViewById(R.id.container);
        viewpager.setAdapter(adapter);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_instagram);
        tablayout.getTabAt(2).setIcon(R.drawable.ic_messages);
    }
}
