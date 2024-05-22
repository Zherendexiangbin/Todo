package net.onest.time.components.holder;

import android.widget.BaseExpandableListAdapter;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.adapter.todo.TodoItemAdapter;

public class AdapterHolder {
    private BaseExpandableListAdapter baseExpandableListAdapter;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerViewAdapter;

    public AdapterHolder(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.recyclerViewAdapter = adapter;
    }

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
}
