package net.onest.time.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import net.onest.time.R
import net.onest.time.api.StatisticApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.DrawableUtil
import net.onest.time.utils.showToast
import net.onest.time.utils.withCustomAlphaAnimation
import net.onest.time.utils.withOnClickInfoDialog

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
        textRemark?.text = if(textRemark?.text.isNullOrBlank()) "无备注" else task.remark

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

        }

        //改变背景:
        changeBackground!!.setOnClickListener {

            parentBackground!!.background = DrawableUtil.getRandomImage(context)

        }

    }

    private fun findViews(dialogView: View) {
        title = dialogView.findViewById(R.id.txt_title) //待办标题txt
        changeBackground = dialogView.findViewById(R.id.btn_changeBackground) //设置背景button
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
