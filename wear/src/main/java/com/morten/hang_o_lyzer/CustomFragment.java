package com.morten.hang_o_lyzer;

import android.app.Activity;
import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Morten on 03-05-2016.
 */
public class CustomFragment extends Fragment {
    public int imgid;
    public String desc;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.round_drink, container, false);
        ImageView mImageView = (ImageView) v.findViewById(R.id.imageView);
        mImageView.setImageResource(imgid);
        TextView mTextView = (TextView) v.findViewById(R.id.textViewDescription);
        mTextView.setText(desc);
        return v;
    }
}
