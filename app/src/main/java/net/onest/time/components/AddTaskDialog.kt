package net.onest.time.components

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.textfield.TextInputEditText
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.api.TaskApi
import net.onest.time.api.dto.TaskDto
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.doSpinCircleAnimation
import net.onest.time.utils.showToast
import net.onest.time.utils.withCustomAlphaAnimation

/**
 * 添加任务 弹窗
 */
class AddTaskDialog(
    private val context: Context,
    private val tasks: List<TaskVo>,
    private val adapter: AdapterHolder
) : AlertDialog(context) {
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

    private lateinit var rootLayout: LinearLayout

    private val task = TaskDto().withDefault()

    init {
        val view = LayoutInflater.from(getContext())
            .inflate(R.layout.todo_fragment_add_item_pop_window, null)
            .withCustomAlphaAnimation()

        getViews(view) //获取控件
        setListeners()

        show()

        window!!.setContentView(view)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        relaChange!!.visibility = View.GONE
        setTimeTwoTxt!!.visibility = View.GONE
        setTimeThreeTxt!!.visibility = View.GONE

//        // 文本框获取焦点
//        itemName?.requestFocus()

        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }

    private fun setListeners() {
        // 取消焦点
        rootLayout.setOnClickListener {
            itemName?.clearFocus()
            hideKeyboard(itemName!!)
        }

        higherSet!!.setOnClickListener { view: View? ->
            HigherSetDialog(context, task)
        }

        // 什么是番茄钟
        itemNameAbout!!.setOnClickListener {
            TipDialog(
                context,
                "什么是番茄钟",
                """
                1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。
                2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。
                3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦
                """.trimIndent()
            )
                .show()
        }

        itemName?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            } else {
                hideKeyboard(v)
            }
        }

        // 计时方式
        todoSetTime!!.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
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

        // 番茄钟时长
        setTimeGroupThree!!.setOnClickListener {
            setTimeGroupThree?.text.toString().showToast()

            XPopup.Builder(context)
                .asInputConfirm(
                    "自定义番茄钟时间",
                    "输入倒计时分钟数:" ) {
                    text -> setTimeGroupThree!!.text = "$text 分钟"
                }
                .show()
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
                    task.taskName = itemName!!.getText().toString().trim()
                    task.type = 1
                }

                // 不计时
                R.id.set_time_three -> {
                    task.taskName = itemName!!.getText().toString().trim()
                    task.type = 2
                }
            }


            val taskVo = TaskApi.addTask(task)
            (tasks as MutableList<TaskVo>).add(taskVo)
            adapter.notifyItemChanged(tasks.indexOf(taskVo))
//            adapter.notifyDataSetChanged()
            dismiss()
        }

        addNo!!.setOnClickListener { dismiss() }
    }

    private fun getViews(view: View) {
        //以下是弹窗控件：
        addYes = view.findViewById(R.id.add_todo_item_yes)
        addNo = view.findViewById(R.id.add_todo_item_no)
        itemNameAbout = view.findViewById(R.id.todo_item_about)
        relaChange = view.findViewById(R.id.add_todo_item_change)

        itemName = view.findViewById(R.id.todo_item_name)

        todoSetTime = view.findViewById(R.id.todo_item_set_time)
        setTimeGroup = view.findViewById(R.id.set_time_one_group)


        setTimeOne = view.findViewById(R.id.set_time_one)
        setTimeTwo = view.findViewById(R.id.set_time_two)
        setTimeThree = view.findViewById(R.id.set_time_three)

        setTimeGroupOne = view.findViewById(R.id.set_time_one_group_one)
        setTimeGroupTwo = view.findViewById(R.id.set_time_one_group_two)
        setTimeGroupThree = view.findViewById(R.id.set_time_one_group_three)

        setTimeOneTxt = view.findViewById(R.id.set_time_one_txt)
        setTimeTwoTxt = view.findViewById(R.id.set_time_two_txt)
        setTimeThreeTxt = view.findViewById(R.id.set_time_three_txt)

        higherSet = view.findViewById(R.id.todo_fragment_add_item_higher_setting)
        popRela = view.findViewById(R.id.todo_add_item_pop_background)

        rootLayout = view.findViewById<LinearLayout>(R.id.root_layout)
    }

    private fun showKeyboard(view: View) {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setTaskName(taskName: String) {
        itemName?.setText(taskName)
    }
}
