package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mut_jaeryo.circletimer.CircleTimer;

public class TimerActivity extends AppCompatActivity {
    //计时器：
    private CircleTimer circleTimer;
    private Button startBtn,stopBtn,restartBtn,btn;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main2);
        findViews();

//        btn.setVisibility(View.GONE);

        intent = getIntent();
        String timeStr = intent.getStringExtra("time");
//        circleTimer.setInitPosition(60);
        int time = Integer.parseInt(timeStr);
        circleTimer.setMaximumTime(3600);
        circleTimer.setInitPosition(time*60);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleTimer.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleTimer.stop();
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleTimer.reset();
            }
        });

        circleTimer.setBaseTimerEndedListener(new CircleTimer.baseTimerEndedListener() {
            @Override
            public void OnEnded() {
            }
        });
    }

    private void findViews() {
        //计时器：
        circleTimer = findViewById(R.id.circle_timer);
        startBtn = findViewById(R.id.timer_start);
        stopBtn = findViewById(R.id.timer_stop);
        restartBtn = findViewById(R.id.timer_reset);
    }
}