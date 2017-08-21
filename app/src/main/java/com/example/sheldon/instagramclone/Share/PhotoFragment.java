package com.example.sheldon.instagramclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sheldon.instagramclone.Profile.AccountSettingsActivity;
import com.example.sheldon.instagramclone.R;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/5/2017.
 */

public class PhotoFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button cameraBtn = (Button) view.findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
        return view;
    }
    private boolean isRootTask() {
        return ((ShareActivity)getActivity()).getTask() == 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            Log.d(TAG, "onActivityResult: capturing image");

            if(isRootTask()) {
                try {
                    Bitmap bm = (Bitmap)data.getExtras().get("data");
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bm);
                    startActivity(intent);
            }
                catch(NullPointerException e) {
                    Log.d(TAG, "onActivityResult: NullPOinterExecption " + e.getMessage());
                }
            }
            else {
                try {
                    Bitmap bm = (Bitmap)data.getExtras().get("data");
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bm);
                    intent.putExtra(getString(R.string.return_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }
                catch(NullPointerException e) {
                    Log.d(TAG, "onActivityResult: NullPOinterExecption " + e.getMessage());
                }
            }
        }
    }
}
