package com.jordanleex13.hackprinceton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jordanleex13.hackprinceton.Helpers.FragmentHelper;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if (toolBar != null) {
            toolBar.setTitle("Main");
            toolBar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolBar);
        }

        FragmentMain newFragment = FragmentMain.newInstance();
        FragmentHelper.swapFragments(getSupportFragmentManager(), R.id.activity_main_container, newFragment,
                true, false, null, FragmentMain.TAG);
    }
}
