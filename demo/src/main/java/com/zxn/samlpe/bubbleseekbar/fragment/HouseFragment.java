package com.zxn.samlpe.bubbleseekbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zxn.samlpe.bubbleseekbar.R;
import com.zxn.seekbar.CursorSeekBar;
import com.zxn.seekbar.api.ISeekBar;
import com.zxn.seekbar.listener.RightCursorChangListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zxn on 2020/9/20.
 */
public class HouseFragment extends Fragment {

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.csb_seek)
    CursorSeekBar csbSeek;

    public HouseFragment() {
    }

    public static HouseFragment newInstance() {
        HouseFragment fragment = new HouseFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house, container, false);
        ButterKnife.bind(this, view);
        csbSeek.setRightCursorChangListener((cursorSeekBar, progressMark) -> tvCount.setText(String.valueOf(progressMark)));
        return view;
    }
}