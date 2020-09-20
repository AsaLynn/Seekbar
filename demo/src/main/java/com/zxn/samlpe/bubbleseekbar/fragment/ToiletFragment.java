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
import com.zxn.seekbar.listener.RightCursorChangListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zxn on 2020/9/20.
 */
public class ToiletFragment extends Fragment {


    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.csb_seek)
    CursorSeekBar csbSeek;

    public ToiletFragment() {
    }


    public static ToiletFragment newInstance() {
        ToiletFragment fragment = new ToiletFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toilet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        csbSeek.setRightCursorChangListener((cursorSeekBar, progressMark) -> tvCount.setText(String.valueOf(progressMark)));

    }
}