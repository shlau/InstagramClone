package com.example.sheldon.instagramclone.Dialog;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheldon.instagramclone.R;
import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 8/6/2017.
 */




public class DialogConfirmFragment extends DialogFragment {
    private OnConfirmPasswordListener listener;
    private TextView password;
    private TextView confirm;
    private TextView cancel;
    public interface OnConfirmPasswordListener {
        public void onConfirmPassword(String password);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_confirm, container, false);
        confirm = (TextView) view.findViewById(R.id.dialogConfirm);
        cancel = (TextView) view.findViewById(R.id.dialogCancel);
        password = (TextView) view.findViewById(R.id.dialogPassword);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String passwordStr = password.getText().toString();
                if(!passwordStr.equals("")) {
                    Log.d(TAG, "onClick: Checking password");
                    listener.onConfirmPassword(passwordStr);
                    getDialog().dismiss();
                }
                else {
                    Log.d(TAG, "onClick: Empty Password");
                    Toast.makeText(getActivity(), "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnConfirmPasswordListener) getTargetFragment();
        }
        catch(ClassCastException e) {
            Log.e(TAG, "onAttach: " + e.getMessage() );
        }
    }
}
