package com.example.sheldon.instagramclone.Profile;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.BottomNavHelper;
import com.example.sheldon.instagramclone.Util.FireBaseMethods;
import com.example.sheldon.instagramclone.Util.GridImageAdapter;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.example.sheldon.instagramclone.models.Photo;
import com.example.sheldon.instagramclone.models.UserAccountSettings;
import com.example.sheldon.instagramclone.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/24/2017.
 */

public class ProfileFragment extends Fragment{
    private static final int PROFILE_ACTIVITY = 4;
    private static final int NUM_COLUMNS = 4;

    private Context mContext;
    private ImageView mProfileMenu;
    private TextView mPosts;
    private TextView mFollowers;
    private TextView mFollowing;
    private TextView mDisplayName;
    private BottomNavigationViewEx botNavView;
    private Toolbar mToolBar;
    private ImageView mProfilePhoto;
    private TextView mDescription;
    private TextView mWebsite;
    private TextView mUsername;
    private TextView mEditProfile;
    private ProgressBar mProgressBar;
    private GridView mGridView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FireBaseMethods fireBaseMethods;

    private ArrayList<String> photos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initWidgets(view);
        setUpBottomNav();
        setUpToolBar();
        setUpFireBaseAuth();
        setUpGridView();
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        return view;
    }

    private void setUpGridView() {
        photos = new ArrayList<>();
        myRef.child(getString(R.string.db_user_photos)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String imgURL = ds.getValue(Photo.class).getImage_path();
                    photos.add(imgURL);
                    Log.d(TAG, "onDataChange: Adding images to profile gridview " + imgURL);

                }
                GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, "", photos);
                mGridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initWidgets(View view) {
        mContext = getActivity();
        mProfileMenu = (ImageView) view.findViewById(R.id.settingsIcon);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        botNavView = (BottomNavigationViewEx) view.findViewById(R.id.bottom_nav_view_bar);
        mToolBar = (Toolbar) view.findViewById(R.id.profileToolbar);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_image);
        mDescription = (TextView) view.findViewById(R.id.description);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mUsername = (TextView) view.findViewById(R.id.username);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        fireBaseMethods = new FireBaseMethods(mContext);
        mEditProfile = (TextView) view.findViewById(R.id.textEditProfile);

        int screenSize = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = screenSize / NUM_COLUMNS;
        mGridView.setColumnWidth(imageWidth);
    }
    private void setUpBottomNav() {
        BottomNavHelper.disableAnimation(botNavView);
        BottomNavHelper.enableNavBar(mContext, getActivity(), botNavView);
        Menu menu = botNavView.getMenu();
        MenuItem item = menu.getItem(PROFILE_ACTIVITY);
        item.setChecked(true);

    }

    private void setUpToolBar() {
        ProfileActivity profActivity = (ProfileActivity)getActivity();
        profActivity.setSupportActionBar(mToolBar);

        mProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpProfileWidgets(UserSettings settings) {
        UserAccountSettings accountSettings = settings.getSettings();

        UniversalImageLoader.setImage(accountSettings.getProfile_photo(), mProfilePhoto, null, "");
        mPosts.setText(Long.toString(accountSettings.getPosts()));
        mFollowers.setText(Long.toString(accountSettings.getFollowers()));
        mFollowing.setText(Long.toString(accountSettings.getFollowing()));
        mDisplayName.setText(accountSettings.getDisplay_name());
        mDescription.setText(accountSettings.getDescription());
        mWebsite.setText(accountSettings.getWebsite());
        mUsername.setText(accountSettings.getUsername());
        mProgressBar.setVisibility(View.GONE);

    }
    // firebase
    private void setUpFireBaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserSettings settings = fireBaseMethods.getUserSettings(dataSnapshot);
                setUpProfileWidgets(settings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
