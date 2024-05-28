package net.onest.time.adapter.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.XPopup;

import net.onest.time.MyTextView;
import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.TaskInfoDialog;
import net.onest.time.components.holder.AdapterHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private List<TaskVo> itemListByDay  = new ArrayList<>();

    private OnItemClickListener mItemClickListener;
    private Intent intent;

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

    public void setItemListByDay(List<TaskVo> itemListByDay) {
        this.itemListByDay = itemListByDay;
    }

    public List<TaskVo> getItemListByDay() {
        return itemListByDay;
    }

    public TodoItemAdapter(Context context, List<TaskVo> itemListByDay) {
        this.context = context;
        this.itemListByDay = itemListByDay;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;
        if(viewType==VIEW_TYPE_EMPTY){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.null_item_page, parent, false);
//            view = View.inflate(context, R.layout.null_item_page, null);
            return new RecyclerView.ViewHolder(view) {};
        }

        view = View.inflate(context, R.layout.re_item, null);
        return new MyViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        if(itemListByDay.isEmpty()){
            return VIEW_TYPE_EMPTY;//返回空布局
        }
        return VIEW_TYPE_ITEM;//返回正常布局
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder holders = (MyViewHolder) holder;
            TaskVo task = itemListByDay.get(position);

            if(Objects.equals(task.getType(),0)){
                holders.time.setText(task.getClockDuration()+" 分钟");
            }else if(Objects.equals(task.getType(),1)){
                holders.time.setText("正向计时");
            }else{
                holders.time.setText("普通待办");
            }

            if(itemListByDay.get(position).getTaskStatus()==2){
                //完成状态==设置删除线:
                holders.name.setText(task.getTaskName());
//                holders.name.setDeleteLineColor(Color.parseColor("#0000ff"));//设置删除线的颜色
                holders.name.setShowDeleteLine(true);//删除线是否显示
                holders.name.setDeleteLineWidth(context,2);//删除线显示宽度
//                holders.name.setPaintFlags(holders.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                SpannableString spannableString = new SpannableString(itemListByDay.get(position).getTaskName());
//                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                holders.name.setText(task.getTaskName());
                holders.name.setShowDeleteLine(false);
            }

            Glide.with(context).asBitmap().load(task.getBackground()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(resource);
                    holders.backGroundLin.setBackground(drawable);
                }
            });

            holders.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("正向计时".equals(holders.time.getText().toString())){
                        //正向计时：
                        intent = new Intent();
                        intent.setClass(context, TimerActivity.class);
                        intent.putExtra("method", "forWard");
                        intent.putExtra("name", itemListByDay.get(position).getTaskName());
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holders.btn,"fab").toBundle());
                    } else if ("普通待办".equals(holders.time.getText().toString())) {
                        //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        new XPopup
                                .Builder(context)
                                .asConfirm("","该待办为不计时待办，点击确认完成即可完成一次。\n \n确定要完成一次吗？",
                                        () -> {
//                                            Log.e("yes",task.getTaskName());
//                                            TaskApi.removeTask(task.getTaskId());
                                            TaskApi.complete(task.getTaskId());//完成任务
                                            notifyItemChanged(itemListByDay.indexOf(task));
//
                                        })
                                .show();
//                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                    }else{
                        //添加番茄钟:
//                        TomatoClockApi.addTomatoClock(itemListByDay.get(position).getTaskId());
                        //倒计时：
                        intent = new Intent();
                        String[] parts = holders.time.getText().toString().split(" ");
                        String num = parts[0];
//                int num = Integer.parseInt(parts[0]);
                        intent.putExtra("time", num);
                        intent.putExtra("method", "countDown");
                        intent.putExtra("name", itemListByDay.get(position).getTaskName());
                        intent.putExtra("taskId",itemListByDay.get(position).getTaskId());
                        intent.putExtra("start","go");
                        intent.setClass(context, TimerActivity.class);
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holders.btn,"fab").toBundle());
                    }
                }
            });

            //编辑数据
            holders.statistics.setOnClickListener(view -> {
                new TaskInfoDialog(context, task, itemListByDay, new AdapterHolder(TodoItemAdapter.this));
            });
        }
    }

    @Override
    public int getItemCount() {
        if(itemListByDay.isEmpty()){
            return 1;
        }
        return itemListByDay.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout statistics;
        MyTextView name;
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
