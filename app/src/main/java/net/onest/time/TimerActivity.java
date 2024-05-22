package net.onest.time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private Button startBtn,stopOuterBtn,restartBtn,alterBtn,stopInnerBtn;
    private LinearLayout draLin;
    private Intent intent;
    private TextView text,taskName;

    private OkHttpClient httpClient;//可以复用，定义全局
    private Request request;
    private Response response;
    private Call call;
    private Gson gson = new Gson();

    private RelativeLayout timerEntire;

    /** 获取屏幕坐标点 **/
    Point startPoint;// 起始点
    Point endPoint;// 终点
    /** 记录按下的坐标点（起始点）**/
    private float mPosX = 0;
    private float mPosY = 0;
    /** 记录移动后抬起坐标点（终点）**/
    private float mCurPosX = 0;
    private float mCurPosY = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        timerEntire.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 60
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            //向下滑動
                            Toast.makeText(TimerActivity.this, "向下滑动😊", Toast.LENGTH_SHORT).show();
                        } else if (mCurPosY - mPosY < -60
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            //向上滑动
                            Toast.makeText(TimerActivity.this, "向上滑动😊", Toast.LENGTH_SHORT).show();
                        }else if (mCurPosX - mPosX > 60
                                && (Math.abs(mCurPosX - mPosX) > 60)) {
                            //向上滑动
                            Toast.makeText(TimerActivity.this, "向右滑动😊", Toast.LENGTH_SHORT).show();
                        }else if (mCurPosX - mPosX < -60
                                && (Math.abs(mCurPosX - mPosX) > 60)) {
                            //向上滑动
                            Toast.makeText(TimerActivity.this, "向左滑动😊", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return true;
            }
        });


        intent = getIntent();
        taskName.setText(intent.getStringExtra("name"));

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

            stopOuterBtn.setOnClickListener(new View.OnClickListener() {
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

            //外部打断:
            stopOuterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TimerActivity.this, "外部打断", Toast.LENGTH_SHORT).show();
                    stopTimer();
                }
            });

            //内部打断:
            stopInnerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TimerActivity.this, "内部打断", Toast.LENGTH_SHORT).show();
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
                            text.setText("“ "+result+" ”");
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
        stopOuterBtn = findViewById(R.id.timer_outer_stop);
        stopInnerBtn = findViewById(R.id.timer_inner_btn);
        restartBtn = findViewById(R.id.timer_reset);
        draLin = findViewById(R.id.timer_background_lin);
        timeTxt = findViewById(R.id.timer_forward);

        alterBtn = findViewById(R.id.alter_btn);
        text = findViewById(R.id.timer_text);
        taskName = findViewById(R.id.timer_name);

        timerEntire = findViewById(R.id.timer_entirely);
    }
}