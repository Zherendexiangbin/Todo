package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.time.R;
import net.onest.time.api.vo.UserVo;

import java.util.List;

public class StudyRoomUserItemAdapter extends RecyclerView.Adapter<StudyRoomUserItemAdapter.ViewHolder> {
    private List<UserVo> userVoList;
    private Context context;

    public StudyRoomUserItemAdapter(Context context, List<UserVo> userVoList) {
        this.context = context;
        this.userVoList = userVoList;   // 后端已按专注时长排序
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        UserVo userVo = userVoList.get(position);

        Glide.with(context)
                        .load(userVo.getAvatar())
                                .into(holder.userAvatar);

        holder.userName.setText(userVo.getUserName());
        holder.userRank.setText("" + (position + 1));
        holder.userTime.setText(userVo.getTomatoDuration() + "分钟");
    }

    @Override
    public int getItemCount() {
        return userVoList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view  = LayoutInflater
                .from(context)
                .inflate(R.layout.studyroom_user_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView userAvatar;
        public TextView userRank, userName, userTime;

        public ViewHolder(View itemView){
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userRank = itemView.findViewById(R.id.user_rank);
            userName = itemView.findViewById(R.id.user_name);
            userTime = itemView.findViewById(R.id.user_time);
        }
    }

    public void updateData(List<UserVo> userVos) {
        this.userVoList = userVos;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }

}
