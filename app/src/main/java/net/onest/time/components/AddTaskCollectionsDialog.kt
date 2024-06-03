package net.onest.time.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.onest.time.R
import net.onest.time.adapter.list.ExpandableListAdapter
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.dto.TaskCategoryDto
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.entity.list.TaskCollections
import net.onest.time.utils.makeToast
import net.onest.time.utils.showToast

/**
 * 添加待办集合
 */
class AddTaskCollectionsDialog(
    context: Context,
    private val expandableListAdapter: ExpandableListAdapter,
    private val taskCollectionsList: MutableList<TaskCategoryVo>
) : AlertDialog(context) {
    private var addYes: Button? = null
    private var addNo: Button? = null
    private var groupOne: RadioGroup? = null
    private var groupTwo: RadioGroup? = null
    private var edit: TextInputEditText? = null
    private var editLayout: TextInputLayout? = null

    init {
        val dialogView = LayoutInflater.from(getContext())
            .inflate(R.layout.list_fragment_add_parent_item_pop_window, null)

        show()
        window!!.setContentView(dialogView)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        setEditColor("#0000ff")

        findViews(dialogView)
        setListeners()
    }

    private fun findViews(dialogView: View) {
        addYes = dialogView.findViewById(R.id.add_list_item_yes)
        addNo = dialogView.findViewById(R.id.add_list_item_no)
        groupOne = dialogView.findViewById(R.id.list_fragment_pop_window_group_one)
        groupTwo = dialogView.findViewById(R.id.list_fragment_pop_window_group_two)
        edit = dialogView.findViewById(R.id.list_fragment_group_edit)
        editLayout = dialogView.findViewById(R.id.list_fragment_group_edit_layout)
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
                    setEditColor("#0000ff")
                }

                R.id.list_fragment_group_one_card_view_brown -> {
                    setEditColor("#a52a2a")
                }

                R.id.list_fragment_group_one_card_view_gray -> {
                    setEditColor("#808080")
                }

                R.id.list_fragment_group_one_card_view_pink -> {
                    setEditColor("#ffc0cb")
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
                    setEditColor("#8a2be2")
                }

                R.id.list_fragment_group_two_card_view_lightgreen -> {
                    setEditColor("#90ee90")
                }

                R.id.list_fragment_group_two_card_view_purple -> {
                    setEditColor("#800080")
                }

                R.id.list_fragment_group_two_card_view_red -> {
                    setEditColor("#E83141")
                }
            }
        }

        edit?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            } else {
                hideKeyboard(v)
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

            val taskCollectionsColor = editLayout!!.boxStrokeColor

//            val taskCollections = TaskCollections(
//                    null,
//                taskCollectionsName,
//                taskCollectionsColor,
//                ArrayList()
//            )

            val taskCollections2 = TaskCategoryVo()
            taskCollections2.categoryName = taskCollectionsName
            taskCollections2.color = taskCollectionsColor
            taskCollections2.taskVos = ArrayList()

            val taskCategoryDto = TaskCategoryDto()
            taskCategoryDto.categoryName=taskCollections2.categoryName
            taskCategoryDto.color=taskCollections2.color

            try {
                val taskCategory = TaskCategoryApi.addTaskCategory(taskCategoryDto)
                taskCollections2.categoryId = taskCategory.categoryId
            } catch (e: RuntimeException) {
                e.message?.showToast()
            }

            // TODO: 换组件，受不了了！！！！！！！！！！！！！！！！！
            taskCollectionsList.add(taskCollections2)
            expandableListAdapter.notifyDataSetChanged()
            dismiss()
        }

        addNo!!.setOnClickListener { dismiss() }
    }

    private fun setEditColor(color: String) {
        val colorStateList = ColorStateList.valueOf(Color.parseColor(color))
        val parseColor = Color.parseColor(color)

        editLayout?.run {
            defaultHintTextColor = colorStateList
            hintTextColor = colorStateList
            boxStrokeColor = parseColor
        }

    }

    private fun showKeyboard(view: View) {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
