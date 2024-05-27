package net.onest.time.adapter.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import net.onest.time.R
import net.onest.time.TimerActivity
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskMoreDialog
import net.onest.time.components.TaskInfoDialog
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.entity.list.TaskCollections

/**
 * 待办集的Adapter
 */
class ExpandableListAdapter(
        private val itemViewId: Int,
        private val childViewId: Int,
        private val context: Context,
        private var taskCollectionsList: List<TaskCollections>
) : BaseExpandableListAdapter() {
    var childItemList: List<TaskVo>? = null
    private var intent: Intent? = null


    //返回列表项数量
    override fun getGroupCount() = taskCollectionsList.size

    //返回子列表项数量
    override fun getChildrenCount(groupPosition: Int) = taskCollectionsList[groupPosition].tasks.size

    //获得指定列表项数据
    override fun getGroup(groupPosition: Int) = taskCollectionsList[groupPosition]

    //获得指定子列表项数据
    override fun getChild(groupPosition: Int, childPosition: Int): TaskVo = taskCollectionsList[groupPosition].tasks[childPosition]

    //获得父列表id
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    //获得子列表id
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()

    //指定位置相应的组视图
    override fun hasStableIds() = true

    //设置父列表的view
    override fun getGroupView(
            groupPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        if (convertView != null)
            return convertView

        val view = LayoutInflater.from(context).inflate(itemViewId, parent, false)

        val backView = view.findViewById<View>(R.id.list_fragment_parent_item_color)
        val textView = view.findViewById<TextView>(R.id.list_fragment_parent_item_name)
        val expandBtn = view.findViewById<Button>(R.id.list_fragment_parent_arrow)
        val dataBtn = view.findViewById<Button>(R.id.list_fragment_parent_data)
        val addBtn = view.findViewById<Button>(R.id.list_fragment_parent_add)

        val taskCollections = taskCollectionsList[groupPosition]
        val tasks: List<TaskVo> = taskCollections.tasks
        val category: String = taskCollections.taskCollectionsName

        // 设置为不可点击，将事件传递给父组件
        expandBtn.isClickable = false

        // 展示数据统计的按钮
        dataBtn.setOnClickListener { v: View? ->
            val toast = Toast.makeText(context, "该功能尚未完善😙", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }

        // 添加任务按钮
        addBtn.setOnClickListener { v: View? ->
            AddTaskMoreDialog(
                    context, taskCollections.taskCollectionsId, tasks as MutableList<TaskVo>, AdapterHolder(
                    this@ExpandableListAdapter
            )
            )
        }

        backView.setBackgroundColor(taskCollectionsList[groupPosition].taskCollectionsColor)
        textView.text = taskCollectionsList[groupPosition].taskCollectionsName
        return view
    }

    //设置子列表的view
    @SuppressLint("SetTextI18n")
    override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        if (convertView != null) return convertView
        val view = LayoutInflater.from(context).inflate(childViewId, parent, false)

        val backLin = view.findViewById<LinearLayout>(R.id.list_fragment_item_child_background_lin)
        val childName = view.findViewById<TextView>(R.id.list_fragment_item_child_txt_name)
        val childTime = view.findViewById<TextView>(R.id.list_fragment_item_child_txt_time)
        val startBtn = view.findViewById<Button>(R.id.list_fragment_item_child_ry_btn)
        val statistics = view.findViewById<RelativeLayout>(R.id.list_click_statistics)

        val tasks: List<TaskVo> = taskCollectionsList[groupPosition].tasks
        val taskVo = tasks[childPosition]

        Glide.with(context).asBitmap().load(taskVo.background)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                    ) {
                        val drawable: Drawable = BitmapDrawable(resource)
                        backLin.background = drawable
                    }
                })
        //        backLin.setBackgroundColor(taskVo.getColor());
        childName.text = taskVo.taskName
        when (taskVo.type) {
            0 -> {
                childTime.text = "${taskVo.clockDuration} 分钟"
            }

            1 -> {
                childTime.text = "正向计时"
            }
            2 -> {
                childTime.text = "普通待办"
            }
        }


        //编辑数据：
        statistics.setOnClickListener { views: View ->
            //设置弹窗
            TaskInfoDialog(views.context, taskVo, tasks, AdapterHolder(this@ExpandableListAdapter))
        }


        startBtn.setOnClickListener {
            if (taskCollectionsList[groupPosition].tasks[childPosition].type == 0) {
                // 正向计时
                intent = Intent()
                intent!!.setClass(context, TimerActivity::class.java)
                intent!!.putExtra("method", "forWard")
                context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                        context as Activity, startBtn, "fab"
                ).toBundle()
                )
            } else if (taskCollectionsList[groupPosition].tasks[childPosition].type == 2) {
                // 普通待办 不计时
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                val spannableString = SpannableString(
                        taskCollectionsList[groupPosition].tasks.get(childPosition)
                                .getTaskName()
                )
                spannableString.setSpan(
                        StrikethroughSpan(),
                        0,
                        spannableString.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                taskCollectionsList[groupPosition].tasks.get(childPosition)
                        .setTaskName(spannableString.toString())
                //                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
            } else {
                // 倒计时：
                intent = Intent()
                val parts =
                        childTime.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                val num = parts[0]
                //                int num = Integer.parseInt(parts[0]);
                intent!!.putExtra("time", num)
                intent!!.putExtra("method", "countDown")
                intent!!.setClass(context, TimerActivity::class.java)
                context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                        context as Activity, startBtn, "fab"
                ).toBundle()
                )
            }
        }
        return view
    }

    //当选择子节点的时候，调用该方法(点击二级列表)
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
