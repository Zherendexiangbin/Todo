package net.onest.time.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.adapter.list.ExpandableListAdapter
import net.onest.time.api.TaskApi
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskCollectionsDialog
import net.onest.time.entity.list.TaskCollections
import net.onest.time.utils.showToast

class ListFragment : Fragment() {
    private var backLog: ExpandableListView? = null
    private var addParentBtn: Button? = null
    private var menuBtn: Button? = null
    private var taskCollectionsList: MutableList<TaskCollections> = ArrayList()
    private var parentMap: MutableMap<TaskCategoryVo, List<TaskVo?>> = HashMap()
    private var expandableListAdapter: ExpandableListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        expandableListAdapter = ExpandableListAdapter(
            R.layout.list_fragment_expandable_parent_list,
            R.layout.list_fragment_expandable_child_list,
            requireContext(),
            init()
        )
        backLog!!.setAdapter(expandableListAdapter)

        val display = requireActivity().windowManager.defaultDisplay
        //        Point size = new Point();
//        display.getSize(size);
//        int screenWidth = size.x;
        val width = display.width
        //        backLog.setIndicatorBoundsRelative(100,100);
        backLog!!.setIndicatorBounds(width - 320, width - 290)


        //默认展开第一项
//        backLog.expandGroup(0);
        backLog!!.setOnGroupClickListener { parent, v, groupPosition, id ->
            val groupExpanded = backLog!!.isGroupExpanded(groupPosition)
            if (groupExpanded) {
                v.findViewById<View>(R.id.list_fragment_parent_arrow)
                    .setBackgroundResource(R.drawable.arrow_right2)
            } else {
                v.findViewById<View>(R.id.list_fragment_parent_arrow)
                    .setBackgroundResource(R.drawable.arrow_down2)
            }
            false
        }

//        长按删除/编辑事件:
        backLog!!.setOnItemLongClickListener { parent, view, position, id ->

            val packedPositionType = ExpandableListView.getPackedPositionType(id)
            if (packedPositionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
//                val groupPosition = ExpandableListView.getPackedPositionGroup(id)
                // 处理长按parentItem的逻辑
//                Toast.makeText(context, "长按了parentItem：$groupPosition", Toast.LENGTH_SHORT).show()

                // 参数2：设置BottomSheetDialog的主题样式；将背景设置为transparent，这样我们写的shape_bottom_sheet_dialog.xml才会起作用
                val bottomSheetDialog = BottomSheetDialog(requireContext());
                //不传第二个参数
                //BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

                // 底部弹出的布局
                val bottomView = LayoutInflater.from(requireContext())
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
                }

                delete.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    XPopup.Builder(context)
                            .asConfirm("",
                                    "你确定要删除${taskCollectionsList[position].taskCollectionsName}待办集吗？") {
                                try {
                                    TaskCategoryApi.deleteTaskCategory(taskCollectionsList[position].taskCollectionsId)
                                    "删除成功！".showToast()
                                } catch (e: RuntimeException) {
                                    e.message?.showToast()
                                }

                                taskCollectionsList.removeAt(position)
                                expandableListAdapter?.notifyDataSetChanged()
                            }
                            .show()
                }

                true // 返回true表示消费了长按事件
            } else {
                false
            }
        }



        backLog!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Toast.makeText(
                    context,
                    "点击了" + taskCollectionsList[groupPosition].tasks[childPosition].taskName,
                    Toast.LENGTH_SHORT
            ).show()
            false
        }

        //添加待办集:
        addParentBtn!!.setOnClickListener {
            AddTaskCollectionsDialog(
                    requireContext(),
                    expandableListAdapter!!,
                    taskCollectionsList
            )
        }
    }

    private fun init(): List<TaskCollections> {
        try {
            val all = TaskCategoryApi.getAll()
            all.forEach { taskCategoryVo ->
                val allTasks = TaskCategoryApi.getAllTasks(taskCategoryVo.categoryId)
                parentMap[taskCategoryVo] = allTasks
            }

        } catch (e: RuntimeException) {
            e.message?.showToast()
        }

        taskCollectionsList = ArrayList()
        parentMap.forEach { (key: TaskCategoryVo, value: List<TaskVo?>) ->
            val taskCollections = TaskCollections(
                    key.categoryId,
                    key.categoryName,
                    key.color!!,
                    value
            )
            taskCollectionsList.add(taskCollections)
        }

//        taskCollectionsList.sortWith(TaskCollections.comparator())
        return taskCollectionsList
    }

    private fun findViews(view: View) {
        backLog = view.findViewById(R.id.list_fragment_backlog)
        addParentBtn = view.findViewById(R.id.list_fragment_add_btn)
        menuBtn = view.findViewById(R.id.list_fragment_menu_btn)
    }
}
