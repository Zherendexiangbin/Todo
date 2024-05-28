package net.onest.time.navigation.fragment

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import net.onest.time.R
import net.onest.time.api.StatisticApi
import net.onest.time.api.vo.statistic.StatisticVo
import net.onest.time.databinding.RecordFragmentBinding
import net.onest.time.utils.ColorUtil
import net.onest.time.utils.DateUtil
import net.onest.time.utils.showToast
import java.util.SortedMap
import java.util.TreeMap

class RecordFragment : Fragment() {
    private var i = 0

    //饼状图:
    private var pieChart: PieChart? = null

    //选择日期：
    private var radioDataGroup: RadioGroup? = null
    private var radioDayButton: RadioButton? = null
    private var radioWeekButton: RadioButton? = null
    private var radioMonthButton: RadioButton? = null

    private var todayFocus: TextView? = null
    private var dataDateTxt: TextView? = null
    private var appDateTxt: TextView? = null

    //水平柱状图:
    private var barHor: HorizontalBarChart? = null
    var list: MutableList<BarEntry> = ArrayList()

    private lateinit var binding: RecordFragmentBinding

    private var statisticsVo: StatisticVo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            statisticsVo = StatisticApi.statistic()
        } catch (e: RuntimeException) {
            e.message?.showToast()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RecordFragmentBinding.inflate(inflater, container, false)
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
        val view = binding.root

        findViews(view)
        setListeners()
        return view
    }

    private fun setListeners() {
        radioDataGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.record_fragment_time_data_day -> {
                    val pieEntries = statisticsVo!!.ratioByDurationOfDay.map {
                        PieEntry(it.ratio.toFloat(), it.taskName)
                    }

                    val colors = statisticsVo!!.ratioByDurationOfDay.map {
                        ColorUtil.getColorByRgb(null)
                    }

                    dataDateTxt!!.text = DateUtil.curDay

                    setPieChartData(pieEntries, colors)
                    pieChart!!.notifyDataSetChanged()
                }

                R.id.record_fragment_time_data_week -> {
                    val pieEntries = statisticsVo!!.ratioByDurationOfWeek.map {
                        PieEntry(it.ratio.toFloat(), it.taskName)
                    }

                    val colors = statisticsVo!!.ratioByDurationOfWeek.map {
                        ColorUtil.getColorByRgb(null)
                    }

                    dataDateTxt!!.text = DateUtil.curWeek
                    setPieChartData(pieEntries, colors)
                    pieChart!!.notifyDataSetChanged()
                }

                R.id.record_fragment_time_data_month -> {
                    val pieEntries = statisticsVo!!.ratioByDurationOfMonth.map {
                        PieEntry(it.ratio.toFloat(), it.taskName)
                    }

                    val colors = statisticsVo!!.ratioByDurationOfMonth.map {
                        ColorUtil.getColorByRgb(null)
                    }

                    dataDateTxt!!.text = DateUtil.curMonth
                    setPieChartData(pieEntries, colors)
                    pieChart!!.notifyDataSetChanged()
                }
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsVo?.let {
            setCumulativeFocus()
            setTodayFocus()
            setFocusDurationRatio()
        }

        //当前日期：
        todayFocus!!.text = DateUtil.curDay
        dataDateTxt!!.text = DateUtil.curDay
        appDateTxt!!.text = DateUtil.curDay

//        val yVals: MutableList<PieEntry> = ArrayList()
//        val colors: MutableList<Int> = ArrayList()
//        if (radioDayButton!!.isChecked) {
//            //设置饼状图数据：
//            yVals.add(PieEntry(28.6f, "陆地"))
//            yVals.add(PieEntry(60.3f, "海洋"))
//            yVals.add(PieEntry(100f - 28.6f - 60.3f, "天空"))
//
//            colors.add(Color.parseColor("#4A92FC"))
//            colors.add(Color.parseColor("#ee6e55"))
//            colors.add(Color.parseColor("#adff2f"))
//            setPieChartData(yVals, colors)
//        }


        //设置圆大小:
        pieChart!!.holeRadius = 50f
        //        pieChart.setHoleColor(Color.RED);//中间空心圆的颜色
        pieChart!!.transparentCircleRadius = 60f

        //        pieChart.setCenterTextRadiusPercent(20);

        //设置饼状图主题：
        pieChart!!.description.isEnabled = false


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
        val barDataSet = BarDataSet(appTimeAndHead, "App前台使用时间")
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f //设置条形柱的宽度
        barDataSet.barBorderWidth = 1f //设置条形图边框宽度
        barDataSet.setDrawIcons(true)
        barDataSet.valueTextSize = 10f //设置条形图之上的文字的大小
        barDataSet.valueTextColor = Color.BLUE //设置条形图之上的文字的颜色
        barDataSet.color = Color.RED //设置条形柱子的颜色

        //        barDataSet.setFormLineWidth(100f);
//        barDataSet.setFormSize(10f);
        barHor!!.data = barData

        barHor!!.description.isEnabled = false //隐藏右下角英文
        barHor!!.xAxis.position = XAxis.XAxisPosition.BOTTOM //X轴的位置 默认为右边
        barHor!!.axisLeft.isEnabled = false //隐藏上侧Y轴   默认是上下两侧都有Y轴

        barHor!!.canScrollVertically(1)

        //        barHor.getAxisRight().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
//        barHor.getXAxis().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        barHor!!.axisLeft.setDrawGridLines(false)

        barHor!!.xAxis.isEnabled = false //取消X轴数值
        barHor!!.axisRight.isEnabled = false //取消下方的数值
        barHor!!.axisLeft.isEnabled = true //显示上方的数值

        //Y轴自定义坐标
        barHor!!.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float, axis: AxisBase): String {
                return ""
            }
        }
        barHor!!.axisLeft.axisMaximum = 2000f //Y轴最大数值
        barHor!!.axisLeft.axisMinimum = 0f //Y轴最小数值

        //设置动画
        barHor!!.animateY(1000, Easing.Linear)
        barHor!!.animateX(1000, Easing.Linear)

        barHor!!.isDoubleTapToZoomEnabled = false
        //禁止拖拽
//        barHor.setDragEnabled(false);
        //X轴或Y轴禁止缩放
        barHor!!.isScaleXEnabled = false
        barHor!!.isScaleYEnabled = false
        barHor!!.setScaleEnabled(false)


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


//        AAChartModel aaChartModel = new AAChartModel()
//                .chartType(AAChartType.Area)
//                .title("THE HEAT OF PROGRAMMING LANGUAGE")
//                .subtitle("Virtual Data")
//                .backgroundColor("#4b2b7f")
//                .categories(new String[]{"Java", "Swift", "Python", "Ruby", "PHP", "Go", "C", "C#", "C++"})
//                .dataLabelsEnabled(false)
//                .yAxisGridLineWidth(0f)
//                .series(new AASeriesElement[]{
//                        new AASeriesElement()
//                                .name("Tokyo")
//                                .data(new Object[]{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6}),
//                        new AASeriesElement()
//                                .name("NewYork")
//                                .data(new Object[]{0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5}),
//                        new AASeriesElement()
//                                .name("London")
//                                .data(new Object[]{0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0}),
//                        new AASeriesElement()
//                                .name("Berlin")
//                                .data(new Object[]{3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8})
//                });
//        AAChartModel aaChartModel = new AAChartModel()
//                .chartType(AAChartType.Pie)
//                .title("本月中断原因分析")
//                .backgroundColor("#778899")
//
//                .series(new AASeriesElement[]{
//                        new AASeriesElement()
//                                .innerSize(90f)
//                                .borderWidth(2f)
//                                .name("饼图开发")
//                                .data(new Object[]{15, 15, 35, 20, 15})
//
//                });
//        aaChartView.aa_drawChartWithChartModel(aaChartModel);
    }

    private fun setFocusDurationRatio() {
        val pieEntries = statisticsVo!!.ratioByDurationOfDay.map {
            PieEntry(it.ratio.toFloat(), it.taskName)
        }

        val colors = statisticsVo!!.ratioByDurationOfDay.map {
            ColorUtil.getColorByRgb(null)
        }

        setPieChartData(pieEntries, colors)
    }

    /**
     * 设置今日专注的数据
     */
    private fun setTodayFocus() {
        val todayMillisecond = DateUtil.epochMillisecond()
        binding.todayTomatoTimes.text = (statisticsVo!!
            .dayTomatoMap[todayMillisecond]
            ?.tomatoTimes ?: 0).toString()

        binding.todayTomatoDuration.text = (statisticsVo!!
            .dayTomatoMap[todayMillisecond]
            ?.tomatoDuration ?: 0).toString()
    }

    /**
     * 设置累计专注的数据
     */
    private fun setCumulativeFocus() {
        binding.tomatoTimes.text = (statisticsVo!!.tomatoTimes ?: 0).toString()
        binding.tomatoDuration.text = (statisticsVo!!.tomatoDuration ?: 0).toString()
        binding.avgTomatoDuration.text = (statisticsVo!!.avgTomatoTimes ?: 0).toString()
    }

    private val appTimeAndHead: List<BarEntry>
        get() {
            //        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            try {
//                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//            } catch (Exception e) {
//                Toast.makeText(getContext(),"无s法开启允许查看使用情况的应用界面",Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//        }

            XXPermissions.with(this) // 申请单个权限
                .permission(Permission.PACKAGE_USAGE_STATS) // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                        if (!allGranted) {
                            Toast.makeText(
                                context,
                                "获取部分权限成功，但部分权限未正常授予",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        //                        Toast.makeText(getContext(), "获取权限成功", Toast.LENGTH_SHORT).show();
                    }

                    override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                        if (doNotAskAgain) {
                            Toast.makeText(context, "被拒绝授权，请手动授予权限", Toast.LENGTH_SHORT)
                                .show()
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context!!, permissions)
                        } else {
                            Toast.makeText(context, "获取权限失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                })


            val usageStatsManager =
                requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val packageManager = requireContext().packageManager

            // 获取当前时间的毫秒数
            val currentTime = System.currentTimeMillis()

            // 获取前一小时内的应用使用情况（可根据需求自定义时间范围）
            val usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - 60 * 60 * 1000,
                currentTime
            )

            // 用于存储应用程序包名与使用时间的映射关系
            val appUsageMap: SortedMap<String, Long> = TreeMap()

            for (usageStats in usageStatsList) {
                val packageName = usageStats.packageName
                val totalTimeInForeground = usageStats.totalTimeInForeground / 1000 // 转换为秒

                if (totalTimeInForeground > 0) {
                    appUsageMap[packageName] = totalTimeInForeground
                }
            }

            // 获取应用程序的头像并打印输出
            for (packageName in appUsageMap.keys) {
                try {
                    val appInfo =
                        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                    val appName = packageManager.getApplicationLabel(appInfo) as String
                    val appIcon = packageManager.getApplicationIcon(appInfo)

                    // 打印应用程序的名称、头像和使用时间
                    println("App Name: $appName")
                    println("App Icon: $appIcon")
                    //          list.add(new BarEntry(5,2,setImageSizeAndDistance("person1")));
                    if (appUsageMap[packageName]!! / 60 != 0L) {
                        val time = (appUsageMap[packageName]!! / 60).toFloat()
                        list.add(BarEntry(i++.toFloat(), time, setImageSizeAndDistance(appIcon)))
                    }
                } catch (e: NameNotFoundException) {
                    e.printStackTrace()
                }
            }
            i = 0
            return list
        }


    private fun setPieChartData(yVals: List<PieEntry>, colors: List<Int>) {
        val pieDataSet = PieDataSet(yVals, "")
        pieDataSet.colors = colors
        pieChart!!.setEntryLabelColor(Color.RED) //描述文字的颜色
        pieDataSet.valueTextSize = 15f //数字大小
        pieDataSet.valueTextColor = Color.BLACK //数字颜色
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.2f%%", value * 100)
            }
        }

        //设置描述的位置
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.valueLinePart1Length = 0.5f //设置描述连接线长度
        //设置数据的位置
        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.valueLinePart2Length = 0.6f //设置数据连接线长度
        //设置两根连接线的颜色
        pieDataSet.valueLineColor = Color.BLUE

        val pieData = PieData(pieDataSet)
        pieChart!!.data = pieData
        pieChart!!.setExtraOffsets(0f, 32f, 0f, 32f)
        //动画（如果使用了动画可以则省去更新数据的那一步）
//        pieChart.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        pieChart.animateX(1000); //X轴动画
        pieChart!!.animateXY(1000, 1000) //XY两轴混合动画
    }

    private fun setImageSizeAndDistance(drawable: Drawable): Drawable {
//        int imageResId = getContext().getResources().getIdentifier(name,"drawable", getActivity().getPackageName());
//        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),imageResId);
//        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,50,50,false);
//        Drawable alteredDrawable = new BitmapDrawable(getActivity().getResources(),bitmap1);
//        alteredDrawable.setBounds(100,0,0,0);

        val bitmap: Bitmap
        if (drawable is BitmapDrawable) {
            bitmap = drawable.bitmap
        } else {
            bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }

        // 缩放比例
        val scale = 0.3f // 缩放比例

        // 缩放后的宽高
        val width = (bitmap.width * scale).toInt()
        val height = (bitmap.height * scale).toInt()

        // 缩放Bitmap对象
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

        // 将缩放后的Bitmap对象转换为Drawable
        val scaledDrawable: Drawable = BitmapDrawable(resources, scaledBitmap)
        scaledDrawable.setBounds(100, 0, 0, 0)

        return scaledDrawable
    }

    fun setExtraOffsets(left: Float, top: Float, right: Float, bottom: Float) {
        pieChart!!.extraLeftOffset = left
        pieChart!!.extraTopOffset = top
        pieChart!!.extraRightOffset = right
        pieChart!!.extraBottomOffset = bottom
    }

    private fun findViews(view: View) {
        pieChart = view.findViewById(R.id.record_fragment_pie_chart)
        barHor = view.findViewById(R.id.record_fragment_bar_chart)
        radioDataGroup = view.findViewById(R.id.record_fragment_time_data)
        radioDayButton = view.findViewById(R.id.record_fragment_time_data_day)
        radioWeekButton = view.findViewById(R.id.record_fragment_time_data_week)
        radioMonthButton = view.findViewById(R.id.record_fragment_time_data_month)

        todayFocus = view.findViewById(R.id.record_fragment_today_focus)
        dataDateTxt = view.findViewById(R.id.record_fragment_time_data_date)
        appDateTxt = view.findViewById(R.id.record_fragment_app_use_time_txt)
    }
}
