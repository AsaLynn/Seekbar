package com.zxn.samlpe.bubbleseekbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxn.seekbar.RangeSeekbar;

import java.util.Locale;


/**
 * Created by zxn on 2020/9/12.
 */
public class DemoFragment5 extends Fragment {

    private Activity mActivity;
    private RangeSeekbar mSeekBar;

    public static DemoFragment5 newInstance() {
        return new DemoFragment5();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_demo_5, container, false);


        final TextView progressText1 = view.findViewById(R.id.demo_4_progress_text_1);
        final TextView progressText2 = view.findViewById(R.id.demo_4_progress_text_2);
        final TextView progressText3 = view.findViewById(R.id.demo_4_progress_text_3);


        mSeekBar = (RangeSeekbar) view.findViewById(R.id.seekbar);
        mSeekBar.setOnCursorChangeListener(new RangeSeekbar.OnCursorChangeListener() {
            @Override
            public void onLeftCursorChanged(int location, String textMark) {

            }

            @Override
            public void onRightCursorChanged(int location, String textMark) {

            }
        });
        mSeekBar.setLeftSelection(2);
        mSeekBar.setRightSelection(4);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
    }
}
