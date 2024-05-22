package net.onest.time.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import net.onest.time.R;
import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.holder.AdapterHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加任务 弹窗
 */
public class AddTaskDialog extends AlertDialog {
    private Button addYes, addNo, itemNameAbout, relaChange;
    private TextInputEditText itemName;
    private RadioGroup todoSetTime, setTimeGroup;

    private RadioButton setTimeOne, setTimeTwo, setTimeThree;
    private RadioButton setTimeGroupOne, setTimeGroupTwo, setTimeGroupThree;
    private TextView setTimeOneTxt, setTimeTwoTxt, setTimeThreeTxt, higherSet;
    private RelativeLayout popRela;

    private HashMap<String, String> map = new HashMap<>();


    private final List<TaskVo> tasks;
    private final AdapterHolder adapter;

    public AddTaskDialog(@NonNull Context context, List<TaskVo> tasks, AdapterHolder adapter) {
        super(context);
        this.adapter = adapter;
        this.tasks = tasks;

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.todo_fragment_add_item_pop_window, null);

        getViews(view);//获取控件
        setListeners();

        show();

        getWindow().setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        relaChange.setVisibility(View.GONE);
        setTimeTwoTxt.setVisibility(View.GONE);
        setTimeThreeTxt.setVisibility(View.GONE);
    }

    private void setListeners() {
        higherSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting, null);
                final Dialog dialog = builder.create();
                show();
                getWindow().setContentView(dialogView);
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                                                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT);
                                            }
                                        }, null, false, R.layout.my_confim_popup)//绑定已有布局
                                .show();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                integerList.add(clockTimes.getText().toString().trim());
                        map.put("remark", remark.getText().toString().trim());
                        map.put("clockTimes", clockTimes.getText().toString().trim());
                        map.put("rest", rest.getText().toString().trim());
                        if (checkBox.isChecked()) {
                            map.put("again", "1");
                        } else {
                            map.put("again", "0");
                        }
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
                new XPopup.Builder(getContext())
                        .asConfirm("什么是番茄钟", "1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。\n" +
                                        "2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。\n3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦",
                                "关闭", "番茄钟牛逼",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT);
                                    }
                                }, null, false, R.layout.my_confim_popup)//绑定已有布局
                        .show();
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


        setTimeGroupThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), setTimeGroupThree.getText().toString() + "", Toast.LENGTH_SHORT).show();
                new XPopup.Builder(getContext()).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:",
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
                    Toast.makeText(getContext(), "请输入Item名称", Toast.LENGTH_SHORT).show();
                } else {
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
                            TaskVo taskVo = TaskApi.addTask(taskDto);
                            tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                            adapter.notifyDataSetChanged();
                        } else if (setTimeGroupTwo.isChecked()) {
                            String strings = setTimeGroupTwo.getText().toString().split(" ")[0];

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
                            TaskVo taskVo = TaskApi.addTask(taskDto);
                            tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                            adapter.notifyDataSetChanged();
                        } else {
                            String strings = setTimeGroupThree.getText().toString().split(" ")[0];

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
                            TaskVo taskVo = TaskApi.addTask(taskDto);
                            tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //正向计时：
                    if (setTimeTwo.isChecked()) {
//                                    int forwardTimer = 1;
//                                Item item = new Item();
//                                item.setItemName(itemName.getText().toString());
//                                item.setTime("正向计时");
//                                itemListByDay.add(item);
//                                todoItemAdapter.notifyDataSetChanged();

                    }
                    //不计时：
                    if (setTimeThree.isChecked()) {
//                                    int noTimer = 2;
//                                Item item = new Item();
//                                item.setItemName(itemName.getText().toString());
//                                item.setTime("普通待办");
//                                itemListByDay.add(item);
//                                todoItemAdapter.notifyDataSetChanged();
                    }
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

    private void getViews(View view) {
        //以下是弹窗控件：
        addYes = view.findViewById(R.id.add_todo_item_yes);
        addNo = view.findViewById(R.id.add_todo_item_no);
        itemNameAbout = view.findViewById(R.id.todo_item_about);
        relaChange = view.findViewById(R.id.add_todo_item_change);

        itemName = view.findViewById(R.id.todo_item_name);

        todoSetTime = view.findViewById(R.id.todo_item_set_time);
        setTimeGroup = view.findViewById(R.id.set_time_one_group);


        setTimeOne = view.findViewById(R.id.set_time_one);
        setTimeTwo = view.findViewById(R.id.set_time_two);
        setTimeThree = view.findViewById(R.id.set_time_three);

        setTimeGroupOne = view.findViewById(R.id.set_time_one_group_one);
        setTimeGroupTwo = view.findViewById(R.id.set_time_one_group_two);
        setTimeGroupThree = view.findViewById(R.id.set_time_one_group_three);

        setTimeOneTxt = view.findViewById(R.id.set_time_one_txt);
        setTimeTwoTxt = view.findViewById(R.id.set_time_two_txt);
        setTimeThreeTxt = view.findViewById(R.id.set_time_three_txt);

        higherSet = view.findViewById(R.id.todo_fragment_add_item_higher_setting);
        popRela = view.findViewById(R.id.todo_add_item_pop_background);
    }
}
