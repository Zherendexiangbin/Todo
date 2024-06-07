package net.onest.time.adapter.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.TimerActivity
import net.onest.time.adapter.list.ListFragmentGroupAdapter.ChildVH
import net.onest.time.adapter.list.ListFragmentGroupAdapter.GroupVH
import net.onest.time.api.StatisticApi
import net.onest.time.api.TaskCategoryApi
import net.onest.time.api.vo.TaskCategoryVo
import net.onest.time.api.vo.TaskVo
import net.onest.time.components.AddTaskMoreDialog
import net.onest.time.components.TaskInfoDialog
import net.onest.time.components.UpdateCategoryDialog
import net.onest.time.components.holder.AdapterHolder
import net.onest.time.databinding.ListFragmentExpandableChildListBinding
import net.onest.time.databinding.ListFragmentExpandableParentListBinding
import net.onest.time.utils.ColorUtil
import net.onest.time.utils.showToast
import java.time.LocalDate
import java.time.ZoneOffset

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

            //数据获取:
            statistics!!.setOnClickListener {

                TODO("数据获取")
                val statisticByTask = StatisticApi.statisticByTask(
                    groupBean.categoryId,
                    LocalDate.now().atStartOfDay(ZoneOffset.of("+8")).toEpochSecond() * 1000
                )

                val builder = AlertDialog.Builder(context, R.style.CustomDialogStyle)
                val inflater = LayoutInflater.from(context)
                val dialogViewAban = inflater.inflate(R.layout.task_statistics_pop, null)
                val dialogAban: Dialog = builder.create()
                dialogAban.show()
                dialogAban.window!!.setContentView(dialogViewAban)
                dialogAban.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val tomatoTimes = dialogViewAban.findViewById<TextView>(R.id.statistics_tomato_times)
                val tomatoDuration = dialogViewAban.findViewById<TextView>(R.id.statistics_tomato_duration)
                val averageTomatoTimes = dialogViewAban.findViewById<TextView>(R.id.statistics_today_tomato_times)
                val averageTomatoDuration = dialogViewAban.findViewById<TextView>(R.id.statistics_today_tomato_duration)
                val radioGroup = dialogViewAban.findViewById<RadioGroup>(R.id.statistics_time_data)
                val radioButtonDay = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_day)
                val radioButtonWeek = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_week)
                val radioButtonMonth = dialogViewAban.findViewById<RadioButton>(R.id.statistics_time_data_month)
                var pieChart = dialogViewAban.findViewById<PieChart>(R.id.statistics_pie_chart)
                var close = dialogViewAban.findViewById<Button>(R.id.close_pop_window)

                tomatoTimes.text = "${statisticByTask.tomatoTimes}"
                tomatoDuration.text = "${statisticByTask.tomatoDuration}"
                averageTomatoTimes.text = "${statisticByTask.avgTomatoTimes}"
                averageTomatoDuration.text = "${statisticByTask.avgTomatoDuration}"


                val pieEntries = statisticByTask.ratioByDurationOfDay.map {
                    PieEntry(it.ratio.toFloat(), it.taskName)
                }

                val colors = statisticByTask.ratioByDurationOfDay.map {
                    ColorUtil.getColorByRgb(null)
                }

                setPieChartData(pieEntries, colors,pieChart)
                pieChart!!.notifyDataSetChanged()

                radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.statistics_time_data_day -> {
                            val pieEntries = statisticByTask.ratioByDurationOfDay.map {
                                PieEntry(it.ratio.toFloat(), it.taskName)
                            }

                            val colors = statisticByTask.ratioByDurationOfDay.map {
                                ColorUtil.getColorByRgb(null)
                            }

                            setPieChartData(pieEntries, colors,pieChart)
                            pieChart!!.notifyDataSetChanged()
                        }

                        R.id.statistics_time_data_week -> {
                            val pieEntries = statisticByTask.ratioByDurationOfWeek.map {
                                PieEntry(it.ratio.toFloat(), it.taskName)
                            }

                            val colors = statisticByTask.ratioByDurationOfWeek.map {
                                ColorUtil.getColorByRgb(null)
                            }

                            setPieChartData(pieEntries, colors,pieChart)
                            pieChart!!.notifyDataSetChanged()
                        }

                        R.id.statistics_time_data_month -> {
                            val pieEntries = statisticByTask.ratioByDurationOfMonth.map {
                                PieEntry(it.ratio.toFloat(), it.taskName)
                            }

                            val colors = statisticByTask.ratioByDurationOfMonth.map {
                                ColorUtil.getColorByRgb(null)
                            }

                            setPieChartData(pieEntries, colors,pieChart)
                            pieChart!!.notifyDataSetChanged()
                        }
                    }
                }

                close.setOnClickListener {
                    dialogAban.dismiss()
                }
            }
        }
    }

    private fun setPieChartData(yVals: List<PieEntry>, colors: List<Int>,pieChart: PieChart) {
        if(yVals.isEmpty()){
            pieChart.visibility = View.INVISIBLE
        }else{
            pieChart.visibility = View.VISIBLE

            val pieDataSet = PieDataSet(yVals, "")
            pieDataSet.colors = colors
            pieChart.setEntryLabelColor(Color.parseColor("#ff8c00")) //描述文字的颜色
            pieDataSet.valueTextSize = 15f //数字大小
            pieDataSet.valueTextColor = Color.BLACK //数字颜色
            pieDataSet.valueFormatter = object : ValueFormatter() {
                @SuppressLint("DefaultLocale")
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.2f%%", value * 100)
                }
            }

            //设置描述的位置
            pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.valueLinePart1Length = 0.5f //设置描述连接线长度
            //设置数据的位置
            pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.valueLinePart2Length = 0.6f //设置数据连接线长度

            //设置两根连接线的颜色
            pieDataSet.valueLineColor = Color.BLUE

            val pieData = PieData(pieDataSet)
            pieChart.data = pieData
            pieChart.setExtraOffsets(0f, 20f, 0f, 20f)
            //动画（如果使用了动画可以则省去更新数据的那一步）
//        pieChart.animateY(1000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        pieChart.animateX(1000); //X轴动画
            pieChart.animateXY(1000, 1000) //XY两轴混合动画

            val legend = pieChart!!.legend
            legend.xEntrySpace = 20f
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.isWordWrapEnabled = true
//            legend.formSize = 15f
//            legend.textSize = 15f
            legend.textColor=Color.BLACK

            pieDataSet.notifyDataSetChanged()
            pieChart.notifyDataSetChanged()

        }
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, childViewType: Int): ChildVH {
        childBinding = ListFragmentExpandableChildListBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ChildVH(childBinding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindChildViewHolder(
        childVH: ChildVH?,
        groupBean: TaskCategoryVo,
        taskVo: TaskVo?
    ) {
        childVH?.run {
            if (taskVo?.taskStatus == 2) {
                //完成状态==设置删除线:
                nameTv.text = taskVo?.taskName
                nameTv.setDeleteLineColor(Color.parseColor("#ffffff")) //设置删除线的颜色
                nameTv.setShowDeleteLine(true) //删除线是否显示
                nameTv.setDeleteLineWidth(context, 3) //删除线显示宽度

            } else {
                nameTv.text = taskVo?.taskName
                nameTv.setShowDeleteLine(false)
            }

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
        val statistics = parentBinding.listFragmentParentStatistics

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