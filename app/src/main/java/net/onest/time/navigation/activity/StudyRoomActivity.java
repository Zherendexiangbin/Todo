package net.onest.time.navigation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.StudyRoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomActivity extends AppCompatActivity {
    private StudyRoomItemAdapter itemAdapter;
    private List<String> avatarList;
    private RecyclerView recyclerView;
    private LinearLayout llAddUser;
    private Button addUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom_page);

        loadData();
        initView();
        setListeners();
    }

    private void setListeners() {
        addUser.setOnClickListener(view -> {
            avatarList.add("avatar-");
            itemAdapter.updateData(avatarList);
            if (recyclerView.getAdapter().getItemCount()>=5){
                llAddUser.setVisibility(View.GONE);
            }else {
                llAddUser.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadData() {
        avatarList = new ArrayList<>();
        for(int i=0; i<1; i++){
            avatarList.add("avatar-"+i);
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        itemAdapter = new StudyRoomItemAdapter(this, avatarList);
        recyclerView.setAdapter(itemAdapter);

        llAddUser = findViewById(R.id.ll_addUser);
        addUser = findViewById(R.id.add_user1);
    }
}
