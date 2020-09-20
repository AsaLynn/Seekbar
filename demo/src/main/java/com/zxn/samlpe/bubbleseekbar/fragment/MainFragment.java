package com.zxn.samlpe.bubbleseekbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxn.samlpe.bubbleseekbar.R;
import com.zxn.seekbar.RangeSeekbar;


/**
 * Created by zxn on 2020/9/12.
 */
public class MainFragment extends Fragment {

    private RangeSeekbar mSeekBar;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
//        mSeekBar = (RangeSeekbar) view.findViewById(R.id.seekbar);
//        mSeekBar.setOnCursorChangeListener(new RangeSeekbar.OnCursorChangeListener() {
//            @Override
//            public void onLeftCursorChanged(int location, String textMark) {
//
//            }
//
//            @Override
//            public void onRightCursorChanged(int location, String textMark) {
//
//            }
//        });
//        mSeekBar.setLeftSelection(2);
//        mSeekBar.setRightSelection(4);

        view.findViewById(R.id.set_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setLeftSelection(0);
            }
        });

        view.findViewById(R.id.set_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setRightSelection(5);
            }
        });

        return view;
    }
}
