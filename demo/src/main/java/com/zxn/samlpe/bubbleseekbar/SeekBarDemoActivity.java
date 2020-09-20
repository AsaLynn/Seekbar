package com.zxn.samlpe.bubbleseekbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxn.seekbar.RangeSeekbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by zxn on 2020/9/18.
 */
public class SeekBarDemoActivity extends AppCompatActivity {

    @BindView(R.id.rt_yxqy)
    RelativeLayout rtYxqy;
    @BindView(R.id.rt_yxlp)
    RelativeLayout rtYxlp;
    @BindView(R.id.rt_khly)
    RelativeLayout rtKhly;
    @BindView(R.id.iv_qgdj)
    ImageView ivQgdj;
    @BindView(R.id.checkbox_mj)
    CheckBox checkboxMj;
    @BindView(R.id.seekbar_mj)
    RangeSeekbar seekbarMj;
    @BindView(R.id.seekbar_fs)
    RangeSeekbar seekbarFs;
    @BindView(R.id.checkbox_fs)
    CheckBox checkboxFs;
    @BindView(R.id.seekbar_cs)
    RangeSeekbar seekbarCs;
    @BindView(R.id.checkbox_cs)
    CheckBox checkboxCs;
    @BindView(R.id.checkbox_lc)
    CheckBox checkboxLc;
    @BindView(R.id.lt_lc)
    LinearLayout ltLc;
    @BindView(R.id.seekbar_sq)
    RangeSeekbar seekbarSq;
    @BindView(R.id.seekbar_zj)
    RangeSeekbar seekbarZj;
    @BindView(R.id.et_minfloor)
    EditText etMinfloor;
    @BindView(R.id.et_maxfloor)
    EditText etMaxfloor;
    List<String> zyyy = new ArrayList<>();
    List<String> tbyq = new ArrayList<>();


    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, SeekBarDemoActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_regist);
        ButterKnife.bind(this);
    }

}