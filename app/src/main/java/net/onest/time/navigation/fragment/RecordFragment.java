package net.onest.time.navigation.fragment;

import static android.content.ContentValues.TAG;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import net.onest.time.MainActivity;
import net.onest.time.R;
import net.onest.time.utils.DateUtil;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordFragment extends Fragment {
    private TextView appTime;
    private ImageView appHead;
    private int i=0;

    //饼状图:
    private PieChart pieChart;

    //选择日期：
    private RadioGroup radioDataGroup;
    private RadioButton radioDayButton,radioWeekButton,radioMonthButton;

    private TextView todayFocus,dataDateTxt,appDateTxt;

    //水平柱状图:
    private HorizontalBarChart barHor;
    List<BarEntry>list=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment, container, false);
        //改变资源文件颜色
//        // 获取drawable资源文件
//        Drawable drawable = getResources().getDrawable(R.drawable.shape_10dp_all_corners);
//
//// 将drawable资源文件转换为GradientDrawable对象
//        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
//
//// 设置新的颜色
//        gradientDrawable.setColor(Color.parseColor("#FFFFFF"));
//
//// 将修改后的drawable重新设置给View
//        view.findViewById(R.id.record_fragment_lin1).setBackground(gradientDrawable);
//        view.findViewById(R.id.record_fragment_lin2).setBackground(gradientDrawable);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);


        //当前日期：
        todayFocus.setText(DateUtil.getCurDay());
        dataDateTxt.setText(DateUtil.getCurDay());
        appDateTxt.setText(DateUtil.getCurDay());

        List<PieEntry> yVals = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        if(radioDayButton.isChecked()){
            //设置饼状图数据：
            yVals.add(new PieEntry(28.6f, "陆地"));
            yVals.add(new PieEntry(60.3f, "海洋"));
            yVals.add(new PieEntry(100f-28.6f-60.3f, "天空"));

            colors.add(Color.parseColor("#4A92FC"));
            colors.add(Color.parseColor("#ee6e55"));
            colors.add(Color.parseColor("#adff2f"));
            setPieChartData(yVals,colors);
        }

        radioDataGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.record_fragment_time_data_day:
                        dataDateTxt.setText(DateUtil.getCurDay());

                        setPieChartData(yVals,colors);
                        pieChart.invalidate();//实时更新数据
                        break;
                    case R.id.record_fragment_time_data_week:
                        dataDateTxt.setText(DateUtil.getCurWeek());

                        pieChart.setData(null);
                        pieChart.notifyDataSetChanged();
                        pieChart.invalidate();
                        break;
                    case R.id.record_fragment_time_data_month:
                        dataDateTxt.setText(DateUtil.getCurMonth());

                        pieChart.setData(null);
                        pieChart.notifyDataSetChanged();
                        pieChart.invalidate();
                        break;
                }
            }
        });


        //设置圆大小:
        pieChart.setHoleRadius(50f);
//        pieChart.setHoleColor(Color.RED);//中间空心圆的颜色
        pieChart.setTransparentCircleRadius(60f);
//        pieChart.setCenterTextRadiusPercent(20);

        //设置饼状图主题：
        pieChart.getDescription().setEnabled(false);
//        String descriptionStr = "平台上有违章车辆和没违章车辆的占比统计";
//        Description description = new Description();
//        description.setText(descriptionStr);
//        pieChart.setDescription(description);
//        //设置饼状图主题样式：
//        description.setTextSize(18f);
//        description.setTextColor(Color.parseColor("#4A92FC"));
//
////设置饼状图主题位置
//        // 获取屏幕中间x 轴的像素坐标
//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        float x = dm.widthPixels / 2;
//// y轴像素坐标，获取文本高度（dp）+上方间隔12dp 转换为像素
//        Paint paint = new Paint();
//        paint.setTextSize(18f);
//        Rect rect = new Rect();
//        paint.getTextBounds(descriptionStr, 0, descriptionStr.length(), rect);
//        float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                rect.height() + 12, getResources().getDisplayMetrics());
//// 设置饼状图主题的位置
//        description.setPosition(x, y);



//-----------------------------------------------------------------------------------------------

//        Drawable drawable1 = getContext().getResources().getDrawable(R.drawable.add,null);
//        Drawable drawable2 = getContext().getResources().getDrawable(R.drawable.trophy,null);
//        Drawable drawable3 = getContext().getResources().getDrawable(R.drawable.trophy1,null);
//        Drawable drawable4 = getContext().getResources().getDrawable(R.drawable.home1,null);
//        Drawable drawable5 = getContext().getResources().getDrawable(R.drawable.person1,null);
//        drawable1.setBounds(0,40,0,40);

        //获取app运行时间及其头像:
//        getAppTimeAndHead();

        //水平柱状图:X、Y轴颠倒   //设置图片大小及其距离：
//        list.add(new BarEntry(1,3,setImageSizeAndDistance("add")));
//        list.add(new BarEntry(2,5,setImageSizeAndDistance("trophy")));
//        list.add(new BarEntry(3,6,setImageSizeAndDistance("trophy1")));
//        list.add(new BarEntry(4,4,setImageSizeAndDistance("home1")));
//        list.add(new BarEntry(5,2,setImageSizeAndDistance("person1")));


        BarDataSet barDataSet=new BarDataSet(getAppTimeAndHead(),"App前台使用时间");
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(0.5f);//设置条形柱的宽度
        barDataSet.setBarBorderWidth(1);//设置条形图边框宽度
        barDataSet.setDrawIcons(true);
        barDataSet.setValueTextSize(10);//设置条形图之上的文字的大小
        barDataSet.setValueTextColor(Color.BLUE);//设置条形图之上的文字的颜色
        barDataSet.setColor(Color.RED);//设置条形柱子的颜色

//        barDataSet.setFormLineWidth(100f);
//        barDataSet.setFormSize(10f);
        barHor.setData(barData);

        barHor.getDescription().setEnabled(false);//隐藏右下角英文
        barHor.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为右边
        barHor.getAxisLeft().setEnabled(false);//隐藏上侧Y轴   默认是上下两侧都有Y轴

        barHor.canScrollVertically(1);

//        barHor.getAxisRight().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
//        barHor.getXAxis().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        barHor.getAxisLeft().setDrawGridLines(false);

        barHor.getXAxis().setEnabled(false);//取消X轴数值
        barHor.getAxisRight().setEnabled(false);//取消下方的数值
        barHor.getAxisLeft().setEnabled(true);//显示上方的数值

        //Y轴自定义坐标
        barHor.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });
        barHor.getAxisLeft().setAxisMaximum(500);   //Y轴最大数值
        barHor.getAxisLeft().setAxisMinimum(0);   //Y轴最小数值

        //设置动画
        barHor.animateY(1000, Easing.Linear);
        barHor.animateX(1000, Easing.Linear);

        barHor.setDoubleTapToZoomEnabled(false);
        //禁止拖拽
//        barHor.setDragEnabled(false);
        //X轴或Y轴禁止缩放
        barHor.setScaleXEnabled(false);
        barHor.setScaleYEnabled(false);
        barHor.setScaleEnabled(false);
        //禁止所有事件
//        barHor.setTouchEnabled(false);

//        barHor.setBackgroundColor(Color.RED);//设置表格背景颜色

//        barHor.getDescription().setEnabled(true);                  //是否显示右下角描述
//        barHor.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
//        barHor.getDescription().setTextSize(20);                    //字体大小
//        barHor.getDescription().setTextColor(Color.RED);             //字体颜色

//        //图例
//        Legend legend=barHor.getLegend();
//        legend.setEnabled(true);    //是否显示图例
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);    //图例的位置
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

//        //X轴自定义值
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                return dateValueList.get((int) value % dateValueList.size()).getTradeDate();
//            }
//        });
//        //右侧Y轴自定义值
//        rightAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                return (int) value + "%";
//            }
//        });
    }

    private List<BarEntry> getAppTimeAndHead() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            try {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            } catch (Exception e) {
                Toast.makeText(getContext(),"无法开启允许查看使用情况的应用界面",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }


        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager packageManager = getContext().getPackageManager();

        // 获取当前时间的毫秒数
        long currentTime = System.currentTimeMillis();

        // 获取前一小时内的应用使用情况（可根据需求自定义时间范围）
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 60*60*1000, currentTime);

        // 用于存储应用程序包名与使用时间的映射关系
        SortedMap<String, Long> appUsageMap = new TreeMap<>();

        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();
            long totalTimeInForeground = usageStats.getTotalTimeInForeground() / 1000; // 转换为秒

            if (totalTimeInForeground > 0) {
                appUsageMap.put(packageName, totalTimeInForeground);
            }
        }

        // 获取应用程序的头像并打印输出
        for (String packageName : appUsageMap.keySet()) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                String appName = (String) packageManager.getApplicationLabel(appInfo);
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);

                // 打印应用程序的名称、头像和使用时间
                System.out.println("App Name: " + appName);
                appTime.setText("App Name: " + appName+"Usage Time (seconds): " + appUsageMap.get(packageName));
                System.out.println("App Icon: " + appIcon);
                Glide.with(getContext()).load(appIcon).circleCrop().into(appHead);
    //          list.add(new BarEntry(5,2,setImageSizeAndDistance("person1")));
                if(appUsageMap.get(packageName)/60!=0){
                    float time = appUsageMap.get(packageName)/60;
                    list.add(new BarEntry(i++,time,setImageSizeAndDistance(appIcon)));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        i=0;
        return list;
    }


    private void setPieChartData(List<PieEntry> yVals, List<Integer> colors) {
        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setEntryLabelColor(Color.RED);//描述文字的颜色
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

        pieChart.setData(pieData);
        pieChart.setExtraOffsets(0f,32f,0f,32f);
        //动画（如果使用了动画可以则省去更新数据的那一步）
//        pieChart.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        pieChart.animateX(1000); //X轴动画
        pieChart.animateXY(1000,1000);//XY两轴混合动画
    }

    private Drawable setImageSizeAndDistance(Drawable drawable) {
//        int imageResId = getContext().getResources().getIdentifier(name,"drawable", getActivity().getPackageName());
//        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),imageResId);
//        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,50,50,false);
//        Drawable alteredDrawable = new BitmapDrawable(getActivity().getResources(),bitmap1);
//        alteredDrawable.setBounds(100,0,0,0);

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

// 缩放比例
        float scale = 0.3f; // 缩放比例

// 缩放后的宽高
        int width = (int) (bitmap.getWidth() * scale);
        int height = (int) (bitmap.getHeight() * scale);

// 缩放Bitmap对象
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

// 将缩放后的Bitmap对象转换为Drawable
        Drawable scaledDrawable = new BitmapDrawable(getResources(), scaledBitmap);
        scaledDrawable.setBounds(100,0,0,0);

        return scaledDrawable;
    }

    public void setExtraOffsets(float left, float top, float right, float bottom) {
        pieChart.setExtraLeftOffset(left);
        pieChart.setExtraTopOffset(top);
        pieChart.setExtraRightOffset(right);
        pieChart.setExtraBottomOffset(bottom);
    }

    private void findViews(View view) {
        pieChart = view.findViewById(R.id.record_fragment_pie_chart);
        barHor = view.findViewById(R.id.record_fragment_bar_chart);
        radioDataGroup = view.findViewById(R.id.record_fragment_time_data);
        radioDayButton = view.findViewById(R.id.record_fragment_time_data_day);
        radioWeekButton = view.findViewById(R.id.record_fragment_time_data_week);
        radioMonthButton = view.findViewById(R.id.record_fragment_time_data_month);

        todayFocus = view.findViewById(R.id.record_fragment_today_focus);
        dataDateTxt = view.findViewById(R.id.record_fragment_time_data_date);
        appDateTxt = view.findViewById(R.id.record_fragment_app_use_time_txt);

        appTime = view.findViewById(R.id.app_time);
        appHead = view.findViewById(R.id.app_head);
    }
}
