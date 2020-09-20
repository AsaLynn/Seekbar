package com.zxn.samlpe.bubbleseekbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxn.samlpe.bubbleseekbar.R;
import com.zxn.seekbar.CursorSeekBar;
import com.zxn.seekbar.api.ISeekBar;
import com.zxn.seekbar.listener.CursorChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaFragment extends Fragment {


    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.csb_seek)
    CursorSeekBar csbSeek;
    private int mLeftMark;
    private int mRightMark;

    public AreaFragment() {
    }

    public static AreaFragment newInstance() {
        AreaFragment fragment = new AreaFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_fourth, container, false);
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        csbSeek.setCursorChangeListener(new CursorChangeListener() {
            @Override
            public void onLeftCursorChanged(ISeekBar cursorSeekBar, int progressMark) {
                mLeftMark = progressMark;
                tvCount.setText(mLeftMark + "至" + mRightMark);
            }

            @Override
            public void onRightCursorChanged(ISeekBar cursorSeekBar, int progressMark) {
                mRightMark = progressMark;
                tvCount.setText(mLeftMark + "至" + mRightMark);
            }
        });
    }
}