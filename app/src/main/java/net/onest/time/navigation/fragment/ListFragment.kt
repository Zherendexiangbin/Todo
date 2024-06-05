package net.onest.time.navigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.onest.time.R
import net.onest.time.adapter.list.ListFragmentGroupAdapter
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.components.AddTaskCollectionsDialog
import net.onest.time.utils.doShakeAnimation
import net.onest.time.utils.showToast

class ListFragment : Fragment() {
    private var backLogRey: RecyclerView? = null
    private var addParentBtn: Button? = null
    private var taskCollectionsList: MutableList<TaskCategoryVo> = ArrayList()
    private var listFragmentGroupAdapter: ListFragmentGroupAdapter? = null
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

        listFragmentGroupAdapter = ListFragmentGroupAdapter(
            requireContext(),
            init()
        )

        backLogRey?.layoutManager = LinearLayoutManager(requireContext())

        backLogRey?.adapter = listFragmentGroupAdapter


        //添加待办集:
        addParentBtn!!.setOnClickListener {
            it.doShakeAnimation()
            AddTaskCollectionsDialog(
                    requireContext(),
                    listFragmentGroupAdapter!!,
                    taskCollectionsList
            )
        }
    }

    private fun init(): MutableList<TaskCategoryVo> {

        try {
            val allCategoryAndTasks = TaskCategoryApi.getAllCategoryAndTasks()
            taskCollectionsList.addAll(allCategoryAndTasks)
        }catch (e:RuntimeException) {
            e.message?.showToast()
        }

        return taskCollectionsList
    }

    private fun findViews(view: View) {
        backLogRey = view.findViewById(R.id.list_fragment_backlog)
        addParentBtn = view.findViewById(R.id.list_fragment_add_btn)
    }
}
