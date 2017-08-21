package com.example.sheldon.instagramclone.Util;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.example.sheldon.instagramclone.Home.HomeActivity;
import com.example.sheldon.instagramclone.Login.RegisterActivity;
import com.example.sheldon.instagramclone.Profile.AccountSettingsActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.models.Photo;
import com.example.sheldon.instagramclone.models.User;
import com.example.sheldon.instagramclone.models.UserAccountSettings;
import com.example.sheldon.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/19/2017.
 * Firebase utility functions
 */

public class FireBaseMethods {
    private static final String FIREBASE_IMAGE_STORAGE = "photos/users";
    private static final int IMAGE_QUALITY = 100;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private String userID;
    private FirebaseDatabase mFireBaseDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference ref;
    private double mProgress;


    public FireBaseMethods(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mFireBaseDatabase.getReference();
        mProgress = 0;
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

    public int getImageCount(DataSnapshot dataSnapshot) {
        int count = 0;
        for(DataSnapshot ds : dataSnapshot.child(mContext.getString(R.string.db_user_photos)).child(mAuth.getInstance().getCurrentUser().getUid()).getChildren()) {
            count++;
        }
        return count;
    }

    public String getTimeStamp() {
        Long timeStamp = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }

    public String getTags(String caption) {
        if(caption.indexOf("#") == -1) {
            return "";
        }

        boolean hashFound = false;
        StringBuilder sb = new StringBuilder();
        char[] captionChars = caption.toCharArray();
        for(char c : captionChars) {
            if(c == '#') {
                sb.append(c);
                hashFound = true;
            }
            else if(!(c == '\n') && !(c == ' ') && hashFound){
                sb.append(c);
            }
            else {
                hashFound = false;
            }
        }

        String tags = sb.toString().replace("#", ",#");
        return tags;

    }
    public void addPhotoToDb(String caption, String firebaseURL) {
        String photoKey = ref.child(mContext.getString(R.string.db_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(firebaseURL);
        photo.setPhoto_id(photoKey);
        photo.setTags(getTags(caption));
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.child(mContext.getString(R.string.db_photos)).child(photoKey).setValue(photo);
        ref.child(mContext.getString(R.string.db_user_photos)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(photoKey).setValue(photo);
        Log.d(TAG, "addPhotoToDb: added photos to database");
    }
    public void uploadImage(String photoType, final String caption, int count, String imgURL, Bitmap bm) {
        if(photoType.equals(mContext.getString(R.string.new_photo))) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = mStorageRef.child(FIREBASE_IMAGE_STORAGE + "/" + uid + "/photo" + + (count + 1));
            if(bm == null) {
                bm = ImageManager.getBitMap(imgURL);
            }
            byte[] bytes = ImageManager.getBytesFromBitMap(bm, IMAGE_QUALITY);

            UploadTask uploadTask = null;
            uploadTask = storageRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseUri = taskSnapshot.getDownloadUrl();
                    addPhotoToDb(caption, firebaseUri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    if(progress - 15 > mProgress) {
                        Toast.makeText(mContext, "Uploading photo " + progress + "%", Toast.LENGTH_SHORT).show();
                        mProgress = progress;
                    }
                }
            });
        }
        else if(photoType.equals(mContext.getString(R.string.new_profile_photo))) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = mStorageRef.child(FIREBASE_IMAGE_STORAGE + "/" + uid + "/profile_photo");
            if(bm == null) {
                bm = ImageManager.getBitMap(imgURL);
            }
            byte[] bytes = ImageManager.getBytesFromBitMap(bm, IMAGE_QUALITY);

            UploadTask uploadTask = null;
            uploadTask = storageRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseUri = taskSnapshot.getDownloadUrl();
                    addProfilePhoto(firebaseUri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    if(progress - 15 > mProgress) {
                        Toast.makeText(mContext, "Uploading photo " + progress + "%", Toast.LENGTH_SHORT).show();
                        mProgress = progress;
                    }
                }
            });
            ((AccountSettingsActivity)mContext).setUpViewPager(((AccountSettingsActivity) mContext).adapter.getFragmentNumber(mContext.getString(R.string.edit_profile_fragment)));
        }
    }
    public void addProfilePhoto(String imgURL) {
        ref.child(mContext.getString(R.string.db_account_settings)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mContext.getString(R.string.db_profile_photo)).setValue(imgURL);
    }
    public String getUserID() {
        return userID;
    }
}
