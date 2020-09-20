package com.zxn.samlpe.bubbleseekbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.zxn.samlpe.bubbleseekbar.entity.TabEntity;
import com.zxn.samlpe.bubbleseekbar.fragment.AreaFragment;
import com.zxn.samlpe.bubbleseekbar.fragment.HouseFragment;
import com.zxn.samlpe.bubbleseekbar.fragment.PeopelFragment;
import com.zxn.samlpe.bubbleseekbar.fragment.SeekBarFragment;
import com.zxn.samlpe.bubbleseekbar.fragment.ToiletFragment;
import com.zxn.tablayout.CommonTabLayout;
import com.zxn.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Updated by zxn on 2020/9/19.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_menu)
    CommonTabLayout ctlMenu;
    private String[] tabArray = {"数量", "人数", "厕数", "房数", "面积", /*"滑动",*/ /*"其他"*/};

    public static void jumpTo(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        List<CustomTabEntity> tabEntitys = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabArray.length; i++) {
            tabEntitys.add(new TabEntity(tabArray[i]));
        }
        fragments.add(SeekBarFragment.newInstance());
        fragments.add(PeopelFragment.newInstance());
        fragments.add(ToiletFragment.newInstance());
        fragments.add(HouseFragment.newInstance());
        fragments.add(AreaFragment.newInstance());
        //fragments.add(MainFragment.newInstance());
        ctlMenu.setTabData(tabEntitys, this, R.id.container, fragments);
    }


    @OnClick(R.id.fab)
    public void onViewClicked() {
        SeekBarDemoActivity.jumpTo(this);
    }
}
