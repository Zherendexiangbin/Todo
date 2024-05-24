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
                dialog.show();
                dialog.getWindow().setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText remark = dialog.findViewById(R.id.todo_fragment_add_higher_remark);
                EditText clockTimes = dialog.findViewById(R.id.todo_fragment_add_clock_times);
                EditText rest = dialog.findViewById(R.id.todo_fragment_add_rest_time);
                CheckBox checkBox = dialog.findViewById(R.id.todo_fragment_add_higher_again);
                Button clockAbout = dialog.findViewById(R.id.todo_clock_times_about);
                Button btnYes = dialog.findViewById(R.id.add_todo_higher_setting_item_yes);
                Button btnNo = dialog.findViewById(R.id.add_todo_higher_setting_item_no);

                //倒计时：全部显示
                if(setTimeOne.isChecked()){
                    clockAbout.setVisibility(View.VISIBLE);//问号备注
                    clockTimes.setVisibility(View.VISIBLE);//单次循环次数
                    rest.setVisibility(View.VISIBLE);
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
                            if("".equals(remark.getText().toString().trim())){
                                map.put("remark", "无");
                            }else{
                                map.put("remark", remark.getText().toString().trim());
                            }

                            if( "".equals(clockTimes.getText().toString().trim())){
                                map.put("clockTimes", "1");
                            }else{
                                map.put("clockTimes", clockTimes.getText().toString().trim());
                            }
                            if("".equals(map.put("rest", rest.getText().toString().trim()))){
                                map.put("rest", "5");
                            }else{
                                map.put("rest", rest.getText().toString().trim());
                            }
                            if (checkBox.isChecked()) {
                                map.put("again", "1");
                            } else {
                                map.put("again", "0");
                            }
                            dialog.dismiss();
                        }
                    });
                }else if(setTimeTwo.isChecked()){
                    //正向计时：取消显示单次循环次数及其问好备注
                    clockAbout.setVisibility(View.GONE);//问号备注
                    clockTimes.setVisibility(View.GONE);//单次循环次数

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("".equals(remark.getText().toString().trim())){
                                map.put("remark", "无");
                            }else{
                                map.put("remark", remark.getText().toString().trim());
                            }

                            if("".equals(map.put("rest", rest.getText().toString().trim()))){
                                map.put("rest", "5");
                            }else{
                                map.put("rest", rest.getText().toString().trim());
                            }

                            if (checkBox.isChecked()) {
                                map.put("again", "1");
                            } else {
                                map.put("again", "0");
                            }
                            dialog.dismiss();
                        }
                    });
                }else{
                    //不计时:仅显示任务备注。
                    clockAbout.setVisibility(View.GONE);//问号备注
                    clockTimes.setVisibility(View.GONE);//单次循环次数
                    rest.setVisibility(View.GONE);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if("".equals(remark.getText().toString().trim())){
                                map.put("remark", "无");
                            }else{
                                map.put("remark", remark.getText().toString().trim());
                            }
                            if (checkBox.isChecked()) {
                                map.put("again", "1");
                            } else {
                                map.put("again", "0");
                            }
                            dialog.dismiss();
                        }
                    });
                }


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
                            if (map.get("again") == null) {
                                map.put("again", "1");
                            }
                            taskDto.setAgain(Integer.valueOf(map.get("again")));
                            taskDto.setType(0);
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
                            if (map.get("again") == null) {
                                map.put("again", "1");
                            }
                            taskDto.setAgain(Integer.valueOf(map.get("again")));
                            taskDto.setType(0);
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
                            if (map.get("again") == null) {
                                map.put("again", "1");
                            }
                            taskDto.setAgain(Integer.valueOf(map.get("again")));
                            taskDto.setType(0);
                            TaskVo taskVo = TaskApi.addTask(taskDto);
                            tasks.add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //正向计时：
                    if (setTimeTwo.isChecked()) {
                        //取消了单次循环次数
                        TaskDto taskDto = new TaskDto();
                        taskDto.setTaskName(itemName.getText().toString().trim());
                        taskDto.setType(1);
                        taskDto.setRemark(map.get("remark"));
                        if (map.get("rest") == null) {
                            map.put("rest", "5");
                        }
                        taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                        if (map.get("again") == null) {
                            map.put("again", "1");
                        }
                        taskDto.setAgain(Integer.valueOf(map.get("again")));

                        TaskVo taskVo = TaskApi.addTask(taskDto);
                        tasks.add(taskVo);
                        adapter.notifyDataSetChanged();
                    }
                    //不计时：
                    if (setTimeThree.isChecked()) {
                        TaskDto taskDto = new TaskDto();
                        taskDto.setTaskName(itemName.getText().toString().trim());
                        taskDto.setType(2);
                        taskDto.setRemark(map.get("remark"));
                        if (map.get("again") == null) {
                            map.put("again", "1");
                        }
                        taskDto.setAgain(Integer.valueOf(map.get("again")));

                        TaskVo taskVo = TaskApi.addTask(taskDto);
                        tasks.add(taskVo);
                        adapter.notifyDataSetChanged();
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
