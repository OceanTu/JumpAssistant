package com.ocean.ja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ocean.jumpassistant.R;

/**
 * Created by ocean on 2018/1/6.
 */

public class MainActivity extends Activity {

    private SeekBar speedBar;
    private TextView sppedText;
    private float speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button start = (Button) findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast
                startService(new Intent(MainActivity.this, Service.class));
            }
        });
        //stopService (from my original code)
        Button stop = (Button) findViewById(R.id.stopBtn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, Service.class));
                Process.killProcess(Process.myPid());
            }
        });
        sppedText = (TextView) findViewById(R.id.spped_text);
        speedBar = (SeekBar) findViewById(R.id.spped);
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float half = speedBar.getMax() / 2.0f;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= half) {
                    speed = (progress - half) / half * 4 + 1;
                } else {
                    speed = 0.1f + (progress) / half * 0.9f;
                }
                sppedText.setText(String.format("速度 x%.2f (默认1，不同机器不一样)", speed));
                Intent i = new Intent("CHANGE_SPEED");
                i.putExtra("speed", speed);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        speedBar.setProgress(speedBar.getMax() / 2);
    }
}
