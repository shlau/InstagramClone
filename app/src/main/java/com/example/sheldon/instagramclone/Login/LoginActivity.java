package com.example.sheldon.instagramclone.Login;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sheldon on 7/17/2017.
 * User authentication
 */

public class LoginActivity extends AppCompatActivity{
    private ProgressBar mProgressBar;
    private TextView mWaitText;
    private EditText mEmail, mPassword;
    private TextView mLinkText;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidgets();
        setUpFireBaseAuth();
        loginUser();
        mLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initialize widgets being used
     */
    private void initWidgets() {
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = (ProgressBar)findViewById(R.id.loginProgress);
        mWaitText = (TextView) findViewById(R.id.loginWaitText);
        mLinkText = (TextView) findViewById(R.id.registerLink);
        mEmail = (EditText) findViewById(R.id.input_emailAddress);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        mProgressBar.setVisibility(View.GONE);
        mWaitText.setVisibility(View.GONE);
    }

    /**
     * Authenticate user on button click
     */
    private void loginUser() {
        Button loginButton = (Button)findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
    }

    /**
     * Determine whether a string is empty
     * @param input the string to check
     * @return whether the string is empty
     */
    private boolean isNull(String input) {
        return input.equals("");
    }

    /**
     * Authenticate the user and navigate to home activity if successful
     */
    private void loginCheck() {
        mProgressBar.setVisibility(View.VISIBLE);
        mWaitText.setVisibility(View.VISIBLE);
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(isNull(email) || isNull(password)) {
            Toast.makeText(LoginActivity.this, R.string.auth_empty,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = mAuth.getCurrentUser();
                                try {
                                    if(user.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        mAuth.signOut();
                                    }
                                } catch (NullPointerException e) {
                                    Toast.makeText(LoginActivity.this, "Exceuption at login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                Log.w(TAG, "signInWithEmail:succeeded", task.getException());
                                Toast.makeText(LoginActivity.this, R.string.auth_sucess,
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
        mProgressBar.setVisibility(View.GONE);
        mWaitText.setVisibility(View.GONE);
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
