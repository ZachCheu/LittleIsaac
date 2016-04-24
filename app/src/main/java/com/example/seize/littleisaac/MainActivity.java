package com.example.seize.littleisaac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by ZachCheu on 4/17/16.
 */

public class MainActivity extends Activity{
    private ImageButton play_btn;
    private SurfaceHolder _surfaceHolder;
    private TextView t, a;
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView)  findViewById(R.id.titleText);
        System.out.println("Successful Switch");
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Lock");
        //this.wakeLock.acquire();

        play_btn = (ImageButton) findViewById(R.id.playButton);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("TEST");
                    Intent k = new Intent(MainActivity.this, loader_activity.class);
                    startActivity(k);
                    //overridePendingTransition(R.anim.animation2, R.anim.animation);
                } catch (Exception e) {
                    //Error occured with starting single player game variant!
                    e.printStackTrace();
                }
            }
        });
    }
}