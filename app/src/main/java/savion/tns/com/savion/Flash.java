package savion.tns.com.savion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;

public class Flash extends AppCompatActivity {
    CircularMusicProgressBar progressBar;
    private final int interval = 1000; // 1 Second
    int progress=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        progressBar = findViewById(R.id.album_art);

        CountDownTimer countDownTimer = new CountDownTimer(4 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                progress=progress+25;
                progressBar.setValue(progress);
            }
            public void onFinish() {
                progressBar.setValue(100);

                SharedPreferences sharedpreferences = getSharedPreferences("mobile", Context.MODE_PRIVATE);
                if (sharedpreferences.contains("mobile")) {
                    startActivity(new Intent(Flash.this,MainActivity1.class));
                    finish();
                }else {
                    startActivity(new Intent(Flash.this,MobileVerification.class));
                    finish();
                }

            }
        };
        countDownTimer.start();
        // get user update
        progressBar.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {

            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

                updateRandomly();
            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {

            }

        });

    }

    private void updateRandomly() {
        Random random = new Random();
        final float percent = random.nextFloat() * 100;
        progressBar.setValue(percent);
    }

}
