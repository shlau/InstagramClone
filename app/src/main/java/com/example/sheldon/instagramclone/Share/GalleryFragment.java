package com.example.sheldon.instagramclone.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sheldon.instagramclone.R;
import com.example.sheldon.instagramclone.Util.GridImageAdapter;
import com.example.sheldon.instagramclone.Util.ImageFinder;
import com.example.sheldon.instagramclone.Util.UniversalImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 7/5/2017.
 */

public class GalleryFragment extends Fragment {

    private static final int NUM_COLUMNS = 3;
    private ImageView mExit;
    private Spinner mSpinner;
    private TextView mNext;
    private ProgressBar mProgressBar;
    private List<String> directories;
    private GridView mGridView;
    private ImageView mGalleryImage;
    private HashMap<String, ArrayList<String>> directoryToImage;
    private String append = "file:/";
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mExit = (ImageView) view.findViewById(R.id.exitShare);
        mSpinner = (Spinner) view.findViewById(R.id.shareSpinner);
        mNext = (TextView) view.findViewById(R.id.shareNext);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mGalleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
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
        init();

        return view;
    }

    private void init() {
        ImageFinder imageFinder = new ImageFinder();
        imageFinder.getImages(getActivity());
        directoryToImage = imageFinder.getImageMapping();
        directories = new ArrayList<>(directoryToImage.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, directories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + directories.get(position));
                setUpGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpGridView(String directory) {
        final ArrayList<String> imgURLS = directoryToImage.get(directory);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_COLUMNS;
        Log.d(TAG, "setUpGridView: Image Width is " + imageWidth);
        mGridView.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, append, imgURLS);
        mGridView.setAdapter(adapter);
        UniversalImageLoader.setImage(imgURLS.get(0),mGalleryImage, mProgressBar, append);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UniversalImageLoader.setImage(imgURLS.get(position), mGalleryImage, mProgressBar, append);
            }
        });
    }
}
