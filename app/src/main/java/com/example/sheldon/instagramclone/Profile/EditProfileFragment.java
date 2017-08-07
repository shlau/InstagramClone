package com.example.sheldon.instagramclone.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheldon.instagramclone.Dialog.DialogConfirmFragment;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.FireBaseMethods;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.example.sheldon.instagramclone.models.User;
import com.example.sheldon.instagramclone.models.UserAccountSettings;
import com.example.sheldon.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


/**
 * Created by sheldon on 7/5/2017.
 */

public class EditProfileFragment extends Fragment implements DialogConfirmFragment.OnConfirmPasswordListener {

    private ImageView profileImage;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FireBaseMethods fireBaseMethods;

    private TextView mUsername;
    private TextView mDisplayName;
    private TextView mWebsite;
    private TextView mDescription;
    private TextView mEmail;
    private TextView mPhoneNumber;
    private CircleImageView mProfilePhoto;

    private UserSettings uSettings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        profileImage = (ImageView)view.findViewById(R.id.editProfileImage);
        ImageView backButton = (ImageView) view.findViewById(R.id.backArrow);
        initWidgets(view);
        setUpFireBaseAuth();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Testing", "onClick: returning to profile activity");
                getActivity().finish();
            }
        });
        ImageView check = (ImageView) view.findViewById(R.id.checkMark);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });
//        setProfilePicture();
        return view;
    }


    //    public void setProfilePicture() {
//        UniversalImageLoader.setImage("http://www.freeiconspng.com/uploads/profile-icon-28.png", profileImage, null, "");
//
//    }

    private void checkUsernameExists(final String username) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child(getString(R.string.db_users)).orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    fireBaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "Updating username", Toast.LENGTH_SHORT).show();
                }
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.exists()) {
                        Toast.makeText(getActivity(), "This username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void saveProfileSettings() {
        final String displayName = mDisplayName.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());
        final String description = mDescription.getText().toString();
        final String website = mWebsite.getText().toString();
        final String userName = mUsername.getText().toString();
        final String userID = mAuth.getCurrentUser().getUid();


        if(!uSettings.getUser().getUsername().equals(userName)) {
            checkUsernameExists(userName);
        }

        if(!uSettings.getUser().getEmail().equals(email)) {
            DialogConfirmFragment dialog = new DialogConfirmFragment();
            FragmentManager manager = getFragmentManager();
            String tag = getString(R.string.confirm_dialog_fragment);
            dialog.show(manager, tag);
            dialog.setTargetFragment(EditProfileFragment.this, 1);

        }

        if(!uSettings.getSettings().getDisplay_name().equals(displayName)) {
            fireBaseMethods.updateUserAccountSettings(displayName, null, null);
        }
        if(uSettings.getUser().getPhone_number() != phoneNumber) {
            fireBaseMethods.updateUserSettings(phoneNumber);
        }
        if(!uSettings.getSettings().getDescription().equals(description)) {
            fireBaseMethods.updateUserAccountSettings(null, description, null);
        }
        if(!uSettings.getSettings().getWebsite().equals(website)) {
            fireBaseMethods.updateUserAccountSettings(null, null, website);
        }
    }
    private void initWidgets(View view) {

        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mEmail = (TextView) view.findViewById(R.id.email);
        mPhoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
        mDescription = (TextView) view.findViewById(R.id.description);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mUsername = (TextView) view.findViewById(R.id.username);
        fireBaseMethods = new FireBaseMethods(getActivity());
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.editProfileImage);

    }

    private void setUpProfileWidgets(UserSettings settings) {
        UserAccountSettings accountSettings = settings.getSettings();
        User user = settings.getUser();
        uSettings = settings;
        UniversalImageLoader.setImage(accountSettings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(accountSettings.getDisplay_name());
        mDescription.setText(accountSettings.getDescription());
        mWebsite.setText(accountSettings.getWebsite());
        mUsername.setText(accountSettings.getUsername());
        mEmail.setText(user.getEmail());
        mPhoneNumber.setText(Long.toString(user.getPhone_number()));

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

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: acquired password" + password + " for email " + uSettings.getUser().getEmail());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(uSettings.getUser().getEmail(), password);

       // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if(task.isSuccessful()) {
                                        try {
                                            if(task.getResult().getProviders().size() == 1) {
                                                Toast.makeText(getActivity(), "Email already in use", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    fireBaseMethods.updateEmail(mEmail.getText().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                        } catch( NullPointerException e) {
                                            Log.e(TAG, "onComplete: " + e.getMessage() );
                                        }
                                    }
                                    else {
                                        Log.d(TAG, "onComplete: unable to fetch providers");
                                    }
                                }
                            });

                        }
                        else {
                            Log.d(TAG, "onComplete: re-autehntication failed");
                            Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


