package net.onest.time.navigation.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import net.onest.time.R;
import net.onest.time.RecyclerViewEmptySupport;
import net.onest.time.adapter.todo.TodoItemAdapter;
import net.onest.time.adapter.todo.TodoItemAdapterNew;
import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.entity.Item;
import net.onest.time.utils.ColorUtil;
import net.onest.time.utils.DrawableUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoFragment extends Fragment implements TodoItemAdapter.OnItemClickListener,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener{
    private CalendarLayout calendarLayout;
    private CalendarView calendarView;
    private RecyclerView recyclerView;//待办事项
//    private RecyclerViewEmptySupport recyclerView;
    private Button todoBtn;//添加按钮
    private TextView todayTxt;
//    private List<Item> itemList = new ArrayList<>();//待办事项数据源
    private List<TaskVo> itemListByDay  = new ArrayList<>();//待办事项数据源

    private LinearLayout nullPage;


    //以下是弹框布局控件：
    private Button addYes,addNo,itemNameAbout,relaChange;
    private TextInputEditText itemName;
    private RadioGroup todoSetTime,setTimeGroup;

    private RadioButton setTimeOne,setTimeTwo,setTimeThree;
    private RadioButton setTimeGroupOne,setTimeGroupTwo,setTimeGroupThree;
    private TextView setTimeOneTxt,setTimeTwoTxt,setTimeThreeTxt,higherSet;
    private TodoItemAdapter todoItemAdapter;
    private TodoItemAdapterNew todoItemAdapterNew;
    private RelativeLayout popRela;

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

        todayTxt.setText(calendarView.getCurYear()+"年"+calendarView.getCurMonth()+"月"+calendarView.getCurDay()+"日");

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
        long yesterdayEpochMilli = yesterdays.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8*60*60*1000;
        long weekEpochMilli = weeks.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8*60*60*1000;
        long monthEpochMilli = months.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8*60*60*1000;
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
        map.put(getSchemeCalendar(year, month+1, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month+1, 3, 0xFF40db25, "假"));
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
        todoItemAdapter = new TodoItemAdapter(getContext(),itemListByDay);
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
        //添加待办:
        todoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置弹窗：
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.todo_fragment_add_item_pop_window,null);
                getViews(dialogView);//获取控件
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                relaChange.setVisibility(View.GONE);
                setTimeTwoTxt.setVisibility(View.GONE);
                setTimeThreeTxt.setVisibility(View.GONE);

//                List<String> integerList = new ArrayList<>();
                HashMap<String,String> map = new HashMap<String,String>();

                higherSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialogStyle);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting,null);
                        final Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setContentView(dialogView);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        EditText remark = dialogView.findViewById(R.id.todo_fragment_add_higher_remark);
                        EditText clockTimes = dialogView.findViewById(R.id.todo_fragment_add_clock_times);
                        EditText rest = dialogView.findViewById(R.id.todo_fragment_add_rest_time);
                        CheckBox checkBox = dialogView.findViewById(R.id.todo_fragment_add_higher_again);
                        Button clockAbout = dialogView.findViewById(R.id.todo_clock_times_about);
                        Button btnYes = dialogView.findViewById(R.id.add_todo_higher_setting_item_yes);
                        Button btnNo = dialogView.findViewById(R.id.add_todo_higher_setting_item_no);


                        clockAbout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new XPopup.Builder(getContext())
                                        .asConfirm("什么是单次循环次数", "举例:\n" +
                                                        "小明每次学习想学75分钟，但是75分钟太长学的太累，那么可以设定一个番茄钟的时间为25分钟，单次预期循环次数为3次。\n" +
                                                        "这样的番茄钟就会按照:\n" +
                                                        "学习25分钟-休息-学习25分钟-休息-学习25分钟-休息(共循环三次)\n" +
                                                        "来执行",
                                                "关闭", "确认",
                                                new OnConfirmListener() {
                                                    @Override
                                                    public void onConfirm() {
                                                        Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT);
                                                    }
                                                }, null, false,R.layout.my_confim_popup)//绑定已有布局
                                        .show();
                            }
                        });

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                integerList.add(clockTimes.getText().toString().trim());
                                map.put("remark",remark.getText().toString().trim());
                                map.put("clockTimes",clockTimes.getText().toString().trim());
                                map.put("rest",rest.getText().toString().trim());
                                if(checkBox.isChecked()){
                                    map.put("again","1");
                                }else{
                                    map.put("again","0");
                                }
                                dialog.dismiss();
                            }
                        });

                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                itemNameAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new XPopup.Builder(getContext())
                                .asConfirm("什么是番茄钟", "1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。\n" +
                                                "2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。\n3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦",
                                        "关闭", "番茄钟牛逼",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT);
                                            }
                                        }, null, false,R.layout.my_confim_popup)//绑定已有布局
                                .show();
                    }
                });


                todoSetTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.set_time_one:
                                setTimeGroup.setVisibility(View.VISIBLE);
                                setTimeOneTxt.setVisibility(View.VISIBLE);
                                setTimeTwoTxt.setVisibility(View.GONE);
                                setTimeThreeTxt.setVisibility(View.GONE);
                                break;
                            case R.id.set_time_two:
                                setTimeGroup.setVisibility(View.GONE);
                                setTimeOneTxt.setVisibility(View.GONE);
                                setTimeTwoTxt.setVisibility(View.VISIBLE);
                                setTimeThreeTxt.setVisibility(View.GONE);
                                break;
                            case R.id.set_time_three:
                                setTimeGroup.setVisibility(View.GONE);
                                setTimeOneTxt.setVisibility(View.GONE);
                                setTimeTwoTxt.setVisibility(View.GONE);
                                setTimeThreeTxt.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                });


                setTimeGroupThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), setTimeGroupThree.getText().toString()+"", Toast.LENGTH_SHORT).show();
                        new XPopup.Builder(getContext()).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        setTimeGroupThree.setText(text+" 分钟");
                                    }
                                }).show();
                    }
                });

                addYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("".equals(itemName.getText().toString()) && itemName.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "请输入Item名称", Toast.LENGTH_SHORT).show();
                        }else{
                            if(setTimeOne.isChecked()){
                                if(setTimeGroupOne.isChecked()){
                                    String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                                    ArrayList<Integer> estimate = new ArrayList<>();
                                    if(map.get("clockTimes")==null){
                                        map.put("clockTimes","1");
                                    }
                                    estimate.add(Integer.valueOf(map.get("clockTimes")));
                                    TaskDto taskDto = new TaskDto();
                                    taskDto.setTaskName(itemName.getText().toString());
                                    taskDto.setEstimate(estimate);
                                    taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                    taskDto.setRemark(map.get("remark"));
                                    if(map.get("rest") == null){
                                        map.put("rest","5");
                                    }
                                    taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                    taskDto.setAgain(1);
                                    TaskVo taskVo = TaskApi.addTask(taskDto);
                                    itemListByDay.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                    todoItemAdapter.notifyDataSetChanged();
                                } else if (setTimeGroupTwo.isChecked()) {
                                    String strings = setTimeGroupTwo.getText().toString().split(" ")[0];

                                    ArrayList<Integer> estimate = new ArrayList<>();
                                    if(map.get("clockTimes")==null){
                                        map.put("clockTimes","1");
                                    }
                                    estimate.add(Integer.valueOf(map.get("clockTimes")));
                                    TaskDto taskDto = new TaskDto();
                                    taskDto.setTaskName(itemName.getText().toString());
                                    taskDto.setEstimate(estimate);
                                    taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                    taskDto.setRemark(map.get("remark"));
                                    if(map.get("rest") == null){
                                        map.put("rest","5");
                                    }
                                    taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                    taskDto.setAgain(1);
                                    TaskVo taskVo = TaskApi.addTask(taskDto);
                                    itemListByDay.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                    todoItemAdapter.notifyDataSetChanged();
                                }else{
                                    String strings = setTimeGroupThree.getText().toString().split(" ")[0];

                                    ArrayList<Integer> estimate = new ArrayList<>();
                                    if(map.get("clockTimes")==null){
                                        map.put("clockTimes","1");
                                    }
                                    estimate.add(Integer.valueOf(map.get("clockTimes")));
                                    TaskDto taskDto = new TaskDto();
                                    taskDto.setTaskName(itemName.getText().toString());
                                    taskDto.setEstimate(estimate);
                                    taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                    taskDto.setRemark(map.get("remark"));
                                    if(map.get("rest") == null){
                                        map.put("rest","5");
                                    }
                                    taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                    taskDto.setAgain(1);
                                    TaskVo taskVo = TaskApi.addTask(taskDto);
                                    itemListByDay.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                    todoItemAdapter.notifyDataSetChanged();
                                }
                            }
                            //正向计时：
                            if(setTimeTwo.isChecked()){
//                                    int forwardTimer = 1;
//                                Item item = new Item();
//                                item.setItemName(itemName.getText().toString());
//                                item.setTime("正向计时");
//                                itemListByDay.add(item);
//                                todoItemAdapter.notifyDataSetChanged();

                            }
                            //不计时：
                            if(setTimeThree.isChecked()){
//                                    int noTimer = 2;
//                                Item item = new Item();
//                                item.setItemName(itemName.getText().toString());
//                                item.setTime("普通待办");
//                                itemListByDay.add(item);
//                                todoItemAdapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        }
                    }
                });
                addNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
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

    private void getViews(View dialogView) {
        //以下是弹窗控件：
        addYes = dialogView.findViewById(R.id.add_todo_item_yes);
        addNo = dialogView.findViewById(R.id.add_todo_item_no);
        itemNameAbout = dialogView.findViewById(R.id.todo_item_about);
        relaChange = dialogView.findViewById(R.id.add_todo_item_change);

        itemName = dialogView.findViewById(R.id.todo_item_name);

        todoSetTime = dialogView.findViewById(R.id.todo_item_set_time);
        setTimeGroup = dialogView.findViewById(R.id.set_time_one_group);


        setTimeOne = dialogView.findViewById(R.id.set_time_one);
        setTimeTwo = dialogView.findViewById(R.id.set_time_two);
        setTimeThree = dialogView.findViewById(R.id.set_time_three);

        setTimeGroupOne = dialogView.findViewById(R.id.set_time_one_group_one);
        setTimeGroupTwo = dialogView.findViewById(R.id.set_time_one_group_two);
        setTimeGroupThree = dialogView.findViewById(R.id.set_time_one_group_three);

        setTimeOneTxt = dialogView.findViewById(R.id.set_time_one_txt);
        setTimeTwoTxt = dialogView.findViewById(R.id.set_time_two_txt);
        setTimeThreeTxt = dialogView.findViewById(R.id.set_time_three_txt);

        higherSet = dialogView.findViewById(R.id.todo_fragment_add_item_higher_setting);
        popRela = dialogView.findViewById(R.id.todo_add_item_pop_background);
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
        Toast.makeText(getContext(), "你点击了"+itemListByDay.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
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
