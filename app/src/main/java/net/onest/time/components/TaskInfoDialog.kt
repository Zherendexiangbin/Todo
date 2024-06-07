package net.onest.time.components

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import net.onest.time.R
import net.onest.time.api.StatisticApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.api.vo.statistic.StatisticVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.*
import java.time.LocalDateTime
import java.time.ZoneOffset

class TaskInfoDialog(
    private val context: Context, // 存储当前交互的taskVo
    private val task: TaskVo, // 管理的列表
    private val tasks: List<TaskVo>, // 适配器
    private val adapter: AdapterHolder
) : AlertDialog(
    context
) {
    //点击childItem弹窗：
    private var title: TextView? = null
    private var learnFrequency: TextView? = null
    private var learnTime: TextView? = null
    private var textRemark: TextView? = null
    private var changeBackground: Button? = null
    private var setItem: Button? = null
    private var moveItem: Button? = null
    private var deleteItem: Button? = null
    private val timing: Button? = null
    private var parentBackground: LinearLayout? = null
    private var statistics: Button? = null
    private var remarkLin: LinearLayout? = null

    init {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.item_pop, null)
            .withCustomAlphaAnimation()

        findViews(dialogView)
        setListeners()

        title?.text = task.taskName
        try {
            val statistics = StatisticApi.simpleStatisticByTask(task.taskId)
            // 累计专注次数
            learnFrequency?.text = (statistics.tomatoTimes ?: 0).toString()

            // 累计专注时长
            learnTime?.text = (statistics.tomatoDuration ?: 0).toString()
        } catch (e: RuntimeException) {
            e.message?.showToast()
        }

        //备注:
//        if(task.remark.isNullOrBlank()){
//            remarkLin!!.visibility = View.GONE
//        }else{
//            textRemark?.text = task.remark
//        }
        textRemark?.text = if(task.remark.isNullOrBlank()) "无备注" else task.remark

        show()
        window!!.setContentView(dialogView)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setListeners() {
        //删除:
        deleteItem!!.setOnClickListener { v: View? ->
            DeleteTaskDialog(
                context, task, tasks, adapter, this
            )
                .show()
        }

        //编辑
        setItem!!.setOnClickListener {
            UpdateTaskDialog(
                context,
                task,
                tasks,
                adapter,
                this@TaskInfoDialog
            )
        }

        //获取数据统计
        statistics!!.setOnClickListener {

            val statisticByTask = StatisticApi.statisticByTask(
                task.taskId,
                LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))
            )

            val builder = Builder(getContext(), R.style.CustomDialogStyle)
            val inflater = LayoutInflater.from(getContext())
            val dialogViewAban = inflater.inflate(R.layout.task_statistics_pop, null)
            val dialogAban: Dialog = builder.create()
            dialogAban.show()
            dialogAban.window!!.setContentView(dialogViewAban)
            dialogAban.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val tomatoTimes = dialogViewAban.findViewById<TextView>(R.id.statistics_tomato_times)
            val tomatoDuration = dialogViewAban.findViewById<TextView>(R.id.statistics_tomato_duration)
            val averageTomatoTimes = dialogViewAban.findViewById<TextView>(R.id.statistics_today_tomato_times)
            val averageTomatoDuration = dialogViewAban.findViewById<TextView>(R.id.statistics_today_tomato_duration)
            val radioGroup = dialogViewAban.findViewById<RadioGroup>(R.id.statistics_time_data)
            val radioButtonDay = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_day)
            val radioButtonWeek = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_week)
            val radioButtonMonth = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_month)
            var pieChart = dialogViewAban.findViewById<PieChart>(R.id.statistics_pie_chart)

            tomatoTimes.text = "${statisticByTask.tomatoTimes}"
            tomatoDuration.text = "${statisticByTask.tomatoDuration}"
            averageTomatoTimes.text = "${statisticByTask.avgTomatoTimes}"
            averageTomatoDuration.text = "${statisticByTask.avgTomatoDuration}"


            val pieEntries = statisticByTask.ratioByDurationOfDay.map {
                PieEntry(it.ratio.toFloat(), it.taskName)
            }

            val colors = statisticByTask.ratioByDurationOfDay.map {
                ColorUtil.getColorByRgb(null)
            }

            setPieChartData(pieEntries, colors,pieChart)
            pieChart!!.notifyDataSetChanged()

            radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.statistics_time_data_day -> {
                        val pieEntries = statisticByTask.ratioByDurationOfDay.map {
                            PieEntry(it.ratio.toFloat(), it.taskName)
                        }

                        val colors = statisticByTask.ratioByDurationOfDay.map {
                            ColorUtil.getColorByRgb(null)
                        }

                        setPieChartData(pieEntries, colors,pieChart)
                        pieChart!!.notifyDataSetChanged()
                    }

                    R.id.statistics_time_data_week -> {
                        val pieEntries = statisticByTask.ratioByDurationOfWeek.map {
                            PieEntry(it.ratio.toFloat(), it.taskName)
                        }

                        val colors = statisticByTask.ratioByDurationOfWeek.map {
                            ColorUtil.getColorByRgb(null)
                        }

                        setPieChartData(pieEntries, colors,pieChart)
                        pieChart!!.notifyDataSetChanged()
                    }

                    R.id.statistics_time_data_month -> {
                        val pieEntries = statisticByTask.ratioByDurationOfMonth.map {
                            PieEntry(it.ratio.toFloat(), it.taskName)
                        }

                        val colors = statisticByTask.ratioByDurationOfMonth.map {
                            ColorUtil.getColorByRgb(null)
                        }

                        setPieChartData(pieEntries, colors,pieChart)
                        pieChart!!.notifyDataSetChanged()
                    }
                }
            }
        }

//        //改变背景:
//        changeBackground!!.setOnClickListener {
//
//            parentBackground!!.background = DrawableUtil.getRandomImage(context)
//
//        }

    }


    private fun setPieChartData(yVals: List<PieEntry>, colors: List<Int>,pieChart: PieChart) {
        if(yVals.isEmpty()){
            pieChart.visibility = View.INVISIBLE
        }else{
            pieChart.visibility = View.VISIBLE

            val pieDataSet = PieDataSet(yVals, "")
            pieDataSet.colors = colors
            pieChart.setEntryLabelColor(Color.parseColor("#ff8c00")) //描述文字的颜色
            pieDataSet.valueTextSize = 15f //数字大小
            pieDataSet.valueTextColor = Color.BLACK //数字颜色
            pieDataSet.valueFormatter = object : ValueFormatter() {
                @SuppressLint("DefaultLocale")
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
            pieChart.data = pieData
            pieChart.setExtraOffsets(0f, 20f, 0f, 20f)
            //动画（如果使用了动画可以则省去更新数据的那一步）
//        pieChart.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        pieChart.animateX(1000); //X轴动画
            pieChart.animateXY(1000, 1000) //XY两轴混合动画

            val legend = pieChart!!.legend
            legend.xEntrySpace = 20f
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.isWordWrapEnabled = true
//            legend.formSize = 15f
//            legend.textSize = 15f
            legend.textColor=Color.BLACK

            pieDataSet.notifyDataSetChanged()
            pieChart.notifyDataSetChanged()

        }
    }

    private fun findViews(dialogView: View) {
        title = dialogView.findViewById(R.id.txt_title) //待办标题txt
//        changeBackground = dialogView.findViewById(R.id.btn_changeBackground) //设置背景button
        setItem = dialogView.findViewById(R.id.btn_set) //编辑待办button
//        moveItem = dialogView.findViewById(R.id.btn_move) //排序或移动待办button
        deleteItem = dialogView.findViewById(R.id.btn_delete) //删除待办button
        learnFrequency = dialogView.findViewById(R.id.txt_learn_frequency) //累计学习次数txt
        learnTime = dialogView.findViewById(R.id.txt_learn_time) //累计学习时间txt单位分钟
//        learnHistory = dialogView.findViewById(R.id.learn_history) //历史记录(页面跳转)
//        learnStatistics = dialogView.findViewById(R.id.learn_statistics) //数据统计(页面跳转)
        statistics = dialogView.findViewById(R.id.btn_statistics)
        textRemark = dialogView.findViewById(R.id.text_remark)
        parentBackground = dialogView.findViewById(R.id.background_parent)
        remarkLin = dialogView.findViewById(R.id.remark_visible)
    }
}
