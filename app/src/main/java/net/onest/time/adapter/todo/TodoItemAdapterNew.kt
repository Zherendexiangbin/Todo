package net.onest.time.adapter.todo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.onest.time.R
import net.onest.time.api.vo.TaskVo


class TodoItemAdapterNew(layoutResId: Int, data: MutableList<TaskVo>?) :
    BaseQuickAdapter<TaskVo, BaseViewHolder>(layoutResId, data) {
    var taskVoList: List<TaskVo>? = null

    override fun convert(baseViewHolder: BaseViewHolder, taskVo: TaskVo) {
        baseViewHolder.setText(R.id.re_item_txt_name, taskVo.taskName)
        baseViewHolder.setText(R.id.re_item_txt_time, taskVo.clockDuration.toString() + " 分钟")
        Glide.with(context).asBitmap().load(taskVo.background)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val drawable: Drawable = BitmapDrawable(resource)
                    //                baseViewHolder.setBackgroundResource(R.id.re_item_background_lin,drawable);
//                baseViewHolder.setImageDrawable(R.id.re_item_background_lin,drawable);
                    val res = context.resources
                    val resName = "drawable_" + System.currentTimeMillis() // 生成一个随机的资源名称
                    val resId = res.getIdentifier(resName, "drawable", context.packageName)
                    baseViewHolder.setBackgroundResource(R.id.re_item_background_lin, resId)
                }
            })
    }
}
