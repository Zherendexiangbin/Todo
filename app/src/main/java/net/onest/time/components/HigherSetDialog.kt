package net.onest.time.components

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import net.onest.time.R
import net.onest.time.api.dto.TaskDto
import net.onest.time.utils.withCustomAlphaAnimation

/**
 * 待办（第一个）页面，添加Task的高级设置
 */
class HigherSetDialog(
    context: Context,
    private val task: TaskDto,
) : AlertDialog(
    context
) {
    private lateinit var remark: EditText
    private lateinit var clockTimes: EditText
    private lateinit var rest: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var clockAbout: Button
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button


    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.todo_fragment_add_higher_setting, null)
            .withCustomAlphaAnimation()

        findViews(view)
        setListeners()
        show()

        window!!.setContentView(view)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun findViews(view: View) {
        remark = view.findViewById(R.id.todo_fragment_add_higher_remark)
        clockTimes = view.findViewById(R.id.todo_fragment_add_clock_times)
        rest = view.findViewById(R.id.todo_fragment_add_rest_time)
        checkBox = view.findViewById(R.id.todo_fragment_add_higher_again)
        clockAbout = view.findViewById(R.id.todo_clock_times_about)
        btnYes = view.findViewById(R.id.add_todo_higher_setting_item_yes)
        btnNo = view.findViewById(R.id.add_todo_higher_setting_item_no)
    }

    private fun setListeners() {
        // 什么是单次循环次数
        clockAbout.setOnClickListener { v: View? ->
            TipDialog(
                context,
                "什么是单次循环次数",
                """
                    举例:
                    小明每次学习想学75分钟，但是75分钟太长学的太累，那么可以设定一个番茄钟的时间为25分钟，单次预期循环次数为3次。
                    这样的番茄钟就会按照:
                    学习25分钟-休息-学习25分钟-休息-学习25分钟-休息(共循环三次)
                    来执行
                """.trimIndent())
                .show()
        }

        btnYes.setOnClickListener { v: View? ->
            task.remark = remark.text.toString().trim()

            task.estimate!!.clear()
            val e = clockTimes.text.toString().trim()
            task.estimate!!.add(if (e.isNotBlank()) e.toInt() else 0)

            val r = rest.text.toString().trim()
            task.restTime = if (r.isNotBlank()) r.toInt() else 5

            task.again = if (checkBox.isChecked) 1 else 0
            dismiss()
        }
        btnNo.setOnClickListener { dismiss() }
    }
}
