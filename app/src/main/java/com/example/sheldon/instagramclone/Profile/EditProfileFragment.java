package com.example.sheldon.instagramclone.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by sheldon on 7/5/2017.
 */

public class EditProfileFragment extends Fragment {

    private ImageView profileImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        profileImage = (ImageView)view.findViewById(R.id.editProfileImage);
;
        initImageLoader();
        setProfilePicture();
        return view;
    }

    public void initImageLoader() {
        UniversalImageLoader imgLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(imgLoader.getConfig());
    }
    public void setProfilePicture() {
        UniversalImageLoader.setImage("http://www.freeiconspng.com/uploads/profile-icon-28.png", profileImage, null, "");

    }
}
