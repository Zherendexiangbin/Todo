package net.onest.time.components.holder;

import android.widget.BaseExpandableListAdapter;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.adapter.todo.TodoItemAdapter;
import net.onest.time.api.vo.TaskVo;

import java.util.List;

public class AdapterHolder {
    private BaseExpandableListAdapter baseExpandableListAdapter;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerViewAdapter;
//    private TodoItemAdapter todoItemAdapter;

    public AdapterHolder(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.recyclerViewAdapter = adapter;
    }

//    public AdapterHolder(TodoItemAdapter todoItemAdapter) {
//        this.todoItemAdapter = todoItemAdapter;
//    }

    public AdapterHolder(BaseExpandableListAdapter adapter) {
        this.baseExpandableListAdapter = adapter;
    }

    public void notifyDataSetChanged() {
        if (baseExpandableListAdapter != null) {
            baseExpandableListAdapter.notifyDataSetChanged();
            return;
        }

        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyDataSetChanged();
            return;
        }

    }

//    public void notifyItemChanged(int position, TaskVo taskVo) {
//        List<TaskVo> itemListByDay = todoItemAdapter.getItemListByDay();
//        itemListByDay.set(position,taskVo);
//        todoItemAdapter.setItemListByDay(itemListByDay);
//        todoItemAdapter.notifyItemChanged(position);
//    }
    public void notifyItemChanged(int position) {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyItemChanged(position);
        }
    }
}
