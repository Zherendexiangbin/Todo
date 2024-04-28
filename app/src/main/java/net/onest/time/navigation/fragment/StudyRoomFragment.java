package net.onest.time.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.StudyRoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomFragment extends Fragment {
    private View view;

    private StudyRoomItemAdapter itemAdapter;
    private List<String> avatarList;
    private RecyclerView recyclerView;
    private LinearLayout llAddUser;
    private Button addUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.study_room_fragment, container, false);

        findViewById(view);
        initView();
        setListeners();

        return view;
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

    private void initView() {
        avatarList = new ArrayList<>();
        for(int i=0; i<1; i++){
            avatarList.add("avatar-"+i);
        }
    }

    private void findViewById(View view) {
        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        itemAdapter = new StudyRoomItemAdapter(getContext(), avatarList);
        recyclerView.setAdapter(itemAdapter);

        llAddUser = view.findViewById(R.id.ll_addUser);
        addUser = view.findViewById(R.id.add_user1);
    }
}
