package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.time.R;
import net.onest.time.api.RoomApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.UserVo;

import java.util.List;

public class ApplicationItemAdapter extends RecyclerView.Adapter<ApplicationItemAdapter.ViewHolder> {
    private Long roomId;
    private List<UserVo> userVoList;
    private Context context;

    public ApplicationItemAdapter(Long roomId, Context context, List<UserVo> userVoList){
        this.roomId = roomId;
        this.context = context;
        this.userVoList = userVoList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        UserVo userVo = userVoList.get(position);

        //渲染数据
        Glide.with(context)
                .load(userVo.getAvatar())
                .into(holder.avatar);
        holder.userName.setText(userVo.getUserName());

        holder.agree.setOnClickListener(view -> {
            RoomApi.acceptRequest(roomId, userVo.getUserId());
            userVoList.remove(position);
            Toast.makeText(context, "已同意" + userVo.getUserName() + "的请求", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });

        holder.refuse.setOnClickListener(view -> {
            RoomApi.rejectRequest(roomId, userVo.getUserId());
            userVoList.remove(position);
            Toast.makeText(context, "已拒绝" + userVo.getUserName() + "的请求", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount(){
        return userVoList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.studyroom_application_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView avatar;
        public TextView userName;
        public Button refuse, agree;

        public ViewHolder(View itemView){
            super(itemView);
            avatar = itemView.findViewById(R.id.application_avatar);
            userName = itemView.findViewById(R.id.application_name);
            refuse = itemView.findViewById(R.id.btn_application_refuse);
            agree = itemView.findViewById(R.id.btn_application_agree);
        }
    }

    public void updateData(List<UserVo> userVo) {
        this.userVoList = userVo;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }
}
