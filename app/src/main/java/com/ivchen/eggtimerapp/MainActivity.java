package com.ivchen.eggtimerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextview;
    Button controllerButton;
    Boolean timerActive = false;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);
        timerTextview = (TextView) findViewById(R.id.timerTextView);
        controllerButton = (Button) findViewById(R.id.controllerButton);

        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    public void updateTimer(int secondsLeft){
        // Casted to an integer so it would round down to the number of minutes
        // number of seconds left over after minutes
        // taken total number of seconds and subtracted number of seconds already included
        int minutes = (int) secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);

        if(secondString == "0"){
            secondString = "00";
        }else if(seconds <= 9){
            secondString = "0" + secondString;
        }

        timerTextview = (TextView) findViewById(R.id.timerTextView);
        timerTextview.setText(Integer.toString(minutes) + ":" + secondString);
    }

    public void resetSettings(){
        timerTextview.setText("0:30");
        timerSeekBar.setProgress(30);
        countDownTimer.cancel();
        controllerButton.setText("Go!");
        timerSeekBar.setEnabled(true);
        timerActive = false;
    }

    public void controlTimer(View view) {
        Log.i("Button was pressed: ", "Yup, its true");

        // ONLY want everything to happen if the timerActive is initially false
        if(timerActive == false) {
            // Timer active is started
            // so stop the seekbar from being changed
            timerActive = true;
            timerSeekBar.setEnabled(false);
            controllerButton.setText("Stop!");
            // 1/10th of a second later so added 100
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Allows to be used in update timer method
                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    //timerTextview.setText("0:00");
                    Log.i("Timer:", "TIMER DINGGG");

                    resetSettings();
                    //Error: inside countdown timer - cant use "this" to refer to
                    // so use getapplicationcontext
                    MediaPlayer timerRing = MediaPlayer.create(getApplicationContext(), R.raw.person_cheering);
                    timerRing.start();
                }
            }.start();
        } else {
            resetSettings();

        }
    }
}
