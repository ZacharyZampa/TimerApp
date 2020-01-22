package com.zampaze.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    TextView textBox;
    ProgressBar pBar;
    SeekBar sBar;
    Button starter;
    CountDownTimer countDownTimer;
    boolean isCounterActive = false;


    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String secondString = Integer.toString(seconds);

        if (seconds <= 9) {
            secondString = "0" + secondString;
        }

        textBox.setText(minutes + ":" + secondString);
    }

    public void stopClicked(View view) {
        textBox.setText("00:00");
        sBar.setProgress(0);
        sBar.setEnabled(true);
        countDownTimer.cancel();
        starter.setText("Start");
        isCounterActive = false;
    }

    public void runCountdown(final int timeMilli) {
        countDownTimer
                = new CountDownTimer(timeMilli + 100, 100) {
            public void onTick(long millisecondsUntilDone) {
                // this is called every interval
                updateTimer((int) millisecondsUntilDone / 1000);
                pBar.setProgress((int) (millisecondsUntilDone / 1000));
            }
            public void onFinish() {
                // this is called when the countdown timer is completely finished
                textBox.setText("DONE");
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.sos);
                mediaPlayer.start();
            }
        }.start();
    }

    public void resetTimer(int orgTime) {
        countDownTimer.cancel();
        runCountdown(sBar.getProgress() * 1000);
        sBar.setEnabled(false);
    }


    public void buttonClicked(View view) {
        // get time from user
        final int timeMilli = sBar.getProgress() * 1000;

        pBar.setMax((int) timeMilli / 1000);
        pBar.setProgress((int) timeMilli / 1000);

        if (isCounterActive) {
            sBar.setEnabled(true);
            resetTimer(timeMilli);
        } else {
            // disable seekbar
            sBar.setEnabled(false);
            // change start button text to reset
            starter.setText("Reset");
            // set counter flag to true
            isCounterActive = true;

            runCountdown(timeMilli);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // grab UI elements
        textBox = (TextView) findViewById(R.id.textView);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        sBar = (SeekBar) findViewById(R.id.seekBar);
        starter = (Button) findViewById(R.id.startButton);

        sBar.setMax(600);
        sBar.setProgress(0);

        // set up seek bar detection
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
