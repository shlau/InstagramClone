package com.example.sheldon.instagramclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheldon.instagramclone.Home.HomeActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.FireBaseMethods;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.example.sheldon.instagramclone.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 8/15/2017.
 */

public class NextActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FireBaseMethods fireBaseMethods;

    private ImageView mExit;
    private TextView mSharText;
    private ImageView mShareImage;
    private EditText mDescription;

    private int mNumImages;
    private String mImagePath;

    private String append = "file:/";

    private boolean hasImage;
    private boolean hasBitmap;
    private Bitmap bm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        fireBaseMethods = new FireBaseMethods(NextActivity.this);
        mExit = (ImageView) findViewById(R.id.exitNext);
        mSharText = (TextView) findViewById(R.id.shareText);
        mShareImage = (ImageView) findViewById(R.id.shareImage);
        mDescription = (EditText) findViewById(R.id.shareDescription);

        setUpFireBaseAuth();
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setImage();
        mSharText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload image to firebase
                String caption = mDescription.getText().toString();
                if(hasImage) {
                    fireBaseMethods.uploadImage(getString(R.string.new_photo), caption, mNumImages, mImagePath, null);
                }
                else if(hasBitmap) {
                    fireBaseMethods.uploadImage(getString(R.string.new_photo), caption, mNumImages, null, bm);
                }
                Intent intent = new Intent(NextActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setImage() {
        Intent intent = getIntent();
        hasImage = intent.hasExtra(getString(R.string.selected_image));
        hasBitmap = intent.hasExtra(getString(R.string.selected_bitmap));
        if(hasImage) {
            mImagePath = intent.getStringExtra(getString(R.string.selected_image));
            UniversalImageLoader.setImage(mImagePath, mShareImage, null, append);
        }
        else if(hasBitmap) {
            bm = intent.getParcelableExtra(getString(R.string.selected_bitmap));
            mShareImage.setImageBitmap(bm);
        }
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
                mNumImages = fireBaseMethods.getImageCount(dataSnapshot);
                Log.d(TAG, "onDataChange: This user has " + mNumImages + " images.");
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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
