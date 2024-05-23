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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

public class TaskInfoDialog extends AlertDialog {
    private final Context context;
    private final TaskVo task;  // 存储当前交互的taskVo
    private final List<TaskVo> tasks;   // 管理的列表
    private final AdapterHolder adapter;    // 适配器


    //点击childItem弹窗：
    private TextView title,learnFrequency, learnTime,textRemark;
    private Button changeBackground, setItem, moveItem, deleteItem, timing;
    private LinearLayout learnHistory, learnStatistics;

    public TaskInfoDialog(@NonNull Context context, TaskVo task, List<TaskVo> tasks, AdapterHolder adapter) {
        super(context);
        this.context = context;
        this.task = task;
        this.tasks = tasks;
        this.adapter = adapter;

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.item_pop, null);
        findViews(dialogView);
        setListeners();
        show();
        getWindow().setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setListeners() {
        //删除:
        deleteItem.setOnClickListener(v -> {
            new DeleteTaskDialog(context, task, tasks, adapter, this)
                    .show();
        });

        //编辑
        setItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateTaskDialog(context, task, tasks, adapter, TaskInfoDialog.this);
            }
        });
    }

    private void findViews(View dialogView) {
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
