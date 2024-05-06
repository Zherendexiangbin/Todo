package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mut_jaeryo.circletimer.CircleTimer;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    //倒计时器：
    private CircleTimer circleTimer;
    //正向计时：
    private TextView timeTxt;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = 0; // 初始计时时间为0秒

    private Button startBtn,stopBtn,restartBtn,btn;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_timer);
        findViews();

//        btn.setVisibility(View.GONE);

        intent = getIntent();

        if("countDown".equals(intent.getStringExtra("method"))){
            timeTxt.setVisibility(View.GONE);
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
        }else {
            //正向计时：
            circleTimer.setVisibility(View.GONE);
            timeTxt.setText("开始");

//            startBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startTimer();
//                }
//            });
            startTimer();

            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopTimer();
                }
            });

            restartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restartTimer();
                }
            });


        }
    }

    private void restartTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mTimeLeftInMillis = 0;
            updateCountdownText();
        }
    }

    private void stopTimer() {
        mCountDownTimer.cancel();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis +=1000;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                // 不需要处理倒计时结束的事件
            }
        }.start();
    }


    private void updateCountdownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeTxt.setText(timeLeftFormatted);
    }

    private void findViews() {
        //计时器：
        circleTimer = findViewById(R.id.circle_timer);
        startBtn = findViewById(R.id.timer_start);
        stopBtn = findViewById(R.id.timer_stop);
        restartBtn = findViewById(R.id.timer_reset);

        timeTxt = findViewById(R.id.timer_forward);

        btn = findViewById(R.id.btn);
    }
}