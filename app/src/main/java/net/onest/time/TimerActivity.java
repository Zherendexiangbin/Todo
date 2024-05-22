package net.onest.time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.AAChartModel.AAChartCore.AAChartCreator.AAChartModel;
import com.github.AAChartModel.AAChartCore.AAChartCreator.AAChartView;
import com.github.AAChartModel.AAChartCore.AAChartCreator.AASeriesElement;
import com.github.AAChartModel.AAChartCore.AAChartEnum.AAChartType;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mut_jaeryo.circletimer.CircleTimer;


import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.utils.DrawableUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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



    @SuppressLint("ClickableViewAccessibility")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                            LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                            View dialogView = inflater.inflate(R.layout.timer_activity_stop,null);
                            final Dialog dialog = builder.create();
                            dialog.show();
                            dialog.getWindow().setContentView(dialogView);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView abandon = dialogView.findViewById(R.id.abandon_btn);
                            TextView advance = dialogView.findViewById(R.id.advance_btn);
                            TextView cancel = dialogView.findViewById(R.id.cancel_btn);

                            //放弃当前计时
                            abandon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                                    LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                                    View dialogViewAban = inflater.inflate(R.layout.timer_activity_abandon,null);
                                    final Dialog dialogAban = builder.create();
                                    dialogAban.show();
                                    dialogAban.getWindow().setContentView(dialogViewAban);
                                    dialogAban.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    TextView abandonYes = dialogViewAban.findViewById(R.id.timer_activity_abandon_yes);
                                    Button abandonNo = dialogViewAban.findViewById(R.id.timer_activity_abandon_no);
                                    TextInputEditText abandonReason = dialogViewAban.findViewById(R.id.abandon_reason);
                                    PieChart abandonReasonChart = dialogAban.findViewById(R.id.abandon_reason_pie_chart);


                                    String descriptionStr = "本月打断原因分析";
                                    Description description = new Description();
                                    description.setText(descriptionStr);
                                    description.setTextColor(Color.BLACK);
                                    description.setTextSize(15f);
                                    abandonReasonChart.setDescription(description);

                                    // 获取屏幕中间x 轴的像素坐标
                                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                    DisplayMetrics dm = new DisplayMetrics();
                                    wm.getDefaultDisplay().getMetrics(dm);
                                    float x = dm.widthPixels / 2;
                                    // y轴像素坐标，获取文本高度（dp）+上方间隔12dp 转换为像素
                                    Paint paint = new Paint();
                                    paint.setTextSize(18f);
                                    Rect rect = new Rect();
                                    paint.getTextBounds(descriptionStr, 0, descriptionStr.length(), rect);
                                    float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                            rect.height() + 12, getResources().getDisplayMetrics());
                                    // 设置饼状图的位置
                                    description.setPosition(x, y);
                                    description.setTextAlign(Paint.Align.RIGHT);

                                    //设置数据源
                                    List<PieEntry> yVals = new ArrayList<>();
                                    List<Integer> colors = new ArrayList<>();
                                    //设置饼状图数据：
                                    yVals.add(new PieEntry(28.6f, "陆地"));
                                    yVals.add(new PieEntry(60.3f, "海洋"));
                                    yVals.add(new PieEntry(100f-28.6f-60.3f, "天空"));

                                    colors.add(Color.parseColor("#4A92FC"));
                                    colors.add(Color.parseColor("#ee6e55"));
                                    colors.add(Color.parseColor("#adff2f"));
                                    setPieChartData(abandonReasonChart,yVals,colors);

                                    //获取放弃原因!
                                    String reason = abandonReason.getText().toString().trim();

                                    abandonYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(abandonReason.getText().toString().isEmpty()){
                                                Toast.makeText(TimerActivity.this, "请输入打断的原因", Toast.LENGTH_SHORT).show();

                                            }else{
                                                dialogAban.dismiss();
                                            }
                                        }
                                    });

                                    abandonNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogAban.dismiss();
                                        }
                                    });


                                }
                            });

                            //提前完成计时
                            advance.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            //取消
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });


                            Toast.makeText(TimerActivity.this, "向右滑动😊", Toast.LENGTH_SHORT).show();


                        }else if (mCurPosX - mPosX < -60
                                && (Math.abs(mCurPosX - mPosX) > 60)) {
                            Toast.makeText(TimerActivity.this, "向左滑动😊", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return true;
            }
        });


        intent = getIntent();
        taskName.setText(intent.getStringExtra("name"));
        String timeStr = intent.getStringExtra("time");
        if("countDown".equals(intent.getStringExtra("method"))){
            timeTxt.setVisibility(View.GONE);
            
//        circleTimer.setInitPosition(60);
            int time = Integer.parseInt(timeStr);
            circleTimer.setMaximumTime(time*60);
            circleTimer.setInitPosition(time*60);
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    circleTimer.start();
//                    long taskId = intent.getLongExtra("taskId",0L);
//                    if(taskId!=0L){
//                        TomatoClockApi.startTomatoClock(taskId);
//                    }

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
                    Toast.makeText(TimerActivity.this, "现在的时间是"+circleTimer.getValue(), Toast.LENGTH_SHORT).show();
//                    circleTimer.reset();
                }
            });

            circleTimer.setBaseTimerEndedListener(new CircleTimer.baseTimerEndedListener() {
                @Override
                public void OnEnded() {
                }
            });

            //内部打断:
            stopInnerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int time = Integer.parseInt(timeStr);
                    if(time*60 - circleTimer.getValue()<5){
                        Toast toast = Toast.makeText(TimerActivity.this, "不记录5秒以下的专注记录!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                        Intent intent2 = new Intent();
                        intent2.setClass(TimerActivity.this, NavigationActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
//                        finish();
//                        NavController navController = Navigation.findNavController(TimerActivity.this, R.id.nav_host_fragments);
//                        navController.navigate(R.id.action_todo_fragment_to_list_fragment);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                        LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                        View dialogView = inflater.inflate(R.layout.timer_activity_stop,null);
                        final Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setContentView(dialogView);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView abandon = dialogView.findViewById(R.id.abandon_btn);
                        TextView advance = dialogView.findViewById(R.id.advance_btn);
                        TextView cancel = dialogView.findViewById(R.id.cancel_btn);

                        //放弃当前计时
                        abandon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                                LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                                View dialogViewAban = inflater.inflate(R.layout.timer_activity_abandon,null);
                                final Dialog dialogAban = builder.create();
                                dialogAban.show();
                                dialogAban.getWindow().setContentView(dialogViewAban);
                                dialogAban.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView abandonYes = dialogViewAban.findViewById(R.id.timer_activity_abandon_yes);
                                Button abandonNo = dialogViewAban.findViewById(R.id.timer_activity_abandon_no);
                                TextInputEditText abandonReason = dialogViewAban.findViewById(R.id.abandon_reason);
                                PieChart abandonReasonChart = dialogAban.findViewById(R.id.abandon_reason_pie_chart);


                                String descriptionStr = "本月打断原因分析";
                                Description description = new Description();
                                description.setText(descriptionStr);
                                description.setTextColor(Color.BLACK);
                                description.setTextSize(15f);
                                abandonReasonChart.setDescription(description);

                                // 获取屏幕中间x 轴的像素坐标
                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                DisplayMetrics dm = new DisplayMetrics();
                                wm.getDefaultDisplay().getMetrics(dm);
                                float x = dm.widthPixels / 2;
                                // y轴像素坐标，获取文本高度（dp）+上方间隔12dp 转换为像素
                                Paint paint = new Paint();
                                paint.setTextSize(18f);
                                Rect rect = new Rect();
                                paint.getTextBounds(descriptionStr, 0, descriptionStr.length(), rect);
                                float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                        rect.height() + 10, getResources().getDisplayMetrics());
                                // 设置饼状图的位置
                                description.setPosition(x, y);
                                description.setTextAlign(Paint.Align.LEFT);

                                //设置图例:
                                Legend legend = abandonReasonChart.getLegend();
                                legend.setEnabled(false);
//                            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//                            legend.setFormSize(12f);
//                            legend.setFormToTextSpace(10f);//设置图形与文本之间的间隔
//                            legend.setXEntrySpace(10f);//设置X轴上条目的间隔
//                            legend.setMaxSizePercent(100);


                                //设置数据源
                                List<PieEntry> yVals = new ArrayList<>();
                                List<Integer> colors = new ArrayList<>();
                                //设置饼状图数据：
                                yVals.add(new PieEntry(28.6f, "陆地"));
                                yVals.add(new PieEntry(60.3f, "海洋"));
                                yVals.add(new PieEntry(100f-28.6f-60.3f, "天空"));

                                colors.add(Color.parseColor("#4A92FC"));
                                colors.add(Color.parseColor("#ee6e55"));
                                colors.add(Color.parseColor("#adff2f"));
                                setPieChartData(abandonReasonChart,yVals,colors);

                                //获取放弃原因!
                                String reason = abandonReason.getText().toString().trim();

                                abandonYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(abandonReason.getText().toString().isEmpty()){
                                            Toast.makeText(TimerActivity.this, "请输入打断的原因", Toast.LENGTH_SHORT).show();

                                        }else{
                                            dialogAban.dismiss();
                                        }
                                    }
                                });

                                abandonNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogAban.dismiss();
                                    }
                                });


                            }
                        });

                        //提前完成计时
                        advance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        //取消
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }


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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                    LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                    View dialogView = inflater.inflate(R.layout.timer_activity_stop,null);
                    final Dialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setContentView(dialogView);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView abandon = dialogView.findViewById(R.id.abandon_btn);
                    TextView advance = dialogView.findViewById(R.id.advance_btn);
                    TextView cancel = dialogView.findViewById(R.id.cancel_btn);

                    //放弃当前计时
                    abandon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this,R.style.CustomDialogStyle);
                            LayoutInflater inflater = LayoutInflater.from(TimerActivity.this);
                            View dialogViewAban = inflater.inflate(R.layout.timer_activity_abandon,null);
                            final Dialog dialogAban = builder.create();
                            dialogAban.show();
                            dialogAban.getWindow().setContentView(dialogViewAban);
                            dialogAban.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView abandonYes = dialogViewAban.findViewById(R.id.timer_activity_abandon_yes);
                            Button abandonNo = dialogViewAban.findViewById(R.id.timer_activity_abandon_no);
                            TextInputEditText abandonReason = dialogViewAban.findViewById(R.id.abandon_reason);
                            PieChart abandonReasonChart = dialogAban.findViewById(R.id.abandon_reason_pie_chart);

                            //获取放弃原因!
                            String reason = abandonReason.getText().toString().trim();

                            abandonYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAban.dismiss();
                                }
                            });

                            abandonNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAban.dismiss();
                                }
                            });


                        }
                    });

                    //提前完成计时
                    advance.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    //取消
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
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

    private void setPieChartData(PieChart abandonReasonChart, List<PieEntry> yVals, List<Integer> colors) {
        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        abandonReasonChart.setEntryLabelColor(Color.RED);//描述文字的颜色
        pieDataSet.setValueTextSize(15);//数字大小
        pieDataSet.setValueTextColor(Color.BLACK);//数字颜色

        //设置描述的位置
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.6f);//设置描述连接线长度
        //设置数据的位置
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart2Length(0.6f);//设置数据连接线长度
        //设置两根连接线的颜色
        pieDataSet.setValueLineColor(Color.BLUE);

        abandonReasonChart.setData(pieData);
        abandonReasonChart.setExtraOffsets(0f,32f,0f,32f);
        //动画（如果使用了动画可以则省去更新数据的那一步）
//        pieChart.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        pieChart.animateX(1000); //X轴动画
        abandonReasonChart.animateXY(1000,1000);//XY两轴混合动画
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