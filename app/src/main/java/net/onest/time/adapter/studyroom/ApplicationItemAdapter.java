package net.onest.time.adapter.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;

import java.util.List;

public class ApplicationItemAdapter extends RecyclerView.Adapter<ApplicationItemAdapter.ViewHolder> {
    private List<String> avatarList;
    private Context context;

    public ApplicationItemAdapter(Context context, List<String> avatarList){
        this.context = context;
        this.avatarList = avatarList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String info = avatarList.get(position);

        //渲染数据
        holder.avatar.setBackgroundResource(R.mipmap.head);
        holder.userName.setText(info);

        holder.agree.setOnClickListener(view -> {
            avatarList.remove(position);
            notifyDataSetChanged();
        });

        holder.refuse.setOnClickListener(view -> {
            avatarList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount(){
        if (avatarList.size()==0){
            return 0;
        }else{
            return avatarList.size();
        }
    }

    @Override
    public  int getItemViewType(int position){
        return position;
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

    public void updateData(List<String> newData) {
        this.avatarList = newData;
        notifyDataSetChanged(); // 通知适配器数据集已更改，刷新列表
    }
}
