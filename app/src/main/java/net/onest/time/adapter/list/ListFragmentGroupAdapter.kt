package net.onest.time.adapter.list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter
import net.onest.time.adapter.list.ListFragmentGroupAdapter.ChildVH
import net.onest.time.adapter.list.ListFragmentGroupAdapter.GroupVH
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo
import net.onest.time.databinding.ListFragmentExpandableChildListBinding
import net.onest.time.databinding.ListFragmentExpandableParentListBinding

class ListFragmentGroupAdapter(
    var context: Context,
    taskCategoryList: MutableList<TaskCategoryVo>,
) : BaseExpandableRecyclerViewAdapter<
        ListFragmentGroupAdapter.TaskCategoryGroupBean,
        TaskVo?,
        GroupVH?,
        ChildVH?>() {
    private var taskCategoryGroupBeanList: MutableList<TaskCategoryGroupBean>
    lateinit var parentBinding: ListFragmentExpandableParentListBinding
    lateinit var childBinding: ListFragmentExpandableChildListBinding

    init {
        taskCategoryGroupBeanList =
            taskCategoryList.map {
                TaskCategoryGroupBean(it.taskVos!!, it.categoryId!!, it.categoryName!!, it.color!!)
            }.toMutableList()
    }

    override fun getGroupCount() = taskCategoryGroupBeanList.size

    override fun getGroupItem(groupIndex: Int) = taskCategoryGroupBeanList[groupIndex]

    override fun onCreateGroupViewHolder(parent: ViewGroup, groupViewType: Int): GroupVH {
        parentBinding = ListFragmentExpandableParentListBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return GroupVH(parentBinding.root)
    }

    override fun onBindGroupViewHolder(
        holder: GroupVH?,
        groupBean: TaskCategoryGroupBean,
        isExpand: Boolean
    ) {
        holder?.run {
            nameTv.text = groupBean.categoryName
            addBtn.setOnClickListener {
                TODO("添加item")
            }
            dataBtn.setOnClickListener {
                TODO("数据展示")
            }
            color.setBackgroundColor(groupBean.color)
        }
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, childViewType: Int): ChildVH {
        childBinding = ListFragmentExpandableChildListBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ChildVH(childBinding.root)
    }

    override fun onBindChildViewHolder(
        childVH: ChildVH?,
        groupBean: TaskCategoryGroupBean,
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
            startBtn.setOnClickListener {
                TODO("点击跳转时钟")
            }
        }

    }

    inner class GroupVH(itemView: View?) : BaseGroupViewHolder(itemView) {
        val nameTv = parentBinding.listFragmentParentItemName
        val addBtn = parentBinding.listFragmentParentAdd
        val dataBtn = parentBinding.listFragmentParentData
        val color = parentBinding.listFragmentParentItemColor

        override fun onExpandStatusChanged(
            relatedAdapter: RecyclerView.Adapter<*>?,
            isExpanding: Boolean
        ) {
//            foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    inner class ChildVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val nameTv = childBinding.listFragmentItemChildTxtName
        val startBtn = childBinding.listFragmentItemChildRyBtn
        val background = childBinding.listFragmentItemChildBackgroundLin
        val timeTv = childBinding.listFragmentItemChildTxtTime



    }


    inner class TaskCategoryGroupBean(
        var taskVos: List<TaskVo>,
        var categoryId: Long,
        var categoryName: String,
        var color: Int
    ) : BaseGroupBean<TaskVo?> {


        override fun getChildCount() = taskVos.size

        override fun getChildAt(childIndex: Int) = taskVos[childIndex]

        override fun isExpandable() = childCount > 0

    }
}