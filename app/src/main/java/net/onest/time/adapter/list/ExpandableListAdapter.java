package net.onest.time.adapter.list;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.entity.Item;
import net.onest.time.entity.list.ParentItem;
import net.onest.time.utils.ColorUtil;
import net.onest.time.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int itemViewId;
    private int childViewId;
    private List<ParentItem> parentItemList;
    private List<TaskVo> childItemList;
    private Intent intent;

    //以下是弹框布局控件：
    private Button addYes,addNo,itemNameAbout;
    private EditText goalWorkload,habitWorkload;
    private TextInputEditText itemName;
    private RadioGroup todoWant,todoSetTime,setTimeGroup;
    private RadioButton wantOne,wantTwo,wantThree;
    private RadioButton setTimeOne,setTimeTwo,setTimeThree;
    private RadioButton setTimeGroupOne,setTimeGroupTwo,setTimeGroupThree;
    private TextView goalDate,setTimeOneTxt,setTimeTwoTxt,setTimeThreeTxt,higherSet;
    private Spinner goalUnits,habitDateUnits,habitTimeUnits;
    private LinearLayout goalLinear,habitLinear;

    //点击childItem弹窗：
    private TextView title,learnFrequency, learnTime,textRemark;
    private Button changeBackground, setItem, moveItem, deleteItem, timing;
    private LinearLayout learnHistory, learnStatistics;

    //childItem弹中弹
    private Button relaChange;

    private RelativeLayout popRela;


    public ExpandableListAdapter(int itemViewId, int childViewId, Context context, List<ParentItem> parentItemList) {
        this.itemViewId = itemViewId;
        this.childViewId = childViewId;
        this.context = context;
        this.parentItemList = parentItemList;
    }

    public List<ParentItem> getParentItemList() {
        return parentItemList;
    }

    public void setParentItemList(List<ParentItem> parentItemList) {
        this.parentItemList = parentItemList;
    }

    public List<TaskVo> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<TaskVo> childItemList) {
        this.childItemList = childItemList;
    }

    //返回列表项数量
    @Override
    public int getGroupCount() {
        return parentItemList.size();
    }
    //返回子列表项数量
    @Override
    public int getChildrenCount(int groupPosition) {
        return parentItemList.get(groupPosition).getChildItemList().size();
    }
    //获得指定列表项数据
    @Override
    public Object getGroup(int groupPosition) {
        return parentItemList.get(groupPosition);
    }
    //获得指定子列表项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentItemList.get(groupPosition).getChildItemList().get(childPosition);
    }
    //获得父列表id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //获得子列表id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    //指定位置相应的组视图
    @Override
    public boolean hasStableIds() {
        return true;
    }
    //设置父列表的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView!=null)
            return convertView;
        View view = LayoutInflater.from(context).inflate(itemViewId,parent,false);

        View backView = view.findViewById(R.id.list_fragment_parent_item_color);
        TextView textView = view.findViewById(R.id.list_fragment_parent_item_name);
        Button dataBtn = view.findViewById(R.id.list_fragment_parent_data);
        Button addBtn = view.findViewById(R.id.list_fragment_parent_add);
        Button expandBtn = view.findViewById(R.id.list_fragment_parent_arrow);

        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, "该功能尚未完善😙", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
            }
        });



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.list_fragment_add_expandable_child_item_pop_window,null);
                getViews(dialogView);//获取控件
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                setTimeTwoTxt.setVisibility(View.GONE);
                setTimeThreeTxt.setVisibility(View.GONE);
                goalLinear.setVisibility(View.GONE);
                habitLinear.setVisibility(View.GONE);

                HashMap<String,String> map = new HashMap<String,String>();

                higherSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomDialogStyle);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.todo_fragment_add_higher_setting,null);
                        final Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setContentView(dialogView);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                                                        Toast.makeText(context,"click",Toast.LENGTH_SHORT);
                                                    }
                                                }, null, false,R.layout.my_confim_popup)//绑定已有布局
                                        .show();
                            }
                        });

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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
                                                Toast.makeText(context,"click",Toast.LENGTH_SHORT);
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
                        new DatePickerDialog(context, onDateSetListener, mYear, mMonth, mDay).show();
                    }
                });

                setTimeGroupThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, setTimeGroupThree.getText().toString()+"", Toast.LENGTH_SHORT).show();
                        new XPopup.Builder(context).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:",
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
                            Toast.makeText(context, "请输入Item名称", Toast.LENGTH_SHORT).show();
                        }else{
                            //普通的番茄时钟:
                            if(wantOne.isChecked()){
                                //倒计时
//                                int countDownTimer=0;
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
                                        taskDto.setCategory(parentItemList.get(groupPosition).getParentItemName());

                                        TaskVo taskVo = TaskApi.addTask(taskDto);
                                        parentItemList.get(groupPosition).getChildItemList().add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                        notifyDataSetChanged();
                                    } else if (setTimeGroupTwo.isChecked()) {
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
                                        taskDto.setCategory(parentItemList.get(groupPosition).getParentItemName());

                                        TaskVo taskVo = TaskApi.addTask(taskDto);
                                        parentItemList.get(groupPosition).getChildItemList().add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                        notifyDataSetChanged();
                                    }else{
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
                                        taskDto.setCategory(parentItemList.get(groupPosition).getParentItemName());

                                        TaskVo taskVo = TaskApi.addTask(taskDto);
                                        parentItemList.get(groupPosition).getChildItemList().add(taskVo);
//                                    todoItemAdapter.notifyItemChanged(itemListByDay.size()-1);
                                        notifyDataSetChanged();
                                    }
                                }
                                //正向计时：
                                if(setTimeTwo.isChecked()){
//                                    int forwardTimer = 1;
//                                    TaskVo item = new Item();
//                                    item.setTaskName(itemName.getText().toString());
//                                    item.setClockDuration("正向计时");
//                                    parentItemList.get(groupPosition).getChildItemList().add(item);
//                                    notifyDataSetChanged();
                                }
                                //不计时：
                                if(setTimeThree.isChecked()){
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
        
        backView.setBackgroundColor(parentItemList.get(groupPosition).getParentItemColor());
        textView.setText(parentItemList.get(groupPosition).getParentItemName());
        return view;
    }
    //设置子列表的view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView!=null)
            return convertView;
        View view = LayoutInflater.from(context).inflate(childViewId,parent,false);

        LinearLayout backLin = view.findViewById(R.id.list_fragment_item_child_background_lin);
        TextView childName = view.findViewById(R.id.list_fragment_item_child_txt_name);
        TextView childTime = view.findViewById(R.id.list_fragment_item_child_txt_time);
        Button startBtn = view.findViewById(R.id.list_fragment_item_child_ry_btn);
        RelativeLayout statistics = view.findViewById(R.id.list_click_statistics);


        Glide.with(context).asBitmap().load(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getBackground()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(resource);
                backLin.setBackground(drawable);
            }
        });
//        backLin.setBackgroundColor(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getColor());
        childName.setText(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName());
        childTime.setText(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getClockDuration()+" 分钟");

        //编辑数据：
        statistics.setOnClickListener(views -> {
            //设置弹窗
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.item_pop, null);
            setViews(dialogView);
            final Dialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //删除:
            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(context).asConfirm("", "你确定要删除这项任务吗？",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            TaskApi.removeTask(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskId());
                                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                                            parentItemList.get(groupPosition).getChildItemList().remove(parentItemList.get(groupPosition).getChildItemList().get(childPosition));
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    })
                            .show();
                }
            });

            //编辑
            setItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    Toast.makeText(context, "你点击了"+parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName(), Toast.LENGTH_SHORT).show();

                    //设置弹窗：
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.todo_fragment_add_item_pop_window,null);
                    getViews(dialogView);//获取控件
                    final Dialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setContentView(dialogView);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    itemName.setText(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName());
                    textRemark.setText(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRemark());
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


                    //改变背景：
                    relaChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popRela.setBackground(DrawableUtil.getRandomImage(context));
                        }
                    });

                    //获取”更高设置“中的信息
                    HashMap<String,String> map = new HashMap<String,String>();

                    higherSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.CustomDialogStyle);
                            LayoutInflater inflater = LayoutInflater.from(context);
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
                                                            Toast.makeText(context,"click",Toast.LENGTH_SHORT);
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
                            new XPopup.Builder(context)
                                    .asConfirm("什么是番茄钟", "1.番茄钟是全身心工作25分钟，休息5分钟的工作方法。\n" +
                                                    "2.输入事项名称，点击√按钮即可添加一个标准的番茄钟待办。\n3.点击代办卡片上的开始按钮就可以开始一个番茄钟啦",
                                            "关闭", "番茄钟牛逼",
                                            new OnConfirmListener() {
                                                @Override
                                                public void onConfirm() {
                                                    Toast.makeText(context,"click",Toast.LENGTH_SHORT);
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
                            Toast.makeText(context, setTimeGroupThree.getText().toString()+"", Toast.LENGTH_SHORT).show();
                            new XPopup.Builder(context).asInputConfirm("自定义番茄钟时间", "输入倒计时分钟数:",
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
                                Toast.makeText(context, "请输入Item名称", Toast.LENGTH_SHORT).show();
                            }else{
                                if(setTimeOne.isChecked()){
                                    if(setTimeGroupOne.isChecked()){
                                        String strings = setTimeGroupOne.getText().toString().split(" ")[0];

                                        ArrayList<Integer> estimate = new ArrayList<>();
                                        if(map.size()!=0 && map.get("clockTimes")!=null){
                                            estimate.add(Integer.valueOf(map.get("clockTimes")));
                                        }else{
                                            estimate.addAll(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getAgain());
                                        }

                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        childItemList = parentItemList.get(groupPosition).getChildItemList();
                                        for (TaskVo vo : childItemList) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                parentItemList.get(groupPosition).getChildItemList().remove(vo);
                                                parentItemList.get(groupPosition).getChildItemList().add(taskVo);
                                            }
                                        }
                                        notifyDataSetChanged();
                                    } else if (setTimeGroupTwo.isChecked()) {
                                        String strings = setTimeGroupTwo.getText().toString().split(" ")[0];

                                        ArrayList<Integer> estimate = new ArrayList<>();
                                        if(map.size()!=0 && map.get("clockTimes")!=null){
                                            estimate.add(Integer.valueOf(map.get("clockTimes")));
                                        }else{
                                            estimate.addAll(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getAgain());
                                        }

                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        childItemList = parentItemList.get(groupPosition).getChildItemList();
                                        for (TaskVo vo : childItemList) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                parentItemList.get(groupPosition).getChildItemList().remove(vo);
                                                parentItemList.get(groupPosition).getChildItemList().add(taskVo);
                                            }
                                        }
                                        notifyDataSetChanged();
                                    }else{
                                        String strings = setTimeGroupThree.getText().toString().split(" ")[0];

                                        ArrayList<Integer> estimate = new ArrayList<>();
                                        if(map.size()!=0 && map.get("clockTimes")!=null){
                                            estimate.add(Integer.valueOf(map.get("clockTimes")));
                                        }else{
                                            estimate.addAll(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getAgain());
                                        }

                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        childItemList = parentItemList.get(groupPosition).getChildItemList();
                                        for (TaskVo vo : childItemList) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                parentItemList.get(groupPosition).getChildItemList().remove(vo);
                                                parentItemList.get(groupPosition).getChildItemList().add(taskVo);
                                            }
                                        }
                                        notifyDataSetChanged();
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
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("正向计时".equals(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getClockDuration()+"")){
                    //正向计时：
                    intent = new Intent();
                    intent.setClass(context, TimerActivity.class);
                    intent.putExtra("method", "forWard");
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,startBtn,"fab").toBundle());
                } else if ("普通待办".equals(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getClockDuration()+"")) {
                    //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    SpannableString spannableString = new SpannableString(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName());
                    spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    parentItemList.get(groupPosition).getChildItemList().get(childPosition).setTaskName(spannableString.toString());
//                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                }else{
                    //倒计时：
                    intent = new Intent();
                    String[] parts = childTime.getText().toString().split(" ");
                    String num = parts[0];
//                int num = Integer.parseInt(parts[0]);
                    intent.putExtra("time", num);
                    intent.putExtra("method", "countDown");
                    intent.setClass(context, TimerActivity.class);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,startBtn,"fab").toBundle());
                }
            }
        });


        return view;
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


    //当选择子节点的时候，调用该方法(点击二级列表)
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

    private void setViews(View dialogView, int groupPosition, int childPosition) {
        TextView title,learnFrequency, learnTime;
        Button changeBackground, setItem, moveItem, deleteItem, timing;
        LinearLayout learnHistory, learnStatistics;

        title = dialogView.findViewById(R.id.txt_title);//待办标题txt
        changeBackground = dialogView.findViewById(R.id.btn_changeBackground);//设置背景button
        setItem = dialogView.findViewById(R.id.btn_set);//编辑待办button
        moveItem = dialogView.findViewById(R.id.btn_move);//排序或移动待办button
        deleteItem = dialogView.findViewById(R.id.btn_delete);//删除待办button
        learnFrequency = dialogView.findViewById(R.id.txt_learn_frequency);//累计学习次数txt
        learnTime = dialogView.findViewById(R.id.txt_learn_time);//累计学习时间txt单位分钟
        learnHistory = dialogView.findViewById(R.id.learn_history);//历史记录(页面跳转)
        learnStatistics = dialogView.findViewById(R.id.learn_statistics);//数据统计(页面跳转)

    }

}
