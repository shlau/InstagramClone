package com.example.sheldon.instagramclone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sheldon.instagramclone.Login.LoginActivity;
import com.example.sheldon.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/5/2017.
 * Signs out the user and navigates to login page
 */

public class SignOutFragment extends Fragment {
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        Log.d(TAG, "onCreateView: Inside sign out fragment");
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivity(intent);
//        getActivity().finish();
        return view;
    }
}
