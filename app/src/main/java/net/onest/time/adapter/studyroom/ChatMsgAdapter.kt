package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.time.R;
import net.onest.time.api.UserApi;
import net.onest.time.api.vo.MessageVo;

import java.util.List;
import java.util.Objects;

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {
    private final Context context;
    private List<MessageVo> mMsgList;
    private final Long userId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private RelativeLayout rightLayout;
        private TextView leftName, leftMsg;
        private TextView rightName, rightMsg;
        private ImageView leftUserHeader, rightUserHeader;

        public ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftName = view.findViewById(R.id.left_user_name);
            leftMsg = view.findViewById(R.id.left_msg);
            rightName = view.findViewById(R.id.right_user_name);
            rightMsg = view.findViewById(R.id.right_msg);
            leftUserHeader = view.findViewById(R.id.img_left_header);
            rightUserHeader = view.findViewById(R.id.img_right_header);
        }
    }

    public ChatMsgAdapter(Context context, List<MessageVo> msgList, Long userId) {
        this.context = context;
        this.mMsgList = msgList;
        this.userId = userId;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context).
                inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageVo msg = mMsgList.get(position);
        String sendTime = msg.getSendTime().getMonth()+1 + "." + (msg.getSendTime().getDay()+2) + " " + msg.getSendTime().getHours() + ":" + msg.getSendTime().getMinutes();
        if (Objects.equals(msg.getFromUserId(), userId)){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightName.setText(sendTime + " " + UserApi.getUserInfo(msg.getFromUserId()).getUserName());
            holder.rightMsg.setText(msg.getContent());
            Glide.with(context)
                    .load(UserApi.getUserInfo(msg.getFromUserId()).getAvatar())
                    .into(holder.rightUserHeader);
        }else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftName.setText(UserApi.getUserInfo(msg.getFromUserId()).getUserName() + " " + sendTime);
            holder.leftMsg.setText(msg.getContent());
            Glide.with(context)
                    .load(UserApi.getUserInfo(msg.getFromUserId()).getAvatar())
                    .into(holder.leftUserHeader);
        }
    }

    public int getItemCount() {
        return mMsgList.size();
    }
}
