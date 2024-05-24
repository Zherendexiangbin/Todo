package net.onest.time.adapter.list;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.AddTaskMoreDialog;
import net.onest.time.components.TaskInfoDialog;
import net.onest.time.components.holder.AdapterHolder;
import net.onest.time.entity.list.ParentItem;

import java.util.List;

/**
 * 待办集的Adapter
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final int itemViewId;
    private final int childViewId;
    private List<ParentItem> parentItemList;
    private List<TaskVo> childItemList;
    private Intent intent;


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
        if (convertView != null)
            return convertView;
        View view = LayoutInflater.from(context).inflate(itemViewId, parent, false);

        View backView = view.findViewById(R.id.list_fragment_parent_item_color);
        TextView textView = view.findViewById(R.id.list_fragment_parent_item_name);
        Button expandBtn = view.findViewById(R.id.list_fragment_parent_arrow);
        Button dataBtn = view.findViewById(R.id.list_fragment_parent_data);
        Button addBtn = view.findViewById(R.id.list_fragment_parent_add);

        ParentItem parentItem = parentItemList.get(groupPosition);
        List<TaskVo> tasks = parentItem.getChildItemList();
        String category = parentItem.getParentItemName();

        // 设置为不可点击，将事件传递给父组件
        expandBtn.setClickable(false);

        // 展示数据统计的按钮
        dataBtn.setOnClickListener(v -> {
            Toast toast = Toast.makeText(context, "该功能尚未完善😙", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        });

        // 添加任务按钮
        addBtn.setOnClickListener(v -> new AddTaskMoreDialog(context, category, tasks, new AdapterHolder(ExpandableListAdapter.this)));

        backView.setBackgroundColor(parentItemList.get(groupPosition).getParentItemColor());
        textView.setText(parentItemList.get(groupPosition).getParentItemName());
        return view;
    }

    //设置子列表的view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView != null)
            return convertView;
        View view = LayoutInflater.from(context).inflate(childViewId, parent, false);

        LinearLayout backLin = view.findViewById(R.id.list_fragment_item_child_background_lin);
        TextView childName = view.findViewById(R.id.list_fragment_item_child_txt_name);
        TextView childTime = view.findViewById(R.id.list_fragment_item_child_txt_time);
        Button startBtn = view.findViewById(R.id.list_fragment_item_child_ry_btn);
        RelativeLayout statistics = view.findViewById(R.id.list_click_statistics);

        List<TaskVo> tasks = parentItemList.get(groupPosition).getChildItemList();
        TaskVo taskVo = tasks.get(childPosition);

        Glide.with(context).asBitmap().load(taskVo.getBackground()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(resource);
                backLin.setBackground(drawable);
            }
        });
//        backLin.setBackgroundColor(taskVo.getColor());
        childName.setText(taskVo.getTaskName());
        childTime.setText(taskVo.getClockDuration() + " 分钟");

        //编辑数据：
        statistics.setOnClickListener(views -> {
            //设置弹窗
            new TaskInfoDialog(views.getContext(), taskVo, tasks, new AdapterHolder(ExpandableListAdapter.this));
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("正向计时".equals(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getClockDuration() + "")) {
                    //正向计时：
                    intent = new Intent();
                    intent.setClass(context, TimerActivity.class);
                    intent.putExtra("method", "forWard");
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, startBtn, "fab").toBundle());
                } else if ("普通待办".equals(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getClockDuration() + "")) {
                    //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    SpannableString spannableString = new SpannableString(parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName());
                    spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    parentItemList.get(groupPosition).getChildItemList().get(childPosition).setTaskName(spannableString.toString());
//                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                } else {
                    //倒计时：
                    intent = new Intent();
                    String[] parts = childTime.getText().toString().split(" ");
                    String num = parts[0];
//                int num = Integer.parseInt(parts[0]);
                    intent.putExtra("time", num);
                    intent.putExtra("method", "countDown");
                    intent.setClass(context, TimerActivity.class);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, startBtn, "fab").toBundle());
                }
            }
        });

        return view;
    }

    //当选择子节点的时候，调用该方法(点击二级列表)
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
