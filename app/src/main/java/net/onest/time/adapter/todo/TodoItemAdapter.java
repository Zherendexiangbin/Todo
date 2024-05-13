package net.onest.time.adapter.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.MainActivity;
import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.entity.Item;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.fragment.TodoFragment;

import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyViewHolder>{
    private Context context;
    private List<Item> itemList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private Intent intent;

    public TodoItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.re_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item news = itemList.get(position);
        holder.name.setText(news.getItemName());
        holder.time.setText(news.getTime());
//        holder.backGroundLin.setBackgroundColor(news.getColor());
        holder.backGroundLin.setBackground(news.getDrawable());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("正向计时".equals(itemList.get(position).getTime())){
                    //正向计时：
                    intent = new Intent();
                    intent.setClass(context, TimerActivity.class);
                    intent.putExtra("method", "forWard");
                    intent.putExtra("name", itemList.get(position).getItemName());
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holder.btn,"fab").toBundle());
                } else if ("普通待办".equals(itemList.get(position).getTime())) {
                    //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    SpannableString spannableString = new SpannableString(itemList.get(position).getItemName());
                    spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    itemList.get(position).setItemName(spannableString.toString());
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
                    intent.putExtra("name", itemList.get(position).getItemName());
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
        });
    }

    private void setViews(View dialogView) {
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

    @Override
    public int getItemCount() {
        return itemList.size();
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
