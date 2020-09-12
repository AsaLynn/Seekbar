package com.zxn.samlpe.bubbleseekbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.main_tab_btn_1).setOnClickListener(this);
        findViewById(R.id.main_tab_btn_2).setOnClickListener(this);
        findViewById(R.id.main_tab_btn_3).setOnClickListener(this);
        findViewById(R.id.main_tab_btn_4).setOnClickListener(this);
        findViewById(R.id.main_tab_btn_5).setOnClickListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, DemoFragment5.newInstance(), "demo1");
            ft.commit();
            mTag = "demo1";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab_btn_1:
                switchContent("demo1");
                break;
            case R.id.main_tab_btn_2:
                switchContent("demo2");
                break;
            case R.id.main_tab_btn_3:
                switchContent("demo3");
                break;
            case R.id.main_tab_btn_4:
                switchContent("demo4");
                break;
            case R.id.main_tab_btn_5:
                switchContent("demo5");
                break;
        }
    }

    public void switchContent(String toTag) {
        if (mTag.equals(toTag))
            return;

        FragmentManager fm = getSupportFragmentManager();
        Fragment from = fm.findFragmentByTag(mTag);
        Fragment to = fm.findFragmentByTag(toTag);

        FragmentTransaction ft = fm.beginTransaction();
        if (to == null) {
            to = DemoFragment5.newInstance();
        }
        if (!to.isAdded()) {
            ft.hide(from).add(R.id.container, to, toTag);
        } else {
            ft.hide(from).show(to);
        }
        ft.commit();

        mTag = toTag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
