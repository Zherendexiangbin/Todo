package net.onest.time.components;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.mut_jaeryo.circletimer.CircleTimer;

import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.api.TomatoClockApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.navigation.activity.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

public class StopClockDialog extends AlertDialog {
    private TextView abandon;
    private TextView advance;
    private TextView cancel;
    private TaskVo taskVo;

    private CountDownTimer countDownTimer;
    private CircleTimer circleTimer;

//    public StopClockDialog(@NonNull Context context) {
//        super(context);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.timer_activity_stop_pop,null);
//
//        abandon = view.findViewById(R.id.abandon_btn);
//        advance = view.findViewById(R.id.advance_btn);
//        cancel = view.findViewById(R.id.cancel_btn);
//
//        setListeners();
//
//        show();
//        getWindow().setContentView(view);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//    }

    public StopClockDialog(@NonNull Context context,TaskVo taskVo,CircleTimer circleTimer){
        super(context);
        this.taskVo = taskVo;
        this.circleTimer = circleTimer;
        View view = LayoutInflater.from(context).inflate(R.layout.timer_activity_stop_pop,null);

        abandon = view.findViewById(R.id.abandon_btn);
        advance = view.findViewById(R.id.advance_btn);
        cancel = view.findViewById(R.id.cancel_btn);

        setListeners();

        show();
        getWindow().setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public StopClockDialog(@NonNull Context context, TaskVo taskVo){
        super(context);
        this.taskVo = taskVo;
        View view = LayoutInflater.from(context).inflate(R.layout.timer_activity_stop_pop,null);

        abandon = view.findViewById(R.id.abandon_btn);
        advance = view.findViewById(R.id.advance_btn);
        cancel = view.findViewById(R.id.cancel_btn);

        setListeners();

        show();
        getWindow().setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setListeners() {
        //放弃当前计时
        abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopClockDialog.this.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(getContext());
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
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                float x = dm.widthPixels / 2;
                // y轴像素坐标，获取文本高度（dp）+上方间隔12dp 转换为像素
                Paint paint = new Paint();
                paint.setTextSize(18f);
                Rect rect = new Rect();
                paint.getTextBounds(descriptionStr, 0, descriptionStr.length(), rect);
                float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        rect.height() + 10, getContext().getResources().getDisplayMetrics());
                // 设置饼状图的位置
                description.setPosition(x, y);
                description.setTextAlign(Paint.Align.LEFT);

                //设置图例:
                Legend legend = abandonReasonChart.getLegend();
                legend.setWordWrapEnabled(true);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//设置图例的排列走向:vertacal相当于分行
                legend.setForm(Legend.LegendForm.SQUARE);//设置图例的图形样式,默认为圆形
                legend.setFormSize(12f);//设置图例的大小
                legend.setTextSize(12f);//设置图注的字体大小
                legend.setXEntrySpace(15f);//设置图例之间的间隔！
                legend.setTextColor(getContext().getResources().getColor(R.color.black)); //图例的文字颜色


                //设置数据源
                List<PieEntry> yVals = new ArrayList<>();
                List<Integer> colors = new ArrayList<>();
                //设置饼状图数据：
                yVals.add(new PieEntry(0.286f, "吃饭"));
                yVals.add(new PieEntry(0.603f, "睡觉"));
                yVals.add(new PieEntry(1f-0.286f-0.603f, "洗澡"));

                colors.add(Color.parseColor("#4A92FC"));
                colors.add(Color.parseColor("#ee6e55"));
                colors.add(Color.parseColor("#adff2f"));
                setPieChartData(abandonReasonChart,yVals,colors);


                abandonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取放弃原因!
                        String reason = abandonReason.getText().toString().trim();

                        if(abandonReason.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "请输入打断的原因", Toast.LENGTH_SHORT).show();

                        }else{
                            try {
                                TomatoClockApi.stopTomatoClock(taskVo.getTaskId(),reason);
                            } catch (RuntimeException e) {
                                Toast.makeText(getContext(),"番茄钟放弃失败", Toast.LENGTH_SHORT).show();
                            }
                            dialogAban.dismiss();
                            Intent intent2 = new Intent();
                            intent2.setClass(getContext(), NavigationActivity.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                            getContext().startActivity(intent2);

//                            overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
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
                //提前完成之正向计时:

                //提前完成之倒计时:
                circleTimer.setValue(0);
            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopClockDialog.this.dismiss();
            }
        });
    }

    //打断原因
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

}
