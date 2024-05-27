package net.onest.time.adapter.todo;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import net.onest.time.R;
import net.onest.time.api.vo.TaskVo;

import java.util.List;

public class TodoItemAdapterNew extends BaseQuickAdapter<TaskVo, BaseViewHolder> {
    private List<TaskVo> taskVoList;

    public TodoItemAdapterNew(int layoutResId, @Nullable List<TaskVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TaskVo taskVo) {
        baseViewHolder.setText(R.id.re_item_txt_name,taskVo.getTaskName());
        baseViewHolder.setText(R.id.re_item_txt_time,taskVo.getClockDuration()+" 分钟");
        Glide.with(getContext()).asBitmap().load(taskVo.getBackground()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(resource);
//                baseViewHolder.setBackgroundResource(R.id.re_item_background_lin,drawable);
//                baseViewHolder.setImageDrawable(R.id.re_item_background_lin,drawable);
                Resources res = getContext().getResources();
                String resName = "drawable_" + System.currentTimeMillis(); // 生成一个随机的资源名称
                int resId = res.getIdentifier(resName, "drawable", getContext().getPackageName());
                baseViewHolder.setBackgroundResource(R.id.re_item_background_lin,resId);
            }
        });
    }

    public List<TaskVo> getTaskVoList() {
        return taskVoList;
    }

    public void setTaskVoList(List<TaskVo> taskVoList) {
        this.taskVoList = taskVoList;
    }
}
