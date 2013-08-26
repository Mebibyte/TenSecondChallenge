package com.drinkthisshow.tenseconds;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends Activity {

    private Random rand = new Random();
    private ArrayList<Integer> nums  = new ArrayList<Integer>();
    private int curr = 1;
    private TextView target;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        p.setProgress(100);

        target = (TextView) findViewById(R.id.targetText);

        for (int i = 1; i < 50; i++) {
            nums.add(i);
        }
        for (int i = 0; i < 49; i++) {
            int num = rand.nextInt(nums.size());
            ((Button) findViewById(R.id.button1+i)).setText(""+nums.get(num));
            nums.remove(num);
        }

        if (savedInstanceState == null) {
            final Handler h = new Handler();
            final Context c = this;
            Runnable r = new Runnable() {
                long m_startTime = System.currentTimeMillis();
                long m_endTime = m_startTime + 10000;

                @Override
                public void run() {
                    if (System.currentTimeMillis() < m_endTime) {
                        h.postDelayed(this, 100);
                        p.setProgress(p.getProgress() - 1);
                    } else {
                        if (curr < 50) Toast.makeText(c, "Times up! You reached " + (curr - 1) + " !", Toast.LENGTH_LONG).show();
                        else Toast.makeText(c, "Times up! You won!", Toast.LENGTH_LONG).show();
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        timerRunning = false;
                        findViewById(R.id.restartButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.restartButton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                }
            };
            h.post(r);
            timerRunning = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void checkButton(View v) {
        Button clicked = (Button) v;
        if (timerRunning && (curr + "").equals(clicked.getText())) {
            curr++;
            target.setText("Current Target: " + curr);
            clicked.setText("");
            clicked.getBackground().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN);
        }
    }
}
