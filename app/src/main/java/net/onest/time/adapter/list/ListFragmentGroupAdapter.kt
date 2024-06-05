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
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.TimerActivity
import net.onest.time.adapter.list.ListFragmentGroupAdapter.ChildVH
import net.onest.time.adapter.list.ListFragmentGroupAdapter.GroupVH
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskMoreDialog
import net.onest.time.components.TaskInfoDialog
import net.onest.time.components.UpdateCategoryDialog
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.databinding.ListFragmentExpandableChildListBinding
import net.onest.time.databinding.ListFragmentExpandableParentListBinding
import net.onest.time.utils.showToast

class ListFragmentGroupAdapter(
    var context: Context,
    var taskCategoryList: MutableList<TaskCategoryVo>,
) : BaseExpandableRecyclerViewAdapter<
        TaskCategoryVo,
        TaskVo?,
        GroupVH?,
        ChildVH?>() {
    lateinit var parentBinding: ListFragmentExpandableParentListBinding
    lateinit var childBinding: ListFragmentExpandableChildListBinding

    private var intent: Intent? = null

    init {

        this.setListener(object :
            ExpandableRecyclerViewOnClickListener<TaskCategoryVo, TaskVo?> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onGroupLongClicked(groupItem: TaskCategoryVo?): Boolean {

                val bottomSheetDialog = BottomSheetDialog(context);
                //不传第二个参数
                //BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

                // 底部弹出的布局
                val bottomView = LayoutInflater.from(context)
                    .inflate(R.layout.list_fragment_parent_edit, null);
                bottomSheetDialog.setContentView(bottomView)
                //设置点击dialog外部消失
                bottomSheetDialog.setCanceledOnTouchOutside(true)

                bottomSheetDialog.show()
                val cancel = bottomView.findViewById<Button>(R.id.parent_btn_cancel)
                val edit = bottomView.findViewById<Button>(R.id.parent_btn_edit)
                val delete = bottomView.findViewById<Button>(R.id.parent_btn_delete)

                cancel.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                edit.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    UpdateCategoryDialog(
                        context,
                        groupItem!!,
                        this@ListFragmentGroupAdapter
                    )
                }

                delete.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    XPopup.Builder(context)
                        .asConfirm("",
                            "你确定要删除${groupItem?.categoryName}待办集吗？") {
                            try {
                                TaskCategoryApi.deleteTaskCategory(groupItem?.categoryId)
                                "删除成功！".showToast()
                            } catch (e: RuntimeException) {
                                e.message?.showToast()
                            }

                            taskCategoryList.removeAt(taskCategoryList.indexOf(groupItem))
                            notifyDataSetChanged()
                        }
                        .show()
                }
                return true // 返回true表示消费了长按事件
            }

            override fun onInterceptGroupExpandEvent(
                groupItem: TaskCategoryVo?,
                isExpand: Boolean
            ): Boolean {
                return false
            }

            override fun onGroupClicked(groupItem: TaskCategoryVo?) {
//                parentBinding.listFragmentParentArrow.setBackgroundResource(R.drawable.arrow_down2)
            }

            override fun onChildClicked(groupItem: TaskCategoryVo?, childItem: TaskVo?) {
                TaskInfoDialog(context, childItem!!, groupItem!!.taskVos, AdapterHolder(this@ListFragmentGroupAdapter))
            }

        })
    }

    override fun getGroupCount() = taskCategoryList.size

    override fun getGroupItem(groupIndex: Int) = taskCategoryList[groupIndex]

    override fun onCreateGroupViewHolder(parent: ViewGroup, groupViewType: Int): GroupVH {
        parentBinding = ListFragmentExpandableParentListBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return GroupVH(parentBinding.root)
    }

    override fun onBindGroupViewHolder(
        holder: GroupVH?,
        groupBean: TaskCategoryVo,
        isExpand: Boolean
    ) {
        holder?.run {
            nameTv.text = groupBean.categoryName
            addBtn.setOnClickListener {
                groupBean.categoryId?.let {
                    AddTaskMoreDialog(
                        context, it, groupBean.taskVos as MutableList<TaskVo>, AdapterHolder(
                            this@ListFragmentGroupAdapter
                        )
                    )
                }
            }
            color.setBackgroundColor(groupBean.color)

            arrow.isClickable = false
        }
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, childViewType: Int): ChildVH {
        childBinding = ListFragmentExpandableChildListBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ChildVH(childBinding.root)
    }

    override fun onBindChildViewHolder(
        childVH: ChildVH?,
        groupBean: TaskCategoryVo,
        taskVo: TaskVo?
    ) {
        childVH?.run {
            nameTv.text = taskVo?.taskName
            timeTv.text = "${taskVo?.clockDuration} 分钟"

            Glide.with(context).asBitmap().load(taskVo?.background)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val drawable: Drawable = BitmapDrawable(resource)
                        childVH.background.background = drawable
                    }
                })

            //点击跳转时钟:
            startBtn.setOnClickListener {
                if (taskVo?.type == 1) {
                    // 正向计时
                    intent = Intent()
                    intent!!.setClass(context, TimerActivity::class.java)
                    intent!!.putExtra("method", "forWard")
                    intent!!.putExtra("task", taskVo)
                    intent!!.putExtra("name", taskVo.taskName)
                    context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                            context as Activity, startBtn, "fab"
                        ).toBundle()
                    )
                } else if (taskVo?.type == 2) {
                    // 普通待办 不计时
//                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    val spannableString = SpannableString(
                        taskVo.taskName
                    )
                    spannableString.setSpan(
                        StrikethroughSpan(),
                        0,
                        spannableString.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    taskVo.taskName = spannableString.toString()
                    //                    TextView textView = findViewById(R.id.textView);
//                    textView.setText(spannableString);
                } else {
                    // 倒计时：
                    intent = Intent()
                    val parts =
                        timeTv.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val num = parts[0]
                    //                int num = Integer.parseInt(parts[0]);
                    intent!!.putExtra("time", num)
                    intent!!.putExtra("method", "countDown")
                    intent!!.putExtra("name", taskVo?.taskName)
                    intent!!.putExtra("taskId", taskVo?.taskId)
                    intent!!.putExtra("start", "go")
                    intent!!.putExtra("task", taskVo)
                    intent!!.setClass(context, TimerActivity::class.java)
                    context.startActivity(
                        intent, ActivityOptions.makeSceneTransitionAnimation(
                            context as Activity, startBtn, "fab"
                        ).toBundle()
                    )
                }
            }
        }
    }


//    operator fun getValue(nothing: Nothing?, property: KProperty<*>): MutableList<TaskCategoryVo> {
//        return taskCategoryList
//    }
//
//    operator fun setValue(nothing: Nothing?, property: KProperty<*>, taskCategoryVos: MutableList<TaskCategoryVo>) {
//        this.taskCategoryList = taskCategoryVos
//    }


    inner class GroupVH(itemView: View?) : BaseGroupViewHolder(itemView) {
        val nameTv = parentBinding.listFragmentParentItemName
        val addBtn = parentBinding.listFragmentParentAdd
        val color = parentBinding.listFragmentParentItemColor
        val arrow = parentBinding.listFragmentParentArrow

        override fun onExpandStatusChanged(
            relatedAdapter: RecyclerView.Adapter<*>?,
            isExpanding: Boolean
        ) {
            if (isExpanding) {
                arrow.setBackgroundResource(R.drawable.arrow_down2)
            } else {
                arrow.setBackgroundResource(R.drawable.arrow_right2)
            }
//            arrow.setBackgroundResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    inner class ChildVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val nameTv = childBinding.listFragmentItemChildTxtName
        val startBtn = childBinding.listFragmentItemChildRyBtn
        val background = childBinding.listFragmentItemChildBackgroundLin
        val timeTv = childBinding.listFragmentItemChildTxtTime
    }


//    inner class TaskCategoryVo(
//        var taskVos: List<TaskVo>,
//        var categoryId: Long,
//        var categoryName: String,
//        var color: Int
//    ) : BaseGroupBean<TaskVo?> {
//
//        override fun getChildCount() = taskVos.size
//
//        override fun getChildAt(childIndex: Int) = taskVos[childIndex]
//
//        override fun isExpandable() = childCount > 0
//    }
}