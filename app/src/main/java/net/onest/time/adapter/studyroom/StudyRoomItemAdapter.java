package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomItemAdapter extends RecyclerView.Adapter<StudyRoomItemAdapter.ViewHolder> {
    private List<String> avatarList;
    private Context context;

    public StudyRoomItemAdapter(Context context, List<String> avatarList) {
        this.context = context;
        this.avatarList = avatarList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String data = avatarList.get(position);

        holder.avatar.setBackgroundResource(R.mipmap.head);
        holder.avatar.setOnClickListener(view -> {
            if(avatarList.size()>1){
                Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                avatarList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (avatarList.size()==0){
            return 0;
        }else {
            return avatarList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.studyroom_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView avatar;

        public ViewHolder(View itemView){
            super(itemView);
            avatar = itemView.findViewById(R.id.user_avatar);
        }
    }

    public void updateData(List<String> newData) {
        this.avatarList = newData;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }

}
