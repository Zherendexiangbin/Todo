package net.onest.time.adapter.studyroom;

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

import java.util.List;

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {
    private final Context context;
    private final List<Message> mMsgList;

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

    public ChatMsgAdapter(Context context, List<Message> msgList) {
        this.context = context;
        this.mMsgList = msgList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
//        ChatMessage msg = mMsgList.get(position);
//        if (Objects.equals(msg.getFromId(), HuaShangApplication.getUser().getId())) {
//            //如果是发送消息，则显示右边的消息布局
//            holder.rightLayout.setVisibility(View.VISIBLE);
//            holder.leftLayout.setVisibility(View.GONE);
//            holder.rightMsg.setText(msg.getContent());
//            //判断是否显示时间控件
//            holder.rightTime.setVisibility(View.GONE);
//        } else {
//            //如果是收到消息，则显示左边的消息布局
//            holder.leftLayout.setVisibility(View.VISIBLE);
//            holder.rightLayout.setVisibility(View.GONE);
//            holder.leftMsg.setText(msg.getContent());
//            //判断是否显示时间控件
//            holder.leftTime.setVisibility(View.GONE);
//        }
    }

    public int getItemCount() {
        return mMsgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
