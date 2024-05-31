package net.onest.time.adapter.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
import net.onest.time.MyTextView
import net.onest.time.R
import net.onest.time.TimerActivity
import net.onest.time.api.TaskApi
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.TaskInfoDialog
import net.onest.time.components.holder.AdapterHolder

class TodoItemAdapter(private val context: Context, itemListByDay: List<TaskVo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemListByDay: List<TaskVo> = ArrayList()

    private var mItemClickListener: OnItemClickListener? = null
    private var intent: Intent? = null

    init {
        this.itemListByDay = itemListByDay
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == VIEW_TYPE_EMPTY) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.null_item_page, parent, false)
            //            view = View.inflate(context, R.layout.null_item_page, null);
            return object : RecyclerView.ViewHolder(view) {}
        }

        view = View.inflate(context, R.layout.re_item, null)
        return MyViewHolder(view)
    }


    override fun getItemViewType(position: Int): Int {
        if (itemListByDay.isEmpty()) {
            return VIEW_TYPE_EMPTY //返回空布局
        }
        return VIEW_TYPE_ITEM //返回正常布局
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val holders = holder
            val task = itemListByDay[position]

            if (task.type == 0) {
                holders.time.text = task.clockDuration.toString() + " 分钟"
            } else if (task.type == 1) {
                holders.time.text = "正向计时"
            } else {
                holders.time.text = "普通待办"
            }

            if (itemListByDay[position].taskStatus == 2) {
                //完成状态==设置删除线:
                holders.name.text = task.taskName
                holders.name.setDeleteLineColor(Color.parseColor("#ffffff")) //设置删除线的颜色
                holders.name.setShowDeleteLine(true) //删除线是否显示
                holders.name.setDeleteLineWidth(context, 3) //删除线显示宽度
                //                holders.name.setPaintFlags(holders.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                SpannableString spannableString = new SpannableString(itemListByDay.get(position).getTaskName());
//                spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                holders.name.text = task.taskName
                holders.name.setShowDeleteLine(false)
            }

            Glide.with(context).asBitmap().load(task.background)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val drawable: Drawable = BitmapDrawable(resource)
                        holders.backGroundLin.background = drawable
                    }

                })

            holders.btn.setOnClickListener {
                if ("正向计时" == holders.time.text.toString()) {
                    //正向计时：
                    intent = Intent()
                    intent!!.setClass(context, TimerActivity::class.java)
                    intent!!.putExtra("method", "forWard")
                    intent!!.putExtra("name", itemListByDay[position].taskName)
                    intent!!.putExtra("taskId", itemListByDay[position].taskId)
                    context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                            context as Activity, holders.btn, "fab"
                        ).toBundle()
                    )
                } else if ("普通待办" == holders.time.text.toString()) {
                    //不计时：
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    XPopup.Builder(context)
                        .asConfirm(
                            "", "该待办为不计时待办，点击确认完成即可完成一次。\n \n确定要完成一次吗？"
                        ) {
                            TaskApi.complete(task.taskId) //完成任务
                            notifyItemChanged(itemListByDay.indexOf(task))
                        }
                        .show()
                    //                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                } else {
                    //添加番茄钟:
//                        TomatoClockApi.addTomatoClock(itemListByDay.get(position).getTaskId());
                    //倒计时：
                    intent = Intent()
                    val parts = holders.time.text.toString().split(" ".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val num = parts[0]
                    //                int num = Integer.parseInt(parts[0]);
                    intent?.run {
                        putExtra("task",task)
                    }
                    intent!!.putExtra("time", num)
                    intent!!.putExtra("method", "countDown")
                    intent!!.putExtra("name", itemListByDay[position].taskName)
                    intent!!.putExtra("taskId", itemListByDay[position].taskId)
                    intent!!.putExtra("start", "go")
                    intent!!.setClass(context, TimerActivity::class.java)
                    context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                            context as Activity, holders.btn, "fab"
                        ).toBundle()
                    )
                }
            }

            //编辑数据
            holders.statistics.setOnClickListener { view: View? ->
                TaskInfoDialog(
                    context, task, itemListByDay, AdapterHolder(this@TodoItemAdapter)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        if (itemListByDay.isEmpty()) {
            return 1
        }
        return itemListByDay.size
    }

    internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var statistics: RelativeLayout = itemView.findViewById(R.id.click_statistics)
        var name: MyTextView = itemView.findViewById(R.id.re_item_txt_name)
        var time: TextView = itemView.findViewById(R.id.re_item_txt_time)
        var btn: Button = itemView.findViewById(R.id.re_item_ry_btn)
        var backGroundLin: LinearLayout = itemView.findViewById(R.id.re_item_background_lin)
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
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_EMPTY = 0
    }
}
