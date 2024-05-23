package net.onest.time.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import net.onest.time.R;
import net.onest.time.adapter.todo.TodoItemAdapter;
import net.onest.time.adapter.todo.TodoItemAdapterNew;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.AddTaskDialog;
import net.onest.time.components.holder.AdapterHolder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoFragment extends Fragment implements TodoItemAdapter.OnItemClickListener,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {
    private CalendarLayout calendarLayout;
    private CalendarView calendarView;
    private RecyclerView recyclerView;//待办事项
    //    private RecyclerViewEmptySupport recyclerView;
    private Button todoBtn;//添加按钮
    private TextView todayTxt;
    //    private List<Item> itemList = new ArrayList<>();//待办事项数据源
    private List<TaskVo> itemListByDay = new ArrayList<>();//待办事项数据源

    private LinearLayout nullPage;


    private TodoItemAdapter todoItemAdapter;
    private TodoItemAdapterNew todoItemAdapterNew;
//    private RelativeLayout popRela;
//
    private CardView cardView;


    //onCreateView()方法是用于创建Fragment的视图，并返回该视图对象
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_fragment, container, false);
        return view;
    }

    //onViewCreated()方法是在视图创建完毕后被调用，允许你对视图进行初始化和操作。
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(view);

        todayTxt.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");

        //按钮的监听事件
        btnClickIncidents();


        LocalDateTime currentDateTime = LocalDateTime.now();
        // 从今天起 往上推一天
        LocalDateTime yesterdays = currentDateTime.minusDays(1);
        // 从今天起 往上推一周
        LocalDateTime weeks = currentDateTime.minusWeeks(1).plusDays(1);
        // 从今天起 往上推一个月
        LocalDateTime months = currentDateTime.minusMonths(1).plusDays(1);

        long todayEpochMill = currentDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long yesterdayEpochMilli = yesterdays.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000;
        long weekEpochMilli = weeks.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000;
        long monthEpochMilli = months.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000;
//        System.out.println("从今天起 往上推一天 " + yesterdayEpochMilli);
//        System.out.println("从今天起 往上推一周 " + weekEpochMilli);
//        System.out.println("从今天起 往上推一个月 " + monthEpochMilli);

//// 计算当天的时间戳
//        long currentDayTimeMillis = currentTimeMillis - currentDayStartTimeMillis;


        itemListByDay = TaskApi.findByDay(todayEpochMill);

        //《标记》日期:
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();


        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month + 1, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month + 1, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView.setSchemeDate(map);


//        recyclerView.setEmptyView(View.inflate(getContext(),R.layout.null_item_page,null));
//        if(itemListByDay.isEmpty()){
//            recyclerView.setVisibility(View.GONE);
//            nullPage.setVisibility(View.VISIBLE);
//        }else{
//            recyclerView.setVisibility(View.VISIBLE);
//            nullPage.setVisibility(View.GONE);
//        }
        //绑定适配器:
        todoItemAdapter = new TodoItemAdapter(getContext(), itemListByDay);
        recyclerView.setAdapter(todoItemAdapter);
        todoItemAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        //绑定新的适配器:
//        todoItemAdapterNew = new TodoItemAdapterNew(R.layout.re_item,itemListByDay);
//        todoItemAdapterNew.setEmptyView(R.layout.null_item_page);
//        recyclerView.setAdapter(todoItemAdapterNew);
//        recyclerView.setLayoutManager(layoutManager);

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
//        Calendar calendar = new Calendar();
//        calendar.setYear(year);
//        calendar.setMonth(month);
//        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
//        return calendar;
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);

        return calendar;
    }

    private void btnClickIncidents() {
        //添加待办：
        todoBtn.setOnClickListener(v -> new AddTaskDialog(
                TodoFragment.this.getContext(),
                itemListByDay,
                new AdapterHolder(todoItemAdapter)
        ));

        //日历选中事件
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                itemListByDay = TaskApi.findByDay(calendar.getTimeInMillis());
//                if(itemListByDay.isEmpty()){
//                    recyclerView.setVisibility(View.GONE);
//                    nullPage.setVisibility(View.VISIBLE);
//                }else{
//                    recyclerView.setVisibility(View.VISIBLE);
//                    nullPage.setVisibility(View.GONE);
//                }
                todoItemAdapter.setItemListByDay(itemListByDay);
                todoItemAdapter.notifyDataSetChanged();
//                todoItemAdapterNew.setDiffNewData(itemListByDay);
//                todoItemAdapterNew.notifyDataSetChanged();

            }
        });
    }

    private void findView(View view) {
        calendarLayout = view.findViewById(R.id.cancel_button);
        calendarView = view.findViewById(R.id.calendarView);
//        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView = view.findViewById(R.id.my_recyclerView);
        todoBtn = view.findViewById(R.id.todo_btn);
        todayTxt = view.findViewById(R.id.todo_fragment_today);
        nullPage = view.findViewById(R.id.null_item_page);
    }


    @Override
    public void onItemClick(int position) {
        //点击事项进行编辑:
        Toast.makeText(getContext(), "你点击了" + itemListByDay.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
    }

    @Override
    public void onYearChange(int year) {

    }
}
