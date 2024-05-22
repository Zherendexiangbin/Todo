package net.onest.time.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import net.onest.time.R;
import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.holder.AdapterHolder;
import net.onest.time.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateTaskDialog extends AlertDialog {
    private final List<TaskVo> tasks;
    private final AdapterHolder adapter;

    //弹窗：
    private TextView title,learnFrequency, learnTime, textRemark;
    private Button changeBackground, setItem, moveItem, deleteItem, timing;
    private LinearLayout learnHistory, learnStatistics;

    //弹中弹
    private Button addYes, addNo, itemNameAbout, relaChange;
    private TextInputEditText itemName;
    private RadioGroup todoSetTime, setTimeGroup;

    private RadioButton setTimeOne, setTimeTwo, setTimeThree;
    private RadioButton setTimeGroupOne, setTimeGroupTwo, setTimeGroupThree;
    private TextView setTimeOneTxt, setTimeTwoTxt, setTimeThreeTxt, higherSet;
    private RelativeLayout popRela;

    //获取”更高设置“中的信息
    private final HashMap<String, String> map = new HashMap<>();
    private final Context context;
    private final TaskVo task;
    private final Dialog dialog;

    protected UpdateTaskDialog(@NonNull Context context, TaskVo task, List<TaskVo> tasks, AdapterHolder adapter, Dialog dialog) {
        super(context);
        this.tasks = tasks;
        this.adapter = adapter;
        this.context = context;
        this.task = task;
        this.dialog = dialog;

        Toast.makeText(context, "你点击了" + task.getTaskName(), Toast.LENGTH_SHORT).show();

        //设置弹窗：
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.todo_fragment_add_item_pop_window, null);

        getViews(dialogView);//获取控件

        dialog.show();
        dialog.getWindow().setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        itemName.setText(task.getTaskName());
        textRemark.setText(task.getRemark());
        setTimeTwoTxt.setVisibility(View.GONE);
        setTimeThreeTxt.setVisibility(View.GONE);

        Glide.with(context)
                .asBitmap()
                .load(R.drawable.new_card_bg_1)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(resource);
                        popRela.setBackground(drawable);
                    }
                });
        setListeners();
    }

    private void setListeners() {
        //改变背景：
        relaChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popRela.setBackground(DrawableUtil.getRandomImage(context));
            }
        });

        higherSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting, null);
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
//                                integerList.add(clockTimes.getText().toString().trim());
                        map.put("remark", remark.getText().toString().trim());
                        map.put("clockTimes", clockTimes.getText().toString().trim());
                        map.put("rest", rest.getText().toString().trim());
                        if (checkBox.isChecked()) {
                            map.put("again", "1");
                        } else {
                            map.put("again", "0");
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
                    if (setTimeOne.isChecked()) {
                        if (setTimeGroupOne.isChecked()) {
                            String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                            ArrayList<Integer> estimate = new ArrayList<>();
                            if (map.size() != 0 && map.get("clockTimes") != null) {
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                            } else {
                                estimate.addAll(task.getEstimate());
                            }
                            TaskDto taskDto = new TaskDto();
                            taskDto.setTaskName(itemName.getText().toString());
                            taskDto.setEstimate(estimate);
                            taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                            taskDto.setTaskId(task.getTaskId());
                            if (map.size() != 0 && map.get("remark") != null) {
                                taskDto.setRemark(map.get("remark"));
                            } else {
                                taskDto.setRemark(task.getRemark());
                            }
                            if (map.size() != 0 && map.get("rest") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                            } else {
                                taskDto.setRestTime(task.getRestTime());
                            }
                            if (map.size() != 0 && map.get("again") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("again")));
                            } else {
                                taskDto.setAgain(task.getAgain());
                            }

                            TaskVo taskVo = TaskApi.updateTask(taskDto);
                            for (TaskVo vo : tasks) {
                                if (vo.getTaskId() == taskVo.getTaskId()) {
                                    tasks.remove(vo);
                                    tasks.add(taskVo);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else if (setTimeGroupTwo.isChecked()) {
                            String strings = setTimeGroupTwo.getText().toString().split(" ")[0];

                            ArrayList<Integer> estimate = new ArrayList<>();
                            if (map.size() != 0 && map.get("clockTimes") != null) {
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                            } else {
                                estimate.addAll(task.getEstimate());
                            }
                            TaskDto taskDto = new TaskDto();
                            taskDto.setTaskName(itemName.getText().toString());
                            taskDto.setEstimate(estimate);
                            taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                            taskDto.setTaskId(task.getTaskId());
                            if (map.size() != 0 && map.get("remark") != null) {
                                taskDto.setRemark(map.get("remark"));
                            } else {
                                taskDto.setRemark(task.getRemark());
                            }
                            if (map.size() != 0 && map.get("rest") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                            } else {
                                taskDto.setRestTime(task.getRestTime());
                            }
                            if (map.size() != 0 && map.get("again") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("again")));
                            } else {
                                taskDto.setAgain(task.getAgain());
                            }

                            TaskVo taskVo = TaskApi.updateTask(taskDto);
                            for (TaskVo vo : tasks) {
                                if (vo.getTaskId() == taskVo.getTaskId()) {
                                    tasks.remove(vo);
                                    tasks.add(taskVo);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            String strings = setTimeGroupThree.getText().toString().split(" ")[0];

                            ArrayList<Integer> estimate = new ArrayList<>();
                            if (map.size() != 0 && map.get("clockTimes") != null) {
                                estimate.add(Integer.valueOf(map.get("clockTimes")));
                            } else {
                                estimate.addAll(task.getEstimate());
                            }
                            TaskDto taskDto = new TaskDto();
                            taskDto.setTaskName(itemName.getText().toString());
                            taskDto.setEstimate(estimate);
                            taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                            taskDto.setTaskId(task.getTaskId());
                            if (map.size() != 0 && map.get("remark") != null) {
                                taskDto.setRemark(map.get("remark"));
                            } else {
                                taskDto.setRemark(task.getRemark());
                            }
                            if (map.size() != 0 && map.get("rest") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                            } else {
                                taskDto.setRestTime(task.getRestTime());
                            }
                            if (map.size() != 0 && map.get("again") != null) {
                                taskDto.setRestTime(Integer.valueOf(map.get("again")));
                            } else {
                                taskDto.setAgain(task.getAgain());
                            }

                            TaskVo taskVo = TaskApi.updateTask(taskDto);
                            for (TaskVo vo : tasks) {
                                if (vo.getTaskId() == taskVo.getTaskId()) {
                                    tasks.remove(vo);
                                    tasks.add(taskVo);
                                }
                            }
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
                    // TODO: 2024/5/22  
//                    dialog.dismiss();
                }
            }
        });

        addNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2024/5/22  
//                dialog.dismiss();
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

    private void setViews(View dialogView) {
        title = dialogView.findViewById(R.id.txt_title);//待办标题txt
        changeBackground = dialogView.findViewById(R.id.btn_changeBackground);//设置背景button
        setItem = dialogView.findViewById(R.id.btn_set);//编辑待办button
        moveItem = dialogView.findViewById(R.id.btn_move);//排序或移动待办button
        deleteItem = dialogView.findViewById(R.id.btn_delete);//删除待办button
        learnFrequency = dialogView.findViewById(R.id.txt_learn_frequency);//累计学习次数txt
        learnTime = dialogView.findViewById(R.id.txt_learn_time);//累计学习时间txt单位分钟
        learnHistory = dialogView.findViewById(R.id.learn_history);//历史记录(页面跳转)
        learnStatistics = dialogView.findViewById(R.id.learn_statistics);//数据统计(页面跳转)
        textRemark = dialogView.findViewById(R.id.text_remark);
    }
}
