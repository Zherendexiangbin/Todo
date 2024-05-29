package net.onest.time.adapter.studyroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.time.R;
import net.onest.time.api.vo.RoomVo;

import java.util.List;

public class StudyRoomAdapter extends RecyclerView.Adapter<StudyRoomAdapter.ViewHolder>{
    private List<RoomVo> roomVos;
    private Context context;

    public StudyRoomAdapter(Context context, List<RoomVo> roomVos) {
        this.context = context;
        this.roomVos = roomVos;
    }

    @Override
    public void onBindViewHolder(StudyRoomAdapter.ViewHolder holder, int position){
        RoomVo roomVo = roomVos.get(position);

        Glide.with(context)
                .load(roomVo.getRoomAvatar())
                .into(holder.roomAvatar);
//        holder.roomAvatar.setBackgroundResource(R.mipmap.logo);
        holder.roomName.setText(roomVo.getRoomName());

        holder.enterRoom.setOnClickListener(view -> {
            ((Activity)view.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        if (roomVos.size()==0){
            return 0;
        }else {
            return roomVos.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public StudyRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.studyroom_item, parent, false);
        return new StudyRoomAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView roomAvatar, enterRoom;
        public TextView roomName;

        public ViewHolder(View itemView){
            super(itemView);
            roomAvatar = itemView.findViewById(R.id.room_avatar);
            roomName = itemView.findViewById(R.id.room_name);
            enterRoom = itemView.findViewById(R.id.enter_room);
        }
    }

    public void updateData(List<RoomVo> rooms){
        this.roomVos = rooms;
        notifyDataSetChanged();
    }
}
