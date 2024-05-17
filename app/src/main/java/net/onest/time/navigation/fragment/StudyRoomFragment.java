package net.onest.time.navigation.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.wynsbin.vciv.VerificationCodeInputView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.ApplicationItemAdapter;
import net.onest.time.adapter.studyroom.StudyRoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomFragment extends Fragment {
    private View view;
    private TextView roomName, roomId;
    private StudyRoomItemAdapter itemAdapter;
    private List<String> avatarList;
    private RecyclerView recyclerView;
    private Button btnMenu, btnAdd;
    private Boolean isMaster;//是否为房间创建者
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.study_room_fragment, container, false);

        loadData();
        findViewById(view);
        setListeners();

        return view;
    }

    private void setListeners() {
        btnAdd.setOnClickListener(view -> {

//            avatarList.add("avatar-");
//            itemAdapter.updateData(avatarList);
//            if (recyclerView.getAdapter().getItemCount()>1){
//                System.out.println(recyclerView.getAdapter().getItemCount());
//            }
            if(btnAdd.getHint().equals("add")){
                RoomCodePopWindow chatMenu = new RoomCodePopWindow(getContext());
                chatMenu.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.TOP, 0, 0);
            }else {
                new XPopup.Builder(getContext()).asConfirm("", "确定退出自习室吗？",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                btnAdd.setBackgroundResource(R.mipmap.add3);
                                btnAdd.setHint("add");
                                for (int a=avatarList.size()-1; a>0; a--){
                                    avatarList.remove(a);
                                }
                                itemAdapter.updateData(avatarList);

                                roomName.setText("时光自习室");
                                roomId.setText("roomId");
                                btnMenu.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "退出成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        //管理员 菜单查看邀请码、房间加入申请
        btnMenu.setOnClickListener(view -> {
            MenuPopWindow menuPopWindow = new MenuPopWindow(getContext());
            menuPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        });
    }

    private void loadData() {
        avatarList = new ArrayList<>();
        for(int i=0; i<1; i++){
            avatarList.add("user"+i);
        }
    }

    private void findViewById(View view) {
        roomName = view.findViewById(R.id.room_name);
        roomId = view.findViewById(R.id.room_id);
        recyclerView = view.findViewById(R.id.user_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new StudyRoomItemAdapter(getContext(), avatarList);
        recyclerView.setAdapter(itemAdapter);

        btnMenu = view.findViewById(R.id.btn_room_menu);
        btnMenu.setVisibility(View.GONE);

        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setHint("add");
    }


    //房间号、邀请码输入
    private class RoomCodePopWindow extends PopupWindow {
        private RadioButton createRoom, joinRoom;
        private EditText setName;
        private VerificationCodeInputView idInputView;

        public RoomCodePopWindow(Context context) {
            //设置view
            LayoutInflater inflater = LayoutInflater.from(context);
            View view1 = inflater.inflate((R.layout.activity_roomid_pop), null);
            setContentView(view1);
            initView(view1);//获取控件
            //activity的contentView的宽度
            int width = ((Activity) context).findViewById(android.R.id.content).getWidth();
            //其他设置
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置高度
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置宽度
            setFocusable(true);//是否获取焦点
            setOutsideTouchable(true);//是否可以通过点击屏幕外关闭
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            //加入动画
            ObjectAnimator.ofFloat(getContentView(), "translationY", getHeight(), 0).setDuration(200).start();
        }

        private void initView(View view) {
            //获取控件
            createRoom = view.findViewById(R.id.create_room);
            joinRoom = view.findViewById(R.id.join_room);
            setName = view.findViewById(R.id.edit_roomName);
            idInputView = view.findViewById(R.id.id_input);

            setName.setVisibility(View.GONE);

            createRoom.setOnClickListener(view1 -> {
                joinRoom.setChecked(false);
                setName.setVisibility(View.VISIBLE);
                createRoom.setBackgroundResource(R.drawable.button_wheat);
                joinRoom.setBackgroundResource(R.drawable.button_white);
                Toast.makeText(getContext(), "请设置自习室房间名称和房间号", Toast.LENGTH_SHORT).show();
            });

            joinRoom.setOnClickListener(view1 -> {
                createRoom.setChecked(false);
                setName.setVisibility(View.GONE);
                createRoom.setBackgroundResource(R.drawable.button_white);
                joinRoom.setBackgroundResource(R.drawable.button_wheat);
                Toast.makeText(getContext(), "请输入要加入的自习室邀请码", Toast.LENGTH_SHORT).show();
            });

            idInputView.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
                @Override
                public void onComplete(String code) {
                    if (createRoom.isChecked()){
                        new XPopup.Builder(getContext()).asConfirm(
                                "确认创建",
                                "房间名称：" + setName.getText().toString().trim() + "\n" + "房间号" + code,
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        btnAdd.setBackgroundResource(R.mipmap.quit);
                                        btnAdd.setHint("quit");
                                        roomName.setText(setName.getText().toString().trim());
                                        roomId.setText("roomId: " + code);
                                        btnMenu.setVisibility(View.VISIBLE);
                                        dismiss();
                                        Toast.makeText(getContext(), "创建成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }else if (joinRoom.isChecked()){
                        new XPopup.Builder(getContext()).asConfirm(
                                "确认加入",
                                "是否提交自习室加入申请？",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        //向自习室管理员/创建者提交加入申请
                                        //Todo
                                        //如果同意
                                        btnAdd.setBackgroundResource(R.mipmap.quit);
                                        btnAdd.setHint("quit");
                                        avatarList.add("user");
                                        itemAdapter.updateData(avatarList);
                                        if(recyclerView.getAdapter().getItemCount()>1){
                                            roomName.setText("****");
                                            roomId.setText("roomId: " + code);
                                        }
                                        dismiss();
                                        Toast.makeText(getContext(), "加入成功！", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }else {
                        Toast.makeText(getContext(), "请先选择创建或加入自习室！", Toast.LENGTH_SHORT).show();
                        //清除验证码
                        idInputView.clearCode();
                    }
                }

                @Override
                public void onInput() {
                }
            });
        }
    }

    //查看邀请码、加入房间申请
    private  class MenuPopWindow extends PopupWindow{
        private TextView applicationCode;
        private RecyclerView applicationList;
        private ApplicationItemAdapter applicationItemAdapter;

        public MenuPopWindow(Context context){
            //设置view
            LayoutInflater inflater = LayoutInflater.from(context);
            View view2 = inflater.inflate((R.layout.acticity_room_menu), null);
            setContentView(view2);
            initView(view2);//获取控件、初始化控件
            //activity的contentView的高度、宽度
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(false);//是否获取焦点
            setOutsideTouchable(true);//是否可以通过点击屏幕外关闭
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            //加入动画
            ObjectAnimator.ofFloat(getContentView(), "translationY", getHeight(), 0).setDuration(200).start();
        }

        private void initView(View view2) {
            applicationCode = view2.findViewById(R.id.application_code);
            applicationList = view2.findViewById(R.id.application_list);

            //获取房间申请列表
            List<String> info = new ArrayList<>();
            for (int i=0; i<10; i++){
                info.add("用户" + i);
            }
            //绑定数据
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            applicationItemAdapter = new ApplicationItemAdapter(getContext(), info);
            applicationList.setLayoutManager(manager);
            applicationList.setAdapter(applicationItemAdapter);
        }
    }
}
