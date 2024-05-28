package net.onest.time.components.holder

import android.annotation.SuppressLint
import android.widget.BaseExpandableListAdapter
import androidx.recyclerview.widget.RecyclerView

class AdapterHolder {
    private var baseExpandableListAdapter: BaseExpandableListAdapter? = null
    private var recyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    constructor(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        this.recyclerViewAdapter = adapter
    }

    constructor(adapter: BaseExpandableListAdapter?) {
        this.baseExpandableListAdapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() {
        baseExpandableListAdapter?.notifyDataSetChanged()

        recyclerViewAdapter?.notifyDataSetChanged()
    }

    fun notifyItemChanged(position: Int) {
        baseExpandableListAdapter?.notifyDataSetChanged()

        recyclerViewAdapter?.notifyItemChanged(position)
    }

    fun notifyItemRemoved(position: Int) {
        baseExpandableListAdapter?.notifyDataSetChanged()

        recyclerViewAdapter?.notifyItemRemoved(position)
    }

    fun notifyItemInserted(position: Int) {
        baseExpandableListAdapter?.notifyDataSetChanged()

        recyclerViewAdapter?.notifyItemInserted(position)
    }
}
