package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.mut_jaeryo.circletimer.CircleTimer;

import net.onest.time.entity.dayword.QuoteData;
import net.onest.time.utils.DrawableUtil;
import net.onest.time.utils.ResultUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TimerActivity extends AppCompatActivity {
    //倒计时器：
    private CircleTimer circleTimer;
    //正向计时：
    private TextView timeTxt;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = 0; // 初始计时时间为0秒

    private Button startBtn,stopBtn,restartBtn,alterBtn;
    private LinearLayout draLin;
    private Intent intent;
    private TextView text;

    private OkHttpClient httpClient;//可以复用，定义全局
    private Request request;
    private Response response;
    private Call call;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_timer);
        findViews();
        draLin.setBackground(DrawableUtil.randomDrawableBack(getApplicationContext()));

//        getOneWord();
        getOneWordTwo();
//        btn.setVisibility(View.GONE);
        alterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });


        intent = getIntent();

        if("countDown".equals(intent.getStringExtra("method"))){
            timeTxt.setVisibility(View.GONE);
            String timeStr = intent.getStringExtra("time");
//        circleTimer.setInitPosition(60);
            int time = Integer.parseInt(timeStr);
            circleTimer.setMaximumTime(time*60);
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

    //计时器----一句话:https://luckycola.com.cn/tools/yiyan[每日一句]
    private void getOneWord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("fromat","").build();
                request = new Request.Builder().url("https://tenapi.cn/v2/yiyan")
                        .post(formBody)
                        .build();
                httpClient = new OkHttpClient();
                call = httpClient.newCall(request);

                try {
                    response = call.execute();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String result = response.body().string();
                                text.setText(result);
//                                ResultUtil resultUtil = gson.fromJson(result, ResultUtil.class);
//                                String sResult = gson.toJson(resultUtil.getData());
//                                QuoteData quoteData = gson.fromJson(sResult, QuoteData.class);
//                                text.setText(quoteData.getNote()+"");
                                Log.e("pot同步请求", "postSync:"+result);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    //一言——快一点：
    private void getOneWordTwo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpClient = new OkHttpClient();
                request = new Request.Builder().url("https://uapis.cn/api/say").build();
                call = httpClient.newCall(request);

                try {
                    response = call.execute();
                    String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(result);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();
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
        draLin = findViewById(R.id.timer_background_lin);
        timeTxt = findViewById(R.id.timer_forward);

        alterBtn = findViewById(R.id.alter_btn);
        text = findViewById(R.id.timer_text);
    }


}