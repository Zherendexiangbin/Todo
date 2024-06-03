package net.onest.time.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import net.onest.time.api.vo.TaskCategoryVo;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.entity.list.TaskCollections;

import java.util.List;

public class ListFragmentGroupAdapter extends BaseExpandableRecyclerViewAdapter
        <TaskCollections, TaskVo, ListFragmentGroupAdapter.GroupVH, ListFragmentGroupAdapter.ChildVH>{

    private Context context;
    private int groupViewId;
    private int childViewId;
    private List<TaskCollections> taskCollectionsList;


    public ListFragmentGroupAdapter(Context context, int groupViewId, int childViewId, List<TaskCollections> taskCollectionsList) {
        this.context = context;
        this.groupViewId = groupViewId;
        this.childViewId = childViewId;
        this.taskCollectionsList = taskCollectionsList;
    }

    public List<TaskCollections> getTaskCollectionsList() {
        return taskCollectionsList;
    }

    public void setTaskCollectionsList(List<TaskCollections> taskCollectionsList) {
        this.taskCollectionsList = taskCollectionsList;
    }

    @Override
    public int getGroupCount() {
        return taskCollectionsList.size();
    }

    @Override
    public TaskCollections getGroupItem(int groupIndex) {
        return taskCollectionsList.get(groupIndex);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return new GroupVH(LayoutInflater.from(context).inflate(groupViewId,parent,false));
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, TaskCollections groupBean, boolean isExpand) {

    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return new ChildVH(LayoutInflater.from(context).inflate(childViewId,parent,false));
    }

    @Override
    public void onBindChildViewHolder(ChildVH childVH, TaskCollections groupBean, TaskVo taskVo) {

    }

    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView foldIv;
        TextView nameTv;

        GroupVH(View itemView) {
            super(itemView);
//            foldIv = (ImageView) itemView.findViewById(R.id.group_item_indicator);
//            nameTv = (TextView) itemView.findViewById(R.id.group_item_name);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
//            foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
        TextView nameTv;

        ChildVH(View itemView) {
            super(itemView);
//            nameTv = (TextView) itemView.findViewById(R.id.child_item_name);
        }
    }
}
