package com.zxn.samlpe.bubbleseekbar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxn on 2020/9/18.
 */
public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_seekbar0,R.id.btn_seekbar1, R.id.btn_seekbar2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_seekbar0:
                SeekBarActivity.jumpTo(this);
                break;
            case R.id.btn_seekbar1:
                CursorSeekbarActivity.jumpTo(this);
                break;
            case R.id.btn_seekbar2:
                MainActivity.jumpTo(this);
                break;
            /*case R.id.btn_seekbar2:
                SeekBarDemoActivity.jumpTo(this);
                break;*/
        }
    }
}