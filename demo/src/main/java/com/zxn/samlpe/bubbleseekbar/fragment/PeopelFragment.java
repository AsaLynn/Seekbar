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

import butterknife.BindView;
import butterknife.ButterKnife;


public class PeopelFragment extends Fragment {


    @BindView(R.id.tv_house)
    TextView tvHouse;
    @BindView(R.id.tv_people)
    TextView tvPeople;
    @BindView(R.id.csb_people)
    CursorSeekBar csbPeople;

    public PeopelFragment() {
    }


    public static PeopelFragment newInstance() {
        PeopelFragment fragment = new PeopelFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_cursor_seekbar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        csbPeople.setRightCursorChangListener((location, textMark) -> {
            tvPeople.setText(textMark+"");
        });

    }
}