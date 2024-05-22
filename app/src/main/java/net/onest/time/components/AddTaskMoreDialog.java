package net.onest.time.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import net.onest.time.R;
import net.onest.time.adapter.todo.TodoItemAdapter;
import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.holder.AdapterHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 同AddTaskDialog，但有更多选项
 */
public class AddTaskMoreDialog extends AlertDialog {
    private final List<TaskVo> tasks;
    private final AdapterHolder adapter;
    private final String category;

    //以下是弹框布局控件：
    private Button addYes, addNo, itemNameAbout;
    private EditText goalWorkload, habitWorkload;
    private TextInputEditText itemName;
    private RadioGroup todoWant, todoSetTime, setTimeGroup;
    private RadioButton wantOne, wantTwo, wantThree;
    private RadioButton setTimeOne, setTimeTwo, setTimeThree;
    private RadioButton setTimeGroupOne, setTimeGroupTwo, setTimeGroupThree;
    private TextView goalDate, setTimeOneTxt, setTimeTwoTxt, setTimeThreeTxt, higherSet;
    private Spinner goalUnits, habitDateUnits, habitTimeUnits;
    private LinearLayout goalLinear, habitLinear;


    private Context context;
    private HashMap<String, String> map = new HashMap<>();

    public AddTaskMoreDialog(@NonNull Context context, String category, List<TaskVo> tasks, AdapterHolder adapter) {
        super(context);
        this.context = context;
        this.category = category;
        this.adapter = adapter;
        this.tasks = tasks;

        View dialogView = LayoutInflater.from(context).inflate(R.layout.list_fragment_add_expandable_child_item_pop_window, null);
        getViews(dialogView);//获取控件

        show();
        getWindow().setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTimeTwoTxt.setVisibility(View.GONE);
        setTimeThreeTxt.setVisibility(View.GONE);
        goalLinear.setVisibility(View.GONE);
        habitLinear.setVisibility(View.GONE);
        setListeners();
    }

    private void findViews(View dialogView) {

    }

    private void setListeners() {
        higherSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting, null);
                final Dialog dialog = builder.create();
                show();
                getWindow().setContentView(dialogView);
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText remark = dialogView.findViewById(R.id.todo_fragment_add_higher_remark);
                EditText clockTimes = dialogView.findViewById(R.id.todo_fragment_add_clock_times);
                EditText rest = dialogView.findViewById(R.id.todo_fragment_add_rest_time);
                Button clockAbout = dialogView.findViewById(R.id.todo_clock_times_about);
                Button btnYes = dialogView.findViewById(R.id.add_todo_higher_setting_item_yes);
                Button btnNo = dialogView.findViewById(R.id.add_todo_higher_setting_item_no);

                clockAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new XPopup.Builder(context)
                                .asConfirm("什么是单次循环次数", "举例:\n" +
                                                "小明每次学习想学75分钟，但是75分钟太长学的太累，那么可以设定一个番茄钟的时间为25分钟，单次预期循环次数为3次。\n" +
                                                "这样的番茄钟就会按照:\n" +
                                                "学习25分钟-休息-学习25分钟-休息-学习25分钟-休息(共循环三次)\n" +
                                                "来执行",
                                        "关闭", "确认",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                Toast.makeText(context, "click", Toast.LENGTH_SHORT);
                                            }
                                        }, null, false, R.layout.my_confim_popup)//绑定已有布局
                                .show();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        });

        itemNameAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(context)
                        .asConfirm("什么是番茄钟", "1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。\n" +
                                        "2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。\n3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦",
                                "关闭", "番茄钟牛逼",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        Toast.makeText(context, "click", Toast.LENGTH_SHORT);
                                    }
                                }, null, false, R.layout.my_confim_popup)//绑定已有布局
                        .show();
            }
        });

        todoWant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                switch (checkedId) {
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
                new DatePickerDialog(context, onDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        setTimeGroupThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, setTimeGroupThree.getText().toString() + "", Toast.LENGTH_SHORT).show();
                new XPopup.Builder(context).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                setTimeGroupThree.setText(text + " 分钟");
                            }
                        }).show();
            }
        });

        addYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(itemName.getText().toString()) && itemName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入Item名称", Toast.LENGTH_SHORT).show();
                } else {
                    //普通的番茄时钟:
                    if (wantOne.isChecked()) {
                        //倒计时
//                                int countDownTimer=0;
                        if (setTimeOne.isChecked()) {
                            if (setTimeGroupOne.isChecked()) {
                                String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                                ArrayList<Integer> estimate = new ArrayList<>();
                                if (map.get("clockTimes") == null) {
                                    map.put("clockTimes", "1");
                                }
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                                TaskDto taskDto = new TaskDto();
                                taskDto.setTaskName(itemName.getText().toString());
                                taskDto.setEstimate(estimate);
                                taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                taskDto.setRemark(map.get("remark"));
                                if (map.get("rest") == null) {
                                    map.put("rest", "5");
                                }
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                taskDto.setAgain(1);
                                taskDto.setCategory(category);

                                TaskVo taskVo = TaskApi.addTask(taskDto);
                                tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                adapter.notifyDataSetChanged();
                            } else if (setTimeGroupTwo.isChecked()) {
                                String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                                ArrayList<Integer> estimate = new ArrayList<>();
                                if (map.get("clockTimes") == null) {
                                    map.put("clockTimes", "1");
                                }
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                                TaskDto taskDto = new TaskDto();
                                taskDto.setTaskName(itemName.getText().toString());
                                taskDto.setEstimate(estimate);
                                taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                taskDto.setRemark(map.get("remark"));
                                if (map.get("rest") == null) {
                                    map.put("rest", "5");
                                }
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                taskDto.setAgain(1);
                                taskDto.setCategory(category);

                                TaskVo taskVo = TaskApi.addTask(taskDto);
                                tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                adapter.notifyDataSetChanged();
                            } else {
                                String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                                ArrayList<Integer> estimate = new ArrayList<>();
                                if (map.get("clockTimes") == null) {
                                    map.put("clockTimes", "1");
                                }
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                                TaskDto taskDto = new TaskDto();
                                taskDto.setTaskName(itemName.getText().toString());
                                taskDto.setEstimate(estimate);
                                taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                taskDto.setRemark(map.get("remark"));
                                if (map.get("rest") == null) {
                                    map.put("rest", "5");
                                }
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                taskDto.setAgain(1);
                                taskDto.setCategory(category);

                                TaskVo taskVo = TaskApi.addTask(taskDto);
                                tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //正向计时：
                        if (setTimeTwo.isChecked()) {
//                                    int forwardTimer = 1;
//                                    TaskVo item = new Item();
//                                    item.setTaskName(itemName.getText().toString());
//                                    item.setClockDuration("正向计时");
//                                    parentItemList.get(groupPosition).getChildItemList().add(item);
//                                    notifyDataSetChanged();
                        }
                        //不计时：
                        if (setTimeThree.isChecked()) {
//                                    int noTimer = 2;
//                                    Item item = new Item();
//                                    item.setItemName(itemName.getText().toString());
//                                    item.setTime("普通待办");
//                                    parentItemList.get(groupPosition).getChildItemList().add(item);
//                                    notifyDataSetChanged();
                        }
                    }
                    //定目标
                    //养习惯
                    dismiss();
                }
            }
        });
        addNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        higherSet = dialogView.findViewById(R.id.list_fragment_add_item_higher_setting);

        goalUnits = dialogView.findViewById(R.id.goal_units);
        habitDateUnits = dialogView.findViewById(R.id.habit_date_units);
        habitTimeUnits = dialogView.findViewById(R.id.habit_time_units);
        goalLinear = dialogView.findViewById(R.id.todo_item_goal);
        habitLinear = dialogView.findViewById(R.id.todo_item_habit);
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
}
