package net.onest.time.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import net.onest.time.R
import net.onest.time.adapter.list.ExpandableListAdapter
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.dto.TaskCategoryDto
import net.onest.time.entity.list.TaskCollections
import net.onest.time.utils.makeToast
import net.onest.time.utils.showToast

/**
 * 添加待办集合
 */
class AddTaskCollectionsDialog(
    context: Context,
    private val expandableListAdapter: ExpandableListAdapter,
    private val taskCollectionsList: MutableList<TaskCollections>
) : AlertDialog(context) {
    private var addYes: Button? = null
    private var addNo: Button? = null
    private var groupOne: RadioGroup? = null
    private var groupTwo: RadioGroup? = null
    private var edit: EditText? = null

    init {
        val dialogView = LayoutInflater.from(getContext())
            .inflate(R.layout.list_fragment_add_parent_item_pop_window, null)

        show()
        window!!.setContentView(dialogView)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        findViews(dialogView)
        setListeners()
    }

    private fun findViews(dialogView: View) {
        addYes = dialogView.findViewById(R.id.add_list_item_yes)
        addNo = dialogView.findViewById(R.id.add_list_item_no)
        groupOne = dialogView.findViewById(R.id.list_fragment_pop_window_group_one)
        groupTwo = dialogView.findViewById(R.id.list_fragment_pop_window_group_two)
        edit = dialogView.findViewById(R.id.list_fragment_group_edit)
    }

    private fun setListeners() {
        groupOne!!.setOnCheckedChangeListener { group, checkedId ->
            if (groupOne!!.checkedRadioButtonId != -1
                && groupTwo!!.checkedRadioButtonId != -1
            ) {
                groupTwo!!.clearCheck()
            }
            when (checkedId) {
                R.id.list_fragment_group_one_card_view_blue -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0000ff"))
                    edit!!.setTextColor(Color.parseColor("#0000ff"))
                    edit!!.setHintTextColor(Color.parseColor("#0000ff"))
                }

                R.id.list_fragment_group_one_card_view_brown -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#a52a2a"))
                    edit!!.setTextColor(Color.parseColor("#a52a2a"))
                    edit!!.setHintTextColor(Color.parseColor("#a52a2a"))
                }

                R.id.list_fragment_group_one_card_view_gray -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#808080"))
                    edit!!.setTextColor(Color.parseColor("#808080"))
                    edit!!.setHintTextColor(Color.parseColor("#808080"))
                }

                R.id.list_fragment_group_one_card_view_pink -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ffc0cb"))
                    edit!!.setTextColor(Color.parseColor("#ffc0cb"))
                    edit!!.setHintTextColor(Color.parseColor("#ffc0cb"))
                }
            }
        }

        groupTwo!!.setOnCheckedChangeListener { group, checkedId -> // 没选 或 另一个选了
            if (groupTwo!!.checkedRadioButtonId != -1
                && groupOne!!.checkedRadioButtonId != -1
            ) {
                groupOne!!.clearCheck()
            }
            when (checkedId) {
                R.id.list_fragment_group_two_card_view_blueviolet -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#8a2be2"))
                    edit!!.setTextColor(Color.parseColor("#8a2be2"))
                    edit!!.setHintTextColor(Color.parseColor("#8a2be2"))
                }

                R.id.list_fragment_group_two_card_view_lightgreen -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#90ee90"))
                    edit!!.setTextColor(Color.parseColor("#90ee90"))
                    edit!!.setHintTextColor(Color.parseColor("#90ee90"))
                }

                R.id.list_fragment_group_two_card_view_purple -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#800080"))
                    edit!!.setTextColor(Color.parseColor("#800080"))
                    edit!!.setHintTextColor(Color.parseColor("#800080"))
                }

                R.id.list_fragment_group_two_card_view_red -> {
                    edit!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E83141"))
                    edit!!.setTextColor(Color.parseColor("#E83141"))
                    edit!!.setHintTextColor(Color.parseColor("#E83141"))
                }
            }
        }

        addYes!!.setOnClickListener { v: View? ->
            val taskCollectionsName = edit!!.text.toString().trim()

            if (taskCollectionsName.isBlank()) {
                val toast = "请输入待办集名称".makeToast()
                toast.setGravity(Gravity.TOP, 0, 0)
                toast.show()
                return@setOnClickListener
            }

            val taskCollectionsColor = edit!!.currentTextColor

            val taskCollections = TaskCollections(
                    null,
                taskCollectionsName,
                taskCollectionsColor,
                ArrayList()
            )

            val taskCategoryDto = TaskCategoryDto()
            taskCategoryDto.categoryName=taskCollections.taskCollectionsName
            taskCategoryDto.color=taskCollections.taskCollectionsColor
            try {
                val taskCategory = TaskCategoryApi.addTaskCategory(taskCategoryDto)
                taskCollections.taskCollectionsId = taskCategory.categoryId
            } catch (e: RuntimeException) {
                e.message?.showToast()
            }

            taskCollectionsList.add(taskCollections)
            expandableListAdapter.notifyDataSetChanged()
            dismiss()
        }

        addNo!!.setOnClickListener { dismiss() }
    }
}
