package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CountDownTimer countDownTimer =null;
    SeekBar seekBar;
    boolean isRunning = false;
    MediaPlayer mediaPlayer;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Let us set up the slider
        seekBar = (SeekBar) findViewById(R.id.timerController);
        button = (Button) findViewById(R.id.timerButton);
        final TextView timerView = (TextView) findViewById(R.id.timerText);
        seekBar.setMax(600); // setting the total value to be 600, 10 minutes
        seekBar.setProgress(30); //default 30 seconds

        //setting the audio
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mediaPlayer = MediaPlayer.create(this, R.raw.horn);

        //setup the timer
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isRunning) {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                    }
                    button.setText("Go!!!");
                    isRunning=false;
                }
                timerView.setText(getTimeString(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void onTimerButtonClicked(View view) {
        final TextView timerView = (TextView) findViewById(R.id.timerText);

        if (isRunning) {
            if (countDownTimer!=null) {
                countDownTimer.cancel();
            }
            button.setText("Go!!!");
            isRunning=false;
            seekBar.setProgress(30);
            timerView.setText("00:30");

        } else {
            //setup the countdown
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerView.setText(getTimeString((int)millisUntilFinished/1000));
                }

                @Override
                public void onFinish() {
                    mediaPlayer.start();
                }
            }.start();
            button.setText("Stop");
            isRunning=true;
        }
    }

    public String getTimeString(int timeInSeconds) {
        String left =  "" + timeInSeconds / 60;
        if (left.length() < 2) {
            left = "0" + left;
        }

        String right = "" + timeInSeconds % 60;
        if (right.length() < 2) {
            right = "0" + right;
        }

        return left + ":" + right;
    }
}
