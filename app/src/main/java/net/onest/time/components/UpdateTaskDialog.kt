package net.onest.time.components

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.textfield.TextInputEditText
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.api.TaskApi
import net.onest.time.api.dto.TaskDto
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.DrawableUtil
import net.onest.time.utils.showToast
import net.onest.time.utils.withCustomAlphaAnimation

class UpdateTaskDialog (
    private val context: Context,
    private val task: TaskVo,
    private val tasks: List<TaskVo>,
    private val adapter: AdapterHolder,
    private val dialog: Dialog
) : AlertDialog(
    context
) {
    private var addYes: Button? = null
    private var addNo: Button? = null
    private var itemNameAbout: Button? = null
    private var relaChange: Button? = null
    private var itemName: TextInputEditText? = null
    private var todoSetTime: RadioGroup? = null
    private var setTimeGroup: RadioGroup? = null

    private var setTimeOne: RadioButton? = null
    private var setTimeTwo: RadioButton? = null
    private var setTimeThree: RadioButton? = null
    private var setTimeGroupOne: RadioButton? = null
    private var setTimeGroupTwo: RadioButton? = null
    private var setTimeGroupThree: RadioButton? = null
    private var setTimeOneTxt: TextView? = null
    private var setTimeTwoTxt: TextView? = null
    private var setTimeThreeTxt: TextView? = null
    private var higherSet: TextView? = null
    private var popRela: RelativeLayout? = null

    init {

//        Toast.makeText(context, "你点击了" + task.taskName, Toast.LENGTH_SHORT).show()

        //设置弹窗：
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.todo_fragment_add_item_pop_window, null)
            .withCustomAlphaAnimation()

        getViews(dialogView) //获取控件

        dialog.show()

        dialog.window!!.setContentView(dialogView)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        itemName?.requestFocus()

        itemName!!.setText(task.taskName)


        when (task.type) {
            // 倒计时
            0 -> {
                todoSetTime?.check(R.id.set_time_one)
                when (task.clockDuration) {
                    25 -> {
                        setTimeGroup?.check(R.id.set_time_one_group_one)
                    }
                    35 -> {
                        setTimeGroup?.check(R.id.set_time_one_group_two)
                    }
                    else -> {
                        setTimeGroup?.check(R.id.set_time_one_group_three)
                        setTimeGroupThree?.text = "${task.clockDuration} 分钟"
                    }
                }
            }

            // 正向计时
            1 -> {
                todoSetTime?.check(R.id.set_time_two)
            }

            // 不计时
            2 -> {
                todoSetTime?.check(R.id.set_time_three)
            }
        }

        setTimeTwoTxt!!.visibility = View.GONE
        setTimeThreeTxt!!.visibility = View.GONE

        Glide.with(context)
            .asBitmap()
            .load(R.drawable.new_card_bg_1)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val drawable: Drawable = BitmapDrawable(resource)
                    popRela!!.background = drawable
                }
            })

        setListeners()

    }

    private fun setListeners() {

        //改变背景：
        relaChange!!.setOnClickListener {
            popRela!!.background = DrawableUtil.getRandomImage(context)
        }

        itemName?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftInput(itemName)
            } else {
                hideSoftInput(itemName)
            }
        }

        higherSet!!.setOnClickListener { view: View? ->
            val builder = Builder(context, R.style.CustomDialogStyle)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting, null)
            val dialog: Dialog = builder.create()
            dialog.show()
            dialog.window!!.setContentView(dialogView)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val remark = dialog.findViewById<EditText>(R.id.todo_fragment_add_higher_remark)
            val clockTimes = dialog.findViewById<EditText>(R.id.todo_fragment_add_clock_times)
            val rest = dialog.findViewById<EditText>(R.id.todo_fragment_add_rest_time)
            val checkBox = dialog.findViewById<CheckBox>(R.id.todo_fragment_add_higher_again)
            val clockAbout = dialog.findViewById<Button>(R.id.todo_clock_times_about)
            val btnYes = dialog.findViewById<Button>(R.id.add_todo_higher_setting_item_yes)
            val btnNo = dialog.findViewById<Button>(R.id.add_todo_higher_setting_item_no)

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
                    """.trimIndent()
                )
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
                dialog.dismiss()
            }
            btnNo.setOnClickListener { dialog.dismiss() }
        }

        itemNameAbout!!.setOnClickListener {
            TipDialog(
                context,
                "什么是番茄钟",
                """
                    1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。
                    2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。
                    3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦
                """.trimIndent()
            ).show()
        }

        todoSetTime!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.set_time_one -> {
                    setTimeGroup!!.visibility = View.VISIBLE
                    setTimeOneTxt!!.visibility = View.VISIBLE
                    setTimeTwoTxt!!.visibility = View.GONE
                    setTimeThreeTxt!!.visibility = View.GONE
                }

                R.id.set_time_two -> {
                    setTimeGroup!!.visibility = View.GONE
                    setTimeOneTxt!!.visibility = View.GONE
                    setTimeTwoTxt!!.visibility = View.VISIBLE
                    setTimeThreeTxt!!.visibility = View.GONE
                }

                R.id.set_time_three -> {
                    setTimeGroup!!.visibility = View.GONE
                    setTimeOneTxt!!.visibility = View.GONE
                    setTimeTwoTxt!!.visibility = View.GONE
                    setTimeThreeTxt!!.visibility = View.VISIBLE
                }
            }
        }

        setTimeGroupThree!!.setOnClickListener {
            Toast.makeText(
                context,
                setTimeGroupThree!!.text.toString() + "",
                Toast.LENGTH_SHORT
            ).show()
            XPopup.Builder(context).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:"
            ) { text -> setTimeGroupThree!!.text = "$text 分钟" }.show()
        }

        addYes!!.setOnClickListener {
            val taskName = itemName!!.text.toString()

            if (taskName.isBlank()) {
                "请输入任务名称".showToast()
                return@setOnClickListener
            }

            task.taskName = taskName

            when (todoSetTime!!.checkedRadioButtonId) {
                // 倒计时
                R.id.set_time_one -> {
                    // 番茄钟时长
                    var tomatoDuration = 0
                    when (setTimeGroup!!.checkedRadioButtonId) {
                        R.id.set_time_one_group_one -> tomatoDuration = 25
                        R.id.set_time_one_group_two -> tomatoDuration = 35
                        R.id.set_time_one_group_three -> tomatoDuration =
                            setTimeGroupThree!!.text.toString().split(" ".toRegex())[0].toInt()
                    }
                    task.type = 0
                    task.clockDuration = tomatoDuration
                }

                // 正向计时
                R.id.set_time_two -> {
                    // 取消了单次循环次数
                    task.taskName = itemName!!.text.toString().trim()
                    task.type = 1
                }

                // 不计时
                R.id.set_time_three -> {
                    task.taskName = itemName!!.text.toString().trim()
                    task.type = 2
                }
            }

            val taskVo = TaskApi.updateTask(TaskDto().withTaskVo(task))
//            adapter.notifyItemChanged(tasks.indexOf(taskVo))
            adapter.notifyDataSetChanged()

            dialog.dismiss()
        }

        addNo!!.setOnClickListener { dialog.dismiss() }
    }

    private fun getViews(dialogView: View) {
        //以下是弹窗控件：
        addYes = dialogView.findViewById(R.id.add_todo_item_yes)
        addNo = dialogView.findViewById(R.id.add_todo_item_no)
        itemNameAbout = dialogView.findViewById(R.id.todo_item_about)
        relaChange = dialogView.findViewById(R.id.add_todo_item_change)

        itemName = dialogView.findViewById(R.id.todo_item_name)

        todoSetTime = dialogView.findViewById(R.id.todo_item_set_time)
        setTimeGroup = dialogView.findViewById(R.id.set_time_one_group)


        setTimeOne = dialogView.findViewById(R.id.set_time_one)
        setTimeTwo = dialogView.findViewById(R.id.set_time_two)
        setTimeThree = dialogView.findViewById(R.id.set_time_three)

        setTimeGroupOne = dialogView.findViewById(R.id.set_time_one_group_one)
        setTimeGroupTwo = dialogView.findViewById(R.id.set_time_one_group_two)
        setTimeGroupThree = dialogView.findViewById(R.id.set_time_one_group_three)

        setTimeOneTxt = dialogView.findViewById(R.id.set_time_one_txt)
        setTimeTwoTxt = dialogView.findViewById(R.id.set_time_two_txt)
        setTimeThreeTxt = dialogView.findViewById(R.id.set_time_three_txt)

        higherSet = dialogView.findViewById(R.id.todo_fragment_add_item_higher_setting)
        popRela = dialogView.findViewById(R.id.todo_add_item_pop_background)
    }

//    private fun showKeyboard(view: View) {
//        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
//    }
//
//    private fun hideKeyboard(view: View) {
//        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    // 隐藏软键盘
    fun hideSoftInput(view: View?) {
        if (view != null) {
            val manager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // 显示软键盘
    fun showSoftInput( view: View?) {
        if (view != null) {
            val manager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(view, 0)
        }
    }
}
