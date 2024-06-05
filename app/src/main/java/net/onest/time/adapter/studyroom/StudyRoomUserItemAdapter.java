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
import net.onest.time.api.RoomApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudyRoomUserItemAdapter extends RecyclerView.Adapter<StudyRoomUserItemAdapter.ViewHolder> {
    private List<UserVo> userVoList;
    private Context context;

    public StudyRoomUserItemAdapter(Context context, List<UserVo> userVoList) {
        this.context = context;
        this.userVoList = userVoList.stream()
                .sorted(Comparator.comparing(UserVo :: getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        UserVo userVo = userVoList.get(position);

        holder.userRank.setText(""+(position+1));
        Glide.with(context)
                        .load(userVo.getAvatar())
                                .into(holder.userAvatar);
        //查询用户所在自习室
        try {
            RoomVo roomVo = RoomApi.getRoomInfo();
            if (roomVo != null) {
                //查询自习室管理员信息
                UserVo managerInfo = UserApi.getUserInfo(roomVo.getUserId());
                if (userVo.getUserName().equals(managerInfo.getUserName())){
                    holder.userName.setText(userVo.getUserName() + "（管理员）");
                    holder.userTime.setText("" + userVo.getTomatoDuration());
                }else{
                    holder.userName.setText(userVo.getUserName());
                    holder.userTime.setText("" + userVo.getTomatoDuration());
                }
            }else {
                holder.userName.setText(userVo.getUserName());
            }
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        if (userVoList.size()==0){
            return 0;
        }else {
            return userVoList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.studyroom_user_item, parent, false);
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
