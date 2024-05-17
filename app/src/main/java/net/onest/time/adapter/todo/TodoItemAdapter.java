package net.onest.time.adapter.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import net.onest.time.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyViewHolder>{
    private Context context;
    //    private List<Item> itemList = new ArrayList<>();
    private List<TaskVo> itemListByDay  = new ArrayList<>();

    private OnItemClickListener mItemClickListener;
    private Intent intent;

    //弹窗：
    private TextView title,learnFrequency, learnTime,textRemark;
    private Button changeBackground, setItem, moveItem, deleteItem, timing;
    private LinearLayout learnHistory, learnStatistics;

    //弹中弹
    private Button addYes,addNo,itemNameAbout,relaChange;
    private TextInputEditText itemName;
    private RadioGroup todoSetTime,setTimeGroup;

    private RadioButton setTimeOne,setTimeTwo,setTimeThree;
    private RadioButton setTimeGroupOne,setTimeGroupTwo,setTimeGroupThree;
    private TextView setTimeOneTxt,setTimeTwoTxt,setTimeThreeTxt,higherSet;
    private RelativeLayout popRela;

    public List<TaskVo> getItemListByDay() {
        return itemListByDay;
    }

    public void setItemListByDay(List<TaskVo> itemListByDay) {
        this.itemListByDay = itemListByDay;
    }

    public TodoItemAdapter(Context context, List<TaskVo> itemListByDay) {
        this.context = context;
        this.itemListByDay = itemListByDay;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;
//        if(viewType==R.layout.null_item_page){
//            view = View.inflate(context, R.layout.null_item_page, null);
//        }else{
//            view = View.inflate(context, R.layout.re_item, null);
//        }
        view = View.inflate(context, R.layout.re_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskVo item = itemListByDay.get(position);
        holder.name.setText(item.getTaskName());
        if(itemListByDay.get(position).getTaskStatus()==2){
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.time.setText(item.getClockDuration()+" 分钟");
        Glide.with(context).asBitmap().load(item.getBackground()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(resource);
                holder.backGroundLin.setBackground(drawable);
            }
        });

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("正向计时".equals(itemListByDay.get(position).getClockDuration()+" 分钟")){
                    //正向计时：
                    intent = new Intent();
                    intent.setClass(context, TimerActivity.class);
                    intent.putExtra("method", "forWard");
                    intent.putExtra("name", itemListByDay.get(position).getTaskName());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holder.btn,"fab").toBundle());
                } else if ("普通待办".equals(itemListByDay.get(position).getClockDuration()+" 分钟")) {
                    //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    SpannableString spannableString = new SpannableString(itemListByDay.get(position).getTaskName());
                    spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    itemListByDay.get(position).setTaskName(spannableString.toString());
//                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                }else{
                    //倒计时：
                    intent = new Intent();
                    String[] parts = holder.time.getText().toString().split(" ");
                    String num = parts[0];
//                int num = Integer.parseInt(parts[0]);
                    intent.putExtra("time", num);
                    intent.putExtra("method", "countDown");
                    intent.putExtra("name", itemListByDay.get(position).getTaskName());
                    intent.setClass(context, TimerActivity.class);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holder.btn,"fab").toBundle());
                }
            }
        });

        //点击查看数据
        holder.statistics.setOnClickListener(view -> {
            //设置弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                            TaskApi.removeTask(itemListByDay.get(position).getTaskId());
                                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                                            itemListByDay.remove(itemListByDay.get(position));
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

                    Toast.makeText(context, "你点击了"+itemListByDay.get(position).getTaskName(), Toast.LENGTH_SHORT).show();

                    //设置弹窗：
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.todo_fragment_add_item_pop_window,null);
                    getViews(dialogView);//获取控件
                    final Dialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setContentView(dialogView);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    itemName.setText(itemListByDay.get(position).getTaskName());
                    textRemark.setText(itemListByDay.get(position).getRemark());
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
                                            estimate.addAll(itemListByDay.get(position).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(itemListByDay.get(position).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(itemListByDay.get(position).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(itemListByDay.get(position).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(itemListByDay.get(position).getAgain());
                                        }
                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        for (TaskVo vo : itemListByDay) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                itemListByDay.remove(vo);
                                                itemListByDay.add(taskVo);
                                            }
                                        }
                                        notifyDataSetChanged();
                                    } else if (setTimeGroupTwo.isChecked()) {
                                        String strings = setTimeGroupTwo.getText().toString().split(" ")[0];

                                        ArrayList<Integer> estimate = new ArrayList<>();
                                        if(map.size()!=0 && map.get("clockTimes")!=null){
                                            estimate.add(Integer.valueOf(map.get("clockTimes")));
                                        }else{
                                            estimate.addAll(itemListByDay.get(position).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(itemListByDay.get(position).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(itemListByDay.get(position).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(itemListByDay.get(position).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(itemListByDay.get(position).getAgain());
                                        }

                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        for (TaskVo vo : itemListByDay) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                itemListByDay.remove(vo);
                                                itemListByDay.add(taskVo);
                                            }
                                        }
                                        notifyDataSetChanged();
                                    }else{
                                        String strings = setTimeGroupThree.getText().toString().split(" ")[0];

                                        ArrayList<Integer> estimate = new ArrayList<>();
                                        if(map.size()!=0 && map.get("clockTimes")!=null){
                                            estimate.add(Integer.valueOf(map.get("clockTimes")));
                                        }else{
                                            estimate.addAll(itemListByDay.get(position).getEstimate());
                                        }
                                        TaskDto taskDto = new TaskDto();
                                        taskDto.setTaskName(itemName.getText().toString());
                                        taskDto.setEstimate(estimate);
                                        taskDto.setClockDuration(Integer.valueOf(strings.trim()));
                                        taskDto.setTaskId(itemListByDay.get(position).getTaskId());
                                        if(map.size()!=0 && map.get("remark")!=null){
                                            taskDto.setRemark(map.get("remark"));
                                        }else{
                                            taskDto.setRemark(itemListByDay.get(position).getRemark());
                                        }
                                        if(map.size()!=0 && map.get("rest")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("rest")));
                                        }else{
                                            taskDto.setRestTime(itemListByDay.get(position).getRestTime());
                                        }
                                        if(map.size()!=0 && map.get("again")!=null){
                                            taskDto.setRestTime(Integer.valueOf(map.get("again")));
                                        }else{
                                            taskDto.setAgain(itemListByDay.get(position).getAgain());
                                        }

                                        TaskVo taskVo = TaskApi.updateTask(taskDto);
                                        for (TaskVo vo : itemListByDay) {
                                            if(vo.getTaskId()==taskVo.getTaskId()){
                                                itemListByDay.remove(vo);
                                                itemListByDay.add(taskVo);
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

    @Override
    public int getItemCount() {
        return itemListByDay.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout statistics;
        TextView name;
        TextView time;
        Button btn;
        LinearLayout backGroundLin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statistics  =itemView.findViewById(R.id.click_statistics);
            name = itemView.findViewById(R.id.re_item_txt_name);
            time = itemView.findViewById(R.id.re_item_txt_time);
            btn = itemView.findViewById(R.id.re_item_ry_btn);
            backGroundLin = itemView.findViewById(R.id.re_item_background_lin);
        }
    }



    // 定义一个接口，用于传递点击事件
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
