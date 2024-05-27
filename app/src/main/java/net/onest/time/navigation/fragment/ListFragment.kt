package net.onest.time.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import net.onest.time.R
import net.onest.time.adapter.list.ExpandableListAdapter
import net.onest.time.api.TaskApi
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskCollectionsDialog
import net.onest.time.entity.list.TaskCollections
import net.onest.time.utils.ColorUtil
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
                var allTasks = TaskCategoryApi.getAllTasks(taskCategoryVo.categoryId)
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

        return taskCollectionsList
    }

    private fun findViews(view: View) {
        backLog = view.findViewById(R.id.list_fragment_backlog)
        addParentBtn = view.findViewById(R.id.list_fragment_add_btn)
        menuBtn = view.findViewById(R.id.list_fragment_menu_btn)
    }
}
