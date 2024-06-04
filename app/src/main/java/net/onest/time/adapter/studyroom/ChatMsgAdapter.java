package net.onest.time.adapter.studyroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;
import net.onest.time.api.vo.Message;
import net.onest.time.api.vo.UserVo;

import java.util.List;
import java.util.Objects;

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {
    private final Context context;
    private List<Message> mMsgList;
    private final Long userId;

    static class ViewHolder extends RecyclerView.ViewHolder {
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

    public ChatMsgAdapter(Context context, List<Message> msgList, Long userId) {
        this.context = context;
        this.mMsgList = msgList;
        this.userId = userId;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Message msg = mMsgList.get(position);
        String sendTime = msg.getSendTime().getMonth()+1 + "." + (msg.getSendTime().getDay()+2) + " " + msg.getSendTime().getHours() + ":" + msg.getSendTime().getMinutes();
        if (Objects.equals(msg.getFromUserId(), userId)){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightName.setText( sendTime + " " + userId.toString());
            holder.rightMsg.setText(msg.getContent());
        }else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftName.setText(msg.getFromUserId().toString() + " " + sendTime);
            holder.leftMsg.setText(msg.getContent());
        }
    }

    public int getItemCount() {
        return mMsgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateData(List<Message> newMessage) {
        this.mMsgList = newMessage;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }
}
