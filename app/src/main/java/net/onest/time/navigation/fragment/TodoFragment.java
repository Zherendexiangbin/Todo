package net.onest.time.navigation.fragment;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.lxj.xpopup.interfaces.OnSelectListener;

import net.onest.time.MainActivity;
import net.onest.time.TimerActivity;
import net.onest.time.R;
import net.onest.time.adapter.todo.TodoItemAdapter;
import net.onest.time.entity.Item;

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
    private Button todoBtn;//添加按钮
    private List<Item> itemList = new ArrayList<>();//待办事项数据源

    //以下是弹框布局控件：
    private Button addYes,addNo,itemNameAbout;
    private EditText goalWorkload,habitWorkload;
    private TextInputEditText itemName;
    private RadioGroup todoWant,todoSetTime,setTimeGroup;
    private RadioButton wantOne,wantTwo,wantThree;
    private RadioButton setTimeOne,setTimeTwo,setTimeThree;
    private RadioButton setTimeGroupOne,setTimeGroupTwo,setTimeGroupThree;
    private TextView goalDate,setTimeOneTxt,setTimeTwoTxt,setTimeThreeTxt;
    private Spinner goalUnits,habitDateUnits,habitTimeUnits;
    private LinearLayout goalLinear,habitLinear;
    private TodoItemAdapter todoItemAdapter;


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

        //按钮的监听事件
        btnClickIncidents();

//        calendarView.setSchemeDate();

//        calendarView.setSchemeColor(Color.BLACK,Color.RED,Color.RED);

//        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
//            @Override
//            public void onCalendarOutOfRange(Calendar calendar) {
//
//            }
//
//            @Override
//            public void onCalendarSelect(Calendar calendar, boolean isClick) {
//
//            }
//        });

        //创建数据源：
        for(int i=1; i<=3;i++){
            Item item = new Item();
            item.setItemName("事件 "+i);
            item.setTime(i+"0 分钟");
            itemList.add(item);
        }

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


        //绑定适配器:
        todoItemAdapter = new TodoItemAdapter(getContext(),itemList);
        recyclerView.setAdapter(todoItemAdapter);
        todoItemAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
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
        todoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置弹窗：
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.add_todo_item_pop_window,null);
                getViews(dialogView);//获取控件
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                itemName.requestFocus();
//                MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(itemName,InputMethodManager.SHOW_IMPLICIT);

                setTimeTwoTxt.setVisibility(View.GONE);
                setTimeThreeTxt.setVisibility(View.GONE);
                goalLinear.setVisibility(View.GONE);
                habitLinear.setVisibility(View.GONE);

//                final XPopup.Builder builderPop = new XPopup.Builder(getContext()).watchView(itemNameAbout);
                itemNameAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        builderPop.asAttachList(new String[]{"什么是番茄钟", "番茄钟是全身心工作25分钟，休息5分钟的工作方法。","输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。","点击代办卡片上的开始按钮就可以开始一个番茄钟啦"}, null,
//                                        new OnSelectListener() {
//                                            @Override
//                                            public void onSelect(int position, String text) {
//                                                Toast.makeText(getContext(),"click"+text,Toast.LENGTH_SHORT);
//                                            }
//                                        })
//                                .show();
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

                todoWant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.want_one:
                                //普通番茄钟
                                goalLinear.setVisibility(View.GONE);
                                habitLinear.setVisibility(View.GONE);
                                break;
                            case R.id.want_two:
                                //定目标
                                goalLinear.setVisibility(View.VISIBLE);
                                habitLinear.setVisibility(View.GONE);
                                break;
                            case R.id.want_three:
                                //养习惯
                                goalLinear.setVisibility(View.GONE);
                                habitLinear.setVisibility(View.VISIBLE);
                                break;
                        }
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

                //定目标之"设置日期"：
                goalDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        int mYear = calendar.get(java.util.Calendar.YEAR);
                        int mMonth = calendar.get(java.util.Calendar.MONTH);
                        int mDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(getContext(), onDateSetListener, mYear, mMonth, mDay).show();
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
                            //普通的番茄时钟:
                            if(wantOne.isChecked()){
                                //倒计时
//                                int countDownTimer=0;
                                if(setTimeOne.isChecked()){
                                    if(setTimeGroupOne.isChecked()){
                                        Item item = new Item();
                                        item.setItemName(itemName.getText().toString());
                                        item.setTime(setTimeGroupOne.getText().toString());
                                        itemList.add(item);
                                        todoItemAdapter.notifyDataSetChanged();
                                    } else if (setTimeGroupTwo.isChecked()) {
                                        Item item = new Item();
                                        item.setItemName(itemName.getText().toString());
                                        item.setTime(setTimeGroupTwo.getText().toString());
                                        itemList.add(item);
                                        todoItemAdapter.notifyDataSetChanged();
                                    }else{
                                        Item item = new Item();
                                        item.setItemName(itemName.getText().toString());
                                        item.setTime(setTimeGroupThree.getText().toString());
                                        itemList.add(item);
                                        todoItemAdapter.notifyDataSetChanged();
                                    }
                                }
                                //正向计时：
                                if(setTimeTwo.isChecked()){
//                                    int forwardTimer = 1;
                                    Item item = new Item();
                                    item.setItemName(itemName.getText().toString());
                                    item.setTime("正向计时");
                                    itemList.add(item);
                                    todoItemAdapter.notifyDataSetChanged();

                                }
                                //不计时：
                                if(setTimeThree.isChecked()){
//                                    int noTimer = 2;
                                    Item item = new Item();
                                    item.setItemName(itemName.getText().toString());
                                    item.setTime("普通待办");
                                    itemList.add(item);
                                    todoItemAdapter.notifyDataSetChanged();
                                }
                            }
                            //定目标

                            //养习惯
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

            }
        });
    }

    private void getViews(View dialogView) {
        //以下是弹窗控件：
        addYes = dialogView.findViewById(R.id.add_todo_item_yes);
        addNo = dialogView.findViewById(R.id.add_todo_item_no);
        itemNameAbout = dialogView.findViewById(R.id.todo_item_about);

        itemName = dialogView.findViewById(R.id.todo_item_name);
        goalWorkload = dialogView.findViewById(R.id.goal_workload);
        habitWorkload = dialogView.findViewById(R.id.habit_workload);

        todoWant = dialogView.findViewById(R.id.todo_item_want);
        todoSetTime = dialogView.findViewById(R.id.todo_item_set_time);
        setTimeGroup = dialogView.findViewById(R.id.set_time_one_group);

        wantOne = dialogView.findViewById(R.id.want_one);
        wantTwo = dialogView.findViewById(R.id.want_two);
        wantThree = dialogView.findViewById(R.id.want_three);

        setTimeOne = dialogView.findViewById(R.id.set_time_one);
        setTimeTwo = dialogView.findViewById(R.id.set_time_two);
        setTimeThree = dialogView.findViewById(R.id.set_time_three);

        setTimeGroupOne = dialogView.findViewById(R.id.set_time_one_group_one);
        setTimeGroupTwo = dialogView.findViewById(R.id.set_time_one_group_two);
        setTimeGroupThree = dialogView.findViewById(R.id.set_time_one_group_three);

        goalDate = dialogView.findViewById(R.id.goal_date);
        setTimeOneTxt = dialogView.findViewById(R.id.set_time_one_txt);
        setTimeTwoTxt = dialogView.findViewById(R.id.set_time_two_txt);
        setTimeThreeTxt = dialogView.findViewById(R.id.set_time_three_txt);

        goalUnits = dialogView.findViewById(R.id.goal_units);
        habitDateUnits = dialogView.findViewById(R.id.habit_date_units);
        habitTimeUnits = dialogView.findViewById(R.id.habit_time_units);
        goalLinear = dialogView.findViewById(R.id.todo_item_goal);
        habitLinear = dialogView.findViewById(R.id.todo_item_habit);
    }


    private void findView(View view) {
        calendarLayout = view.findViewById(R.id.cancel_button);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerView);
        todoBtn = view.findViewById(R.id.todo_btn);
    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            goalDate.setText(days);
        }
    };


    @Override
    public void onItemClick(int position) {
        //点击事项进行编辑:
        Toast.makeText(getContext(), "你点击了"+itemList.get(position).getItemName(), Toast.LENGTH_SHORT).show();
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
