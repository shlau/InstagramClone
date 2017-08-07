package com.example.sheldon.instagramclone.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheldon.instagramclone.Home.HomeActivity;
import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.FireBaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by sheldon on 7/17/2017.
 * User registration
 */

public class RegisterActivity extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";
    private EditText mEmail, mPassword, mFullName;
    private Context mContext;
    private Button regButton;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private TextView waitText;
    private String email;
    private String password;
    private String fullname;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FireBaseMethods fireBaseMethods;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference ref;
    private String append = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidgets();
        setUpFireBaseAuth();
        registerUser();
    }

    private void checkUsernameExists(final String username) {
        ref = mFireBaseDatabase.getReference();
        Query query = ref.child(getString(R.string.db_users)).orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  Log.d(TAG, "Data Changed:");

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.exists()) {
                        append = ref.push().getKey().substring(3,10);
                    }
                }
                fullname += append;
                fireBaseMethods.addNewUser(email, "", fullname, "", "");
                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * Initialize the widgets being used
     */
    private void initWidgets() {
        mEmail = (EditText) findViewById(R.id.register_emailAddress);
        mPassword = (EditText) findViewById(R.id.register_password);
        mFullName = (EditText) findViewById(R.id.register_name);
        regButton = (Button) findViewById(R.id.regBtn);
        mProgressBar = (ProgressBar) findViewById(R.id.registerProgress);
        waitText = (TextView) findViewById(R.id.registerWaitText);
        mProgressBar.setVisibility(View.GONE);
        waitText.setVisibility(View.GONE);
        mContext = RegisterActivity.this;
        fireBaseMethods = new FireBaseMethods(mContext);
    }

    /**
     * Check if a string is empty
     * @param input the string being checked
     * @return whether the string is empty
     */
    private boolean isNull(String input) {
        return input.equals("");
    }


    /**
     * Attempt to register user
     */
    private void registerUser() {
        regButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                fullname = mFullName.getText().toString();
                mProgressBar.setVisibility(View.VISIBLE);
                waitText.setVisibility(View.VISIBLE);
                if(isNull(email) || isNull(password) || isNull(fullname)) {
                    Toast.makeText(RegisterActivity.this, R.string.auth_empty,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String username = fullname.replace(" ", ".");
                    fireBaseMethods.registerUser(email, password, fullname);
                }
                mProgressBar.setVisibility(View.GONE);
                waitText.setVisibility(View.GONE);
            }
        });
    }
    // firebase
    private void setUpFireBaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                ref = mFireBaseDatabase.getReference();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in: waiting for listener" + user.getUid());

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "Data Changed:");
                            if(fireBaseMethods.userNameExists(fullname, dataSnapshot)) {
                                append = ref.push().getKey().substring(3,10);
                            }
                            fullname += append;

                            fireBaseMethods.addNewUser(email, "", fullname, "", "");
                            Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };

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
