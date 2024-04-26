package net.onest.time.adapter.todo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.MainActivity;
import net.onest.time.R;
import net.onest.time.TimerActivity;
import net.onest.time.entity.Item;
import net.onest.time.navigation.activity.NavigationActivity;
import net.onest.time.navigation.fragment.TodoFragment;

import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyViewHolder>{
    private Context context;
    private List<Item> itemList = new ArrayList<>();

    public TodoItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.re_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item news = itemList.get(position);
        holder.name.setText(news.getItemName());
        holder.time.setText(news.getTime());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String[] parts = holder.time.getText().toString().split(" ");
                String num = parts[0];
//                int num = Integer.parseInt(parts[0]);
                intent.putExtra("time", num);
                intent.setClass(context, TimerActivity.class);
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,holder.btn,"fab").toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
            time = itemView.findViewById(R.id.txt_time);
            btn = itemView.findViewById(R.id.ry_btn);
        }
    }
}
