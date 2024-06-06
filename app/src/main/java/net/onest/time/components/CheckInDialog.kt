package net.onest.time.components

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import net.onest.time.R
import net.onest.time.entity.CheckIn
import net.onest.time.utils.ColorUtil
import net.onest.time.utils.createBitmap
import net.onest.time.utils.saveBitmapCache
import net.onest.time.utils.saveBitmapGallery
import net.onest.time.utils.showToast
import net.onest.time.utils.withCustomAlphaAnimation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.function.Consumer


@SuppressLint("RestrictedApi")
class CheckInDialog(
    context: Context,
    private val checkIn: CheckIn
) : Dialog(
    context
) {
    private lateinit var username: TextView
    private lateinit var checkInBtn: Button
    private lateinit var share: Button
    private lateinit var save: Button
    private lateinit var tomatoTimes: TextView
    private lateinit var tomatoDuration: TextView
    private lateinit var datatime: TextView
    private lateinit var chart: PieChart

    private var view: View = LayoutInflater.from(getContext())
        .inflate(R.layout.component_check_in, null)
        .withCustomAlphaAnimation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    init {

        show()

        findViews(view)
        setListeners()
        setData()

        window?.run {
            setContentView(view)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }
    }

    private fun setData() {
        // username
        username.text = checkIn.username

        // 专注次数
        tomatoTimes.text = checkIn.tomatoTimes.toString()

        // 专注时长
        tomatoDuration.text = checkIn.tomatoDuration.toString()

        // 日期
        datatime.text = SimpleDateFormat("yyyy年MM月dd日 HH:mm") .format(Date())

        // chart
        val pieEntryList = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()
        checkIn.taskRatio.forEach {
            pieEntryList.add(PieEntry(it.ratio.toFloat(), it.taskName))
            colors.add(ColorUtil.getColorByRgb(null))
        }
        val pieDataSet = PieDataSet(pieEntryList, "")

        pieDataSet.colors = colors
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
        pieDataSet.valueLinePart2Length = 0.3f //设置数据连接线长度

        //设置两根连接线的颜色
        pieDataSet.valueLineColor = Color.BLUE

        //设置圆大小:
        chart.setHoleColor(Color.TRANSPARENT)
        chart.transparentCircleRadius = 40f

        val legend: Legend = chart.legend
        legend.isWordWrapEnabled = true
        legend.orientation = Legend.LegendOrientation.HORIZONTAL

        //设置饼状图主题：
        chart.description.isEnabled = false
        chart.setEntryLabelColor(Color.parseColor("#ff8c00")) //描述文字的颜色
        chart.data = PieData(pieDataSet)
        chart.setExtraOffsets(0f, 12f, 0f, 12f)
        chart.animateXY(1000, 1000) //XY两轴混合动画
        chart.notifyDataSetChanged()
    }

    private fun setListeners() {
        share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)

            // 比如发送二进制文件数据流内容（比如图片、视频、音频文件等等）
            // 指定发送的内容 (EXTRA_STREAM 对于文件 Uri )

            view.createBitmap(window!!) { bitmap, success ->
                if (success) {
                    bitmap!!.saveBitmapCache("${context.externalCacheDir?.path}/checkin")
                        .run {
                            shareIntent.putExtra(Intent.EXTRA_STREAM, this)
                            // 指定发送内容的类型 (MIME type)
                            shareIntent.setType("image/png")
                            context.startActivity(shareIntent)
                    }
                }
            }
        }

        save.setOnClickListener {
            view.createBitmap(window!!) { bitmap, success ->
                if (success) {
                    bitmap!!.saveBitmapGallery()
                    "已保存至相册".showToast()
                } else {
                    "出错了".showToast()
                }
            }
        }
    }

    fun setSendImageConsumer(consumer: Consumer<Bitmap>) {
        checkInBtn.setOnClickListener {
            view.createBitmap(window!!) { bitmap, success ->
                if (success) {
                    consumer.accept(bitmap!!)
                }
            }
            dismiss()
        }
    }

    private fun findViews(view: View) {
        username = view.findViewById(R.id.txt_user_name)
        checkInBtn = view.findViewById(R.id.btn_check_in)
        share = view.findViewById(R.id.btn_share)
        save = view.findViewById(R.id.btn_save)
        tomatoTimes = view.findViewById(R.id.txt_tomato_times_value)
        tomatoDuration = view.findViewById(R.id.txt_tomato_duration_value)
        datatime = view.findViewById(R.id.txt_current_datatime)
        chart = view.findViewById(R.id.chart_datetime_ratio)
    }
}