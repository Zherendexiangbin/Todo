package net.onest.time.components

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.api.TaskApi
import net.onest.time.api.dto.TaskDto
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.showToast
import java.util.*

/**
 * 同AddTaskDialog，但有更多选项
 */
class AddTaskMoreDialog(
        private val context: Context,
        private val categoryId: Long,
        private val tasks: MutableList<TaskVo>,
        private val adapter: AdapterHolder
) : AlertDialog(context) {
    //以下是弹框布局控件：
    private var addYes: Button? = null
    private var addNo: Button? = null
    private var itemNameAbout: Button? = null
    private var goalWorkload: EditText? = null
    private var habitWorkload: EditText? = null
    private var itemName: TextInputEditText? = null
    private var todoWant: RadioGroup? = null
    private var todoSetTime: RadioGroup? = null
    private var setTimeGroup: RadioGroup? = null
    private var wantOne: RadioButton? = null
    private var wantTwo: RadioButton? = null
    private var wantThree: RadioButton? = null
    private var setTimeOne: RadioButton? = null
    private var setTimeTwo: RadioButton? = null
    private var setTimeThree: RadioButton? = null
    private var setTimeGroupOne: RadioButton? = null
    private var setTimeGroupTwo: RadioButton? = null
    private var setTimeGroupThree: RadioButton? = null
    private var goalDate: TextView? = null
    private var setTimeOneTxt: TextView? = null
    private var setTimeTwoTxt: TextView? = null
    private var setTimeThreeTxt: TextView? = null
    private var higherSet: TextView? = null
    private var goalUnits: Spinner? = null
    private var habitDateUnits: Spinner? = null
    private var habitTimeUnits: Spinner? = null
    private var goalLinear: LinearLayout? = null
    private var habitLinear: LinearLayout? = null

    private val task = TaskDto().withDefault()

    private fun setListeners() {
        higherSet!!.setOnClickListener {
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
            val clockAbout = dialog.findViewById<Button>(R.id.todo_clock_times_about)
            val btnYes = dialog.findViewById<Button>(R.id.add_todo_higher_setting_item_yes)
            val btnNo = dialog.findViewById<Button>(R.id.add_todo_higher_setting_item_no)
            clockAbout.setOnClickListener {
                XPopup.Builder(context)
                        .asConfirm("什么是单次循环次数", """
     举例:
     小明每次学习想学75分钟，但是75分钟太长学的太累，那么可以设定一个番茄钟的时间为25分钟，单次预期循环次数为3次。
     这样的番茄钟就会按照:
     学习25分钟-休息-学习25分钟-休息-学习25分钟-休息(共循环三次)
     来执行
     """.trimIndent(),
                                "关闭", "确认",
                                { Toast.makeText(context, "click", Toast.LENGTH_SHORT) }, null, false, R.layout.my_confim_popup) //绑定已有布局
                        .show()
            }
            btnYes.setOnClickListener { dialog.dismiss() }
            btnNo.setOnClickListener { dialog.dismiss() }
        }
        itemNameAbout!!.setOnClickListener {
            XPopup.Builder(context)
                    .asConfirm("什么是番茄钟", """
     1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。
     2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。
     3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦
     """.trimIndent(),
                            "关闭", "番茄钟牛逼",
                            { Toast.makeText(context, "click", Toast.LENGTH_SHORT) }, null, false, R.layout.my_confim_popup) //绑定已有布局
                    .show()
        }
        todoWant!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.want_one -> {
                    //普通番茄钟
                    goalLinear!!.visibility = View.GONE
                    habitLinear!!.visibility = View.GONE
                }
                R.id.want_two -> {
                    //定目标
                    goalLinear!!.visibility = View.VISIBLE
                    habitLinear!!.visibility = View.GONE
                }
                R.id.want_three -> {
                    //养习惯
                    goalLinear!!.visibility = View.GONE
                    habitLinear!!.visibility = View.VISIBLE
                }
            }
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

        //定目标之"设置日期"：
        goalDate!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mDay = calendar[Calendar.DAY_OF_MONTH]
            DatePickerDialog(context, onDateSetListener, mYear, mMonth, mDay).show()
        }
        setTimeGroupThree!!.setOnClickListener {
            Toast.makeText(context, setTimeGroupThree!!.text.toString() + "", Toast.LENGTH_SHORT).show()
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
                    task.taskName = itemName!!.getText().toString().trim()
                    task.type = 1
                }

                // 不计时
                R.id.set_time_three -> {
                    task.taskName = itemName!!.getText().toString().trim()
                    task.type = 2
                }
            }

            task.categoryId = categoryId

            val taskVo = TaskApi.addTask(task)
            (tasks as MutableList<TaskVo>).add(taskVo)
            adapter.notifyItemChanged(tasks.indexOf(taskVo))

            dismiss()
        }

//        addYes!!.setOnClickListener {
//            if ("" == itemName!!.text.toString() && itemName!!.text.toString().isEmpty()) {
//                Toast.makeText(context, "请输入Item名称", Toast.LENGTH_SHORT).show()
//            } else {
//                //普通的番茄时钟:
//                if (wantOne!!.isChecked) {
//                    //倒计时
////                                int countDownTimer=0;
//                    if (setTimeOne!!.isChecked) {
//                        if (setTimeGroupOne!!.isChecked) {
//                            val strings = setTimeGroupOne!!.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//                            val estimate = ArrayList<Int>()
//                            if (map["clockTimes"] == null) {
//                                map["clockTimes"] = "1"
//                            }
//                            estimate.add(Integer.valueOf(map["clockTimes"]))
//                            val taskDto = TaskDto()
//                            taskDto.taskName = itemName!!.text.toString()
//                            taskDto.estimate = estimate
//                            taskDto.clockDuration = Integer.valueOf(strings.trim { it <= ' ' })
//                            taskDto.remark = map["remark"]
//                            if (map["rest"] == null) {
//                                map["rest"] = "5"
//                            }
//                            taskDto.restTime = Integer.valueOf(map["rest"])
//                            taskDto.again = 1
//                            //                                taskDto.setCategoryId(category);
//                            //Todo
//                            taskDto.categoryId = 9L
//                            val taskVo = TaskApi.addTask(taskDto)
//                            tasks.add(taskVo)
//                            //                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
//                            adapter.notifyDataSetChanged()
//                        } else if (setTimeGroupTwo!!.isChecked) {
//                            val strings = setTimeGroupOne!!.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//                            val estimate = ArrayList<Int>()
//                            if (map["clockTimes"] == null) {
//                                map["clockTimes"] = "1"
//                            }
//                            estimate.add(Integer.valueOf(map["clockTimes"]))
//                            val taskDto = TaskDto()
//                            taskDto.taskName = itemName!!.text.toString()
//                            taskDto.estimate = estimate
//                            taskDto.clockDuration = Integer.valueOf(strings.trim { it <= ' ' })
//                            taskDto.remark = map["remark"]
//                            if (map["rest"] == null) {
//                                map["rest"] = "5"
//                            }
//                            taskDto.restTime = Integer.valueOf(map["rest"])
//                            taskDto.again = 1
//                            //                                taskDto.setCategory(category);
//                            //Todo
//                            taskDto.categoryId = 9L
//                            val taskVo = TaskApi.addTask(taskDto)
//                            tasks.add(taskVo)
//                            //                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
//                            adapter.notifyDataSetChanged()
//                        } else {
//                            val strings = setTimeGroupOne!!.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//                            val estimate = ArrayList<Int>()
//                            if (map["clockTimes"] == null) {
//                                map["clockTimes"] = "1"
//                            }
//                            estimate.add(Integer.valueOf(map["clockTimes"]))
//                            val taskDto = TaskDto()
//                            taskDto.taskName = itemName!!.text.toString()
//                            taskDto.estimate = estimate
//                            taskDto.clockDuration = Integer.valueOf(strings.trim { it <= ' ' })
//                            taskDto.remark = map["remark"]
//                            if (map["rest"] == null) {
//                                map["rest"] = "5"
//                            }
//                            taskDto.restTime = Integer.valueOf(map["rest"])
//                            taskDto.again = 1
//                            //                                taskDto.setCategory(category);
//                            //Todo
//                            taskDto.categoryId = 9L
//                            val taskVo = TaskApi.addTask(taskDto)
//                            tasks.add(taskVo)
//                            //                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                    //正向计时：
//                    if (setTimeTwo!!.isChecked) {
////                                    int forwardTimer = 1;
////                                    TaskVo item = new Item();
////                                    item.setTaskName(itemName.getText().toString());
////                                    item.setClockDuration("正向计时");
////                                    parentItemList.get(groupPosition).getChildItemList().add(item);
////                                    notifyDataSetChanged();
//                    }
//                    //不计时：
//                    if (setTimeThree!!.isChecked) {
////                                    int noTimer = 2;
////                                    Item item = new Item();
////                                    item.setItemName(itemName.getText().toString());
////                                    item.setTime("普通待办");
////                                    parentItemList.get(groupPosition).getChildItemList().add(item);
////                                    notifyDataSetChanged();
//                    }
//                }
//                //定目标
//                //养习惯
//                dismiss()
//            }
//        }
        addNo!!.setOnClickListener { dismiss() }
    }

    private fun getViews(dialogView: View) {
        //以下是弹窗控件：
        addYes = dialogView.findViewById(R.id.add_todo_item_yes)
        addNo = dialogView.findViewById(R.id.add_todo_item_no)
        itemNameAbout = dialogView.findViewById(R.id.todo_item_about)
        itemName = dialogView.findViewById(R.id.todo_item_name)
        goalWorkload = dialogView.findViewById(R.id.goal_workload)
        habitWorkload = dialogView.findViewById(R.id.habit_workload)
        todoWant = dialogView.findViewById(R.id.todo_item_want)
        todoSetTime = dialogView.findViewById(R.id.todo_item_set_time)
        setTimeGroup = dialogView.findViewById(R.id.set_time_one_group)
        wantOne = dialogView.findViewById(R.id.want_one)
        wantTwo = dialogView.findViewById(R.id.want_two)
        wantThree = dialogView.findViewById(R.id.want_three)
        setTimeOne = dialogView.findViewById(R.id.set_time_one)
        setTimeTwo = dialogView.findViewById(R.id.set_time_two)
        setTimeThree = dialogView.findViewById(R.id.set_time_three)
        setTimeGroupOne = dialogView.findViewById(R.id.set_time_one_group_one)
        setTimeGroupTwo = dialogView.findViewById(R.id.set_time_one_group_two)
        setTimeGroupThree = dialogView.findViewById(R.id.set_time_one_group_three)
        goalDate = dialogView.findViewById(R.id.goal_date)
        setTimeOneTxt = dialogView.findViewById(R.id.set_time_one_txt)
        setTimeTwoTxt = dialogView.findViewById(R.id.set_time_two_txt)
        setTimeThreeTxt = dialogView.findViewById(R.id.set_time_three_txt)
        higherSet = dialogView.findViewById(R.id.list_fragment_add_item_higher_setting)
        goalUnits = dialogView.findViewById(R.id.goal_units)
        habitDateUnits = dialogView.findViewById(R.id.habit_date_units)
        habitTimeUnits = dialogView.findViewById(R.id.habit_time_units)
        goalLinear = dialogView.findViewById(R.id.todo_item_goal)
        habitLinear = dialogView.findViewById(R.id.todo_item_habit)
    }

    /**
     * 日期选择器对话框监听
     */
    private val onDateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val days: String
        days = if (monthOfYear + 1 < 10) {
            if (dayOfMonth < 10) {
                StringBuffer().append(year).append("年").append("0").append(monthOfYear + 1).append("月").append("0").append(dayOfMonth).append("日").toString()
            } else {
                StringBuffer().append(year).append("年").append("0").append(monthOfYear + 1).append("月").append(dayOfMonth).append("日").toString()
            }
        } else {
            if (dayOfMonth < 10) {
                StringBuffer().append(year).append("年").append(monthOfYear + 1).append("月").append("0").append(dayOfMonth).append("日").toString()
            } else {
                StringBuffer().append(year).append("年").append(monthOfYear + 1).append("月").append(dayOfMonth).append("日").toString()
            }
        }
        goalDate!!.text = days
    }

    init {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.list_fragment_add_expandable_child_item_pop_window, null)
        getViews(dialogView) //获取控件
        show()
        window!!.setContentView(dialogView)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTimeTwoTxt!!.visibility = View.GONE
        setTimeThreeTxt!!.visibility = View.GONE
        goalLinear!!.visibility = View.GONE
        habitLinear!!.visibility = View.GONE
        task.categoryId=categoryId
        setListeners()
    }
}