package net.onest.time.navigation.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.CalendarView.OnCalendarSelectListener
import net.onest.time.R
import net.onest.time.adapter.todo.TodoItemAdapter
import net.onest.time.api.TaskApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskDialog
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.DateUtil
import net.onest.time.utils.showToast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TodoFragment : Fragment() {
    private var calendarLayout: CalendarLayout? = null
    private var calendarView: CalendarView? = null
    private var recyclerView: RecyclerView? = null //待办事项
    private var todoBtn: Button? = null //添加按钮
    private var todayTxt: TextView? = null
    private lateinit var tasks: MutableList<TaskVo> //待办事项数据源
    private lateinit var dayTaskMap: MutableMap<Long, MutableList<TaskVo>>
    private var nullPage: LinearLayout? = null

    private var todoItemAdapter: TodoItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            dayTaskMap = TaskApi.getTaskDay(DateUtil.epochMillisecond())
        } catch (e: Exception) {
            e.message?.showToast()
        }
    }

    /**
     * onCreateView()方法是用于创建Fragment的视图，并返回该视图对象
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.to_do_fragment, container, false)
    }

    /**
     * onViewCreated()方法是在视图创建完毕后被调用，允许你对视图进行初始化和操作。
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView(view)
        setListeners()

        // 设置日期文字
        todayTxt!!.text = "${calendarView?.curYear}年 ${calendarView?.curMonth}月 ${calendarView?.curDay}日"

        val map: MutableMap<String, Calendar> = HashMap()
        dayTaskMap.entries.forEach {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.key), ZoneId.of("UTC+8"))
            map[getSchemeCalendar(date.year, date.monthValue, date.dayOfMonth, Color.parseColor("#FFA500"), it.value.size.toString()).toString()] =
                getSchemeCalendar(date.year, date.monthValue, date.dayOfMonth, Color.parseColor("#FFA500"), it.value.size.toString())
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView!!.setSchemeDate(map)

        //绑定适配器:
        tasks = dayTaskMap[DateUtil.epochMillisecond()] ?: ArrayList()
        tasks.sortWith(TaskVo.comparator())
        todoItemAdapter = TodoItemAdapter(requireContext(), tasks)
        recyclerView?.adapter = todoItemAdapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text

        return calendar
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        // 切换月份事件
        calendarView?.setOnMonthChangeListener { year, month ->
            try {
                dayTaskMap = TaskApi.getTaskDay(DateUtil.epochMillisecond(year, month))
                val map: MutableMap<String, Calendar> = HashMap()
                dayTaskMap.entries.forEach {
                    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.key), ZoneId.of("UTC+8"))
                    map[getSchemeCalendar(date.year, date.monthValue, date.dayOfMonth, Color.parseColor("#FFA500"), it.value.size.toString()).toString()] =
                        getSchemeCalendar(date.year, date.monthValue, date.dayOfMonth, Color.parseColor("#FFA500"), it.value.size.toString())
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                calendarView!!.setSchemeDate(map)
            } catch (e: Exception) {
                e.message?.showToast()
            }
        }

        //添加待办：
        todoBtn!!.setOnClickListener { v: View? ->
            AddTaskDialog(
                requireContext(),
                tasks,
                AdapterHolder(todoItemAdapter)
            )

            // 回到当前日期
            calendarView?.scrollToCurrent()
        }

        //日历选中事件
        calendarView!!.setOnCalendarSelectListener(object : OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
                todayTxt?.text = "${calendar.year}年 ${calendar.month}月 ${calendar.day}日"

                tasks = dayTaskMap[DateUtil.epochMillisecond(calendar.timeInMillis)] ?: ArrayList()
                tasks.sortWith(TaskVo.comparator())
                todoItemAdapter?.itemListByDay = tasks
                todoItemAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun findView(view: View) {
        calendarLayout = view.findViewById(R.id.cancel_button)
        calendarView = view.findViewById(R.id.calendarView)
        recyclerView = view.findViewById(R.id.my_recyclerView)
        todoBtn = view.findViewById(R.id.todo_btn)
        todayTxt = view.findViewById(R.id.todo_fragment_today)
        nullPage = view.findViewById(R.id.null_item_page)
    }
}
