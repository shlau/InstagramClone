package com.example.sheldon.instagramclone.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.sheldon.instagramclone.Home.HomeActivity;
import com.example.sheldon.instagramclone.Login.RegisterActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.models.User;
import com.example.sheldon.instagramclone.models.UserAccountSettings;
import com.example.sheldon.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/19/2017.
 * Firebase utility functions
 */

public class FireBaseMethods {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private String userID;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference ref;

    public FireBaseMethods(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        ref = mFireBaseDatabase.getReference();
        if(mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Determine if username is already being used
     * @param username the requested username
     * @param dataSnapshot the database
     * @return whether or not the username is being used
     */
    public boolean userNameExists(String username, DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
            String uName = ds.getValue(User.class).getUsername().replace("."," ");
            if(uName.equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Register the user into firebase
     * @param email the user email
     * @param password the user password
     * @param username the user's full name
     */
    public void registerUser(final String email, final String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }

                        else {
                            Toast.makeText(mContext, R.string.auth_sucess,
                                    Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: userID is " + userID);
                            sendEmailVerification();
                            mAuth.signOut();
                        }

                        // ...
                    }
                });
    }

    public void sendEmailVerification() {
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser != null) {
            mUser.sendEmailVerification();
        }
    }
    /**
     * A
     * @param email
     * @param description
     * @param username
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String description, String username, String website, String profile_photo) {
        Log.d(TAG, "addNewUser: In this method ");
        User user = new User(email, username, userID, 1);
        ref.child(mContext.getString(R.string.db_users)).child(userID).setValue(user);

        UserAccountSettings settings = new UserAccountSettings(description,username,0,0,0,profile_photo, username, website);
        ref.child(mContext.getString(R.string.db_account_settings)).child(userID).setValue(settings);
    }

    public void updateUsername(String username) {
        Log.d(TAG, "Updating username to " + username);
        ref.child(mContext.getString(R.string.db_users)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);
        ref.child(mContext.getString(R.string.db_account_settings)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);
    }

    public void updateEmail(String email) {
        Log.d(TAG, "Updating email to " + email);
        ref.child(mContext.getString(R.string.db_users)).child(userID).child("email").setValue(email);
    }

    public void updateUserAccountSettings(String displayName, String description, String website ) {
        if(displayName != null) {
            ref.child(mContext.getString(R.string.db_account_settings)).child(userID).child(mContext.getString(R.string.field_display_name)).setValue(displayName);
        }
        if(description != null) {
            ref.child(mContext.getString(R.string.db_account_settings)).child(userID).child(mContext.getString(R.string.field_description)).setValue(description);
        }
        if(website != null) {
            ref.child(mContext.getString(R.string.db_account_settings)).child(userID).child(mContext.getString(R.string.field_website)).setValue(website);
        }
    }

    public void updateUserSettings(long phoneNumber) {
        if(phoneNumber != 0) {
            ref.child(mContext.getString(R.string.db_users)).child(userID).child(mContext.getString(R.string.field_phonenumber)).setValue(phoneNumber);
        }
    }
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        UserAccountSettings accountSettings = new UserAccountSettings();
        User user = new User();

        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            if(ds.getKey().equals(mContext.getString(R.string.db_account_settings))) {
                accountSettings.setDescription(ds.child(userID).getValue(UserAccountSettings.class).getDescription());
                accountSettings.setDisplay_name(ds.child(userID).getValue(UserAccountSettings.class).getDisplay_name());
                accountSettings.setFollowers(ds.child(userID).getValue(UserAccountSettings.class).getFollowers());
                accountSettings.setFollowing(ds.child(userID).getValue(UserAccountSettings.class).getFollowing());
                accountSettings.setPosts(ds.child(userID).getValue(UserAccountSettings.class).getPosts());
                accountSettings.setProfile_photo(ds.child(userID).getValue(UserAccountSettings.class).getProfile_photo());
                accountSettings.setUsername(ds.child(userID).getValue(UserAccountSettings.class).getUsername());
                accountSettings.setWebsite(ds.child(userID).getValue(UserAccountSettings.class).getWebsite());
            }

            if(ds.getKey().equals(mContext.getString(R.string.db_users))) {
                user.setUsername(ds.child(userID).getValue(User.class).getUsername());
                user.setEmail(ds.child(userID).getValue(User.class).getEmail());
                user.setPhone_number(ds.child(userID).getValue(User.class).getPhone_number());
                user.setUser_id(ds.child(userID).getValue(User.class).getUser_id());
            }


        }
        UserSettings settings = new UserSettings(user, accountSettings);
        return settings;
    }

    public String getUserID() {
        return userID;
    }
}
