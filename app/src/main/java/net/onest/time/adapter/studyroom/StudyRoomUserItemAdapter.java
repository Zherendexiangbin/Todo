package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        this.userVoList = userVoList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        UserVo userVo = userVoList.get(position);

        Glide.with(context)
                        .load(userVo.getAvatar())
                                .into(holder.avatar);

//        holder.avatar.setOnClickListener(view -> {
//                notifyDataSetChanged();
//        });

    }

    @Override
    public int getItemCount() {
        if (userVoList.size()==0){
            return 1;
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
        public ImageView avatar;

        public ViewHolder(View itemView){
            super(itemView);
            avatar = itemView.findViewById(R.id.user_avatar);
        }
    }

    public void updateData(List<UserVo> userVos) {
        this.userVoList = userVos;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }

}
