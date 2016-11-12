package com.jordanleex13.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jordanleex13.hackprinceton.Helpers.RunnableParseJson;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.launchMapButton);
        mButton.setOnClickListener(this);

        new Thread(new RunnableParseJson("HellO")).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launchMapButton:
                Intent intent = new Intent(this, RecognizeActivity.class);
                startActivity(intent);

                break;
        }
    }
}
