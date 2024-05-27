package net.onest.time.adapter.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.TimerActivity
import net.onest.time.api.TaskApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.TaskInfoDialog
import net.onest.time.components.holder.AdapterHolder

class TodoItemAdapter(
    var context: Context,
    var tasks: List<TaskVo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private var intent: Intent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = when (viewType) {
            VIEW_TYPE_EMPTY -> {
                LayoutInflater.from(context)
                    .inflate(R.layout.null_item_page, parent, false)
            }

            VIEW_TYPE_ITEM -> {
                LayoutInflater.from(context)
                    .inflate(R.layout.re_item, parent, false)
            }

            else -> null
        }

        return ViewHolder(view!!)
    }


    override fun getItemViewType(position: Int): Int {
        if (tasks.isEmpty()) {
            return VIEW_TYPE_EMPTY //返回空布局
        }
        return VIEW_TYPE_ITEM //返回正常布局
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (tasks.isEmpty()) {
            return
        }

        if (holder is ViewHolder) {
            val task = tasks[position]

            // 任务类型
            when (task.type) {
                0 -> holder.time?.text = "${task.clockDuration} 分钟"
                1 -> holder.time?.text = "正向计时"
                2 -> holder.time?.text = "普通待办"
            }

            // 任务名
            holder.name?.text = task.taskName

            if (tasks[position].taskStatus == 2) {
                holder.name?.paintFlags = holder.name?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG)!!
                //                SpannableString spannableString = new SpannableString(itemListByDay.get(position).getTaskName());
//                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Glide.with(context).asBitmap().load(task.background)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val drawable: Drawable = BitmapDrawable(resource)
                        holder.backGroundLin?.background = drawable
                    }

                })

            holder.btn?.setOnClickListener {
                when (holder.time?.text.toString()) {
                    "正向计时" -> {
                        // 正向计时
                        intent = Intent()
                        intent!!.setClass(context, TimerActivity::class.java)
                        intent!!.putExtra("method", "forWard")
                        intent!!.putExtra("name", tasks[position].taskName)
                        context.startActivity(
                            intent, ActivityOptions.makeSceneTransitionAnimation(
                                context as Activity, holder.btn, "fab"
                            ).toBundle()
                        )
                    }

                    "普通待办" -> {
                        // 不计时
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        XPopup.Builder(context)
                            .asConfirm(
                                "", "该待办为不计时待办，点击确认完成即可完成一次。\n \n确定要完成一次吗？"
                            ) {
                                TaskApi.removeTask(task.taskId)
                                notifyDataSetChanged()
                            }
                            .show()
                        //                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                    }

                    else -> {
                        // 倒计时
                        // 添加番茄钟
//                        TomatoClockApi.addTomatoClock(itemListByDay.get(position).getTaskId());
                        intent = Intent()
                        val parts = holder.time?.text.toString().split(" ".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val num = parts[0]
                        //                int num = Integer.parseInt(parts[0]);
                        intent!!.putExtra("time", num)
                        intent!!.putExtra("method", "countDown")
                        intent!!.putExtra("name", tasks[position].taskName)
                        intent!!.putExtra("taskId", tasks[position].taskId)
                        intent!!.putExtra("start", "go")
                        intent!!.setClass(context, TimerActivity::class.java)
                        context.startActivity(
                            intent, ActivityOptions.makeSceneTransitionAnimation(
                                context as Activity, holder.btn, "fab"
                            ).toBundle()
                        )
                    }
                }
            }

            //编辑数据
            holder.statistics?.setOnClickListener { view: View? ->
                TaskInfoDialog(
                    context, task, tasks, AdapterHolder(this@TodoItemAdapter)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        if (tasks.isEmpty()) {
            return 1
        }
        return tasks.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var statistics: RelativeLayout? = itemView.findViewById(R.id.click_statistics)
        var name: TextView? = itemView.findViewById(R.id.re_item_txt_name)
        var time: TextView? = itemView.findViewById(R.id.re_item_txt_time)
        var btn: Button? = itemView.findViewById(R.id.re_item_ry_btn)
        var backGroundLin: LinearLayout? = itemView.findViewById(R.id.re_item_background_lin)
    }


    // 定义一个接口，用于传递点击事件
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // 设置点击监听器
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mItemClickListener = listener
    }

    companion object {
        // 有task 非空列表
        private const val VIEW_TYPE_ITEM = 1

        // 无task 空列表
        private const val VIEW_TYPE_EMPTY = 0
    }
}
