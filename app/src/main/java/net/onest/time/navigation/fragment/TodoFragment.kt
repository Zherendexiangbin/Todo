package net.onest.time.navigation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.CalendarView.OnCalendarSelectListener
import net.onest.time.R
import net.onest.time.adapter.todo.TodoItemAdapter
import net.onest.time.adapter.todo.TodoItemAdapterNew
import net.onest.time.api.TaskApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskDialog
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.utils.DateUtil
import net.onest.time.utils.showToast
import java.time.LocalDateTime
import java.time.ZoneOffset

class TodoFragment : Fragment() {
    private var calendarLayout: CalendarLayout? = null
    private var calendarView: CalendarView? = null
    private var recyclerView: RecyclerView? = null //待办事项
    private var todoBtn: Button? = null //添加按钮
    private var todayTxt: TextView? = null
    private var itemListByDay: MutableList<TaskVo> = ArrayList() //待办事项数据源
    private var nullPage: LinearLayout? = null

    private var todoItemAdapter: TodoItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            itemListByDay = TaskApi.findByDay(DateUtil.epochMillisecond())
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
        todayTxt!!.text =
            calendarView!!.curYear.toString() + "年" + calendarView!!.curMonth + "月" + calendarView!!.curDay + "日"

        val currentDateTime = LocalDateTime.now()
//        // 从今天起 往上推一天
//        val yesterdays = currentDateTime.minusDays(1)
//        // 从今天起 往上推一周
//        val weeks = currentDateTime.minusWeeks(1).plusDays(1)
//        // 从今天起 往上推一个月
//        val months = currentDateTime.minusMonths(1).plusDays(1)

        val todayEpochMill = currentDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()
//        val yesterdayEpochMilli =
//            yesterdays.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000
//        val weekEpochMilli =
//            weeks.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000
//        val monthEpochMilli =
//            months.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000


        //        System.out.println("从今天起 往上推一天 " + yesterdayEpochMilli);
//        System.out.println("从今天起 往上推一周 " + weekEpochMilli);
//        System.out.println("从今天起 往上推一个月 " + monthEpochMilli);

//// 计算当天的时间戳
//        long currentDayTimeMillis = currentTimeMillis - currentDayStartTimeMillis;
//        try {
//            itemListByDay = TaskApi.findByDay(todayEpochMill)
//        } catch (e: RuntimeException) {
//            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
//        }

        //《标记》日期:
        val year = calendarView!!.curYear
        val month = calendarView!!.curMonth


        val map: MutableMap<String, Calendar> = HashMap()
        map[getSchemeCalendar(year, month + 1, 3, -0xbf24db, "假").toString()] =
            getSchemeCalendar(year, month + 1, 3, -0xbf24db, "假")
        map[getSchemeCalendar(year, month, 6, -0x196ec8, "事").toString()] =
            getSchemeCalendar(year, month, 6, -0x196ec8, "事")
        map[getSchemeCalendar(year, month, 9, -0x20ecaa, "议").toString()] =
            getSchemeCalendar(year, month, 9, -0x20ecaa, "议")
        map[getSchemeCalendar(year, month, 13, -0x123a93, "记").toString()] =
            getSchemeCalendar(year, month, 13, -0x123a93, "记")
        map[getSchemeCalendar(year, month, 14, -0x123a93, "记").toString()] =
            getSchemeCalendar(year, month, 14, -0x123a93, "记")
        map[getSchemeCalendar(year, month, 15, -0x5533bc, "假").toString()] =
            getSchemeCalendar(year, month, 15, -0x5533bc, "假")
        map[getSchemeCalendar(year, month, 18, -0x43ec10, "记").toString()] =
            getSchemeCalendar(year, month, 18, -0x43ec10, "记")
        map[getSchemeCalendar(year, month, 25, -0xec5310, "假").toString()] =
            getSchemeCalendar(year, month, 25, -0xec5310, "假")
        map[getSchemeCalendar(year, month, 27, -0xec5310, "多").toString()] =
            getSchemeCalendar(year, month, 27, -0xec5310, "多")
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView!!.setSchemeDate(map)


        //        recyclerView.setEmptyView(View.inflate(getContext(),R.layout.null_item_page,null));
//        if(itemListByDay.isEmpty()){
//            recyclerView.setVisibility(View.GONE);
//            nullPage.setVisibility(View.VISIBLE);
//        }else{
//            recyclerView.setVisibility(View.VISIBLE);
//            nullPage.setVisibility(View.GONE);
//        }
        //绑定适配器:
        todoItemAdapter = TodoItemAdapter(context, itemListByDay)
        recyclerView!!.adapter = todoItemAdapter
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager

        //        //绑定新的适配器:
//        todoItemAdapterNew = new TodoItemAdapterNew(R.layout.re_item,itemListByDay);
//        todoItemAdapterNew.setEmptyView(R.layout.null_item_page);
//        recyclerView.setAdapter(todoItemAdapterNew);
//        recyclerView.setLayoutManager(layoutManager);
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar {
//        Calendar calendar = new Calendar();
//        calendar.setYear(year);
//        calendar.setMonth(month);
//        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
//        return calendar;
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text

        return calendar
    }

    private fun setListeners() {
        //添加待办：
        todoBtn!!.setOnClickListener { v: View? ->
            calendarView!!.scrollToCurrent()
            AddTaskDialog(
                requireContext(),
                itemListByDay,
                AdapterHolder(todoItemAdapter)
            )
            //如果不是当前日期，就会跳转回来
            val currentCalendar = Calendar()
            currentCalendar.year = calendarView!!.curYear
            currentCalendar.month = calendarView!!.curMonth
            currentCalendar.day = calendarView!!.curDay
            if(calendarView!!.selectedCalendar.compareTo(currentCalendar)!=0){
                calendarView!!.scrollToCurrent()
            }
        }

        //日历选中事件
        calendarView!!.setOnCalendarSelectListener(object : OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar) {
            }

            override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
                val currentCalendar = Calendar()
                currentCalendar.year = calendarView!!.curYear
                currentCalendar.month = calendarView!!.curMonth
                currentCalendar.day = calendarView!!.curDay
                if(calendar.differ(currentCalendar)>0){
                    itemListByDay.clear()
                    todoItemAdapter!!.itemListByDay = itemListByDay
                    todoItemAdapter!!.notifyDataSetChanged()
                }else{
                    try {
                        itemListByDay = TaskApi.findByDay(calendar.timeInMillis)
                    } catch (e: Exception) {
                        e.message?.showToast()
                    }

                    todoItemAdapter!!.itemListByDay = itemListByDay
                    todoItemAdapter!!.notifyDataSetChanged()
                }

//                if(calendar.compareTo(calendar.lunarCalendar)==1){
//                    todoItemAdapter!!.itemListByDay = itemListByDay
//                }else{
//
//                }

                //                if(itemListByDay.isEmpty()){
//                    recyclerView.setVisibility(View.GONE);
//                    nullPage.setVisibility(View.VISIBLE);
//                }else{
//                    recyclerView.setVisibility(View.VISIBLE);
//                    nullPage.setVisibility(View.GONE);
//                }
                //                todoItemAdapterNew.setDiffNewData(itemListByDay);
//                todoItemAdapterNew.notifyDataSetChanged();
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
