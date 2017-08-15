package com.example.sheldon.instagramclone.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/5/2017.
 */

public class GalleryFragment extends Fragment {

    private ImageView mExit;
    private Spinner mSpinner;
    private TextView mNext;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mExit = (ImageView) view.findViewById(R.id.exitShare);
        mSpinner = (Spinner) view.findViewById(R.id.shareSpinner);
        mNext = (TextView) view.findViewById(R.id.shareNext);
        
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Navigating to next step in sharing photo");
            }
        });
        return view;
    }
}
