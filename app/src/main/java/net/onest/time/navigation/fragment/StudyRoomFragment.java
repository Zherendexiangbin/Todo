package net.onest.time.navigation.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.wynsbin.vciv.VerificationCodeInputView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.StudyRoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomFragment extends Fragment {
    private View view;
    private TextView roomName;
    private StudyRoomItemAdapter itemAdapter;
    private List<String> avatarList;
    private RecyclerView recyclerView;
    private Button btnAdd;
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
                chatMenu.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定退出自习室吗").setPositiveButton("确定", ((dialogInterface, i) -> {
                            btnAdd.setBackgroundResource(R.mipmap.add2);
                            btnAdd.setHint("add");
                            for (int a = avatarList.size()-1; a>0; a--){
                                avatarList.remove(a);
                            }
                            itemAdapter.updateData(avatarList);
                            Log.e("YES", "确定");
                            Toast.makeText(getContext(), "退出成功！", Toast.LENGTH_SHORT).show();
                            roomName.setText("时光自习室");
                        })).setNegativeButton("取消", (dialogInterface, i) -> Log.e("NO", "取消")).create()
                        .show();
            }
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
        recyclerView = view.findViewById(R.id.user_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new StudyRoomItemAdapter(getContext(), avatarList);
        recyclerView.setAdapter(itemAdapter);


        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setHint("add");
    }


    //房间号输入
    private class RoomCodePopWindow extends PopupWindow {
        private RadioButton createRoom, joinRoom;
        private VerificationCodeInputView codeInputView;

        public RoomCodePopWindow(Context context) {
            //设置view
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate((R.layout.activity_roomcode_page), null);
            setContentView(view);
            initView(view);//获取控件
            //activity的contentView的宽度
            int width = ((Activity) context).findViewById(android.R.id.content).getWidth();
            //其他设置
            setHeight(dp2px(160));//设置高度
            setWidth(width);//设置宽度
            setFocusable(true);//是否获取焦点
            setOutsideTouchable(true);//是否可以通过点击屏幕外关闭
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            //加入动画
            ObjectAnimator.ofFloat(getContentView(), "translationY", getHeight(), 0).setDuration(200).start();
        }
        /**
         * 获取屏幕宽高
         *
         * @param context
         * @return
         */
        private int[] getScreenSize(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
        }

        /**
         * Value of dp to value of px.
         *
         * @param dpValue The value of dp.
         * @return value of px
         */
        private int dp2px(final float dpValue) {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        private void initView(View view) {
            //获取控件
            createRoom = view.findViewById(R.id.create_room);
            joinRoom = view.findViewById(R.id.join_room);
            codeInputView = view.findViewById(R.id.code_input);

            createRoom.setOnClickListener(view1 -> {
                joinRoom.setChecked(false);
                createRoom.setBackgroundResource(R.drawable.button_wheat);
                joinRoom.setBackgroundResource(R.drawable.button_white);
                Toast.makeText(getContext(), "请设置自习室房间号", Toast.LENGTH_SHORT).show();
            });

            joinRoom.setOnClickListener(view1 -> {
                createRoom.setChecked(false);
                createRoom.setBackgroundResource(R.drawable.button_white);
                joinRoom.setBackgroundResource(R.drawable.button_wheat);
                Toast.makeText(getContext(), "请输入要加入的自习室房间号", Toast.LENGTH_SHORT).show();
            });

            codeInputView.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
                @Override
                public void onComplete(String code) {
                    if (createRoom.isChecked()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("确定将房间号设置为："+ code + "吗？").setPositiveButton("确定", ((dialogInterface, i) -> {
                            btnAdd.setBackgroundResource(R.mipmap.quit);
                            btnAdd.setHint("quit");
                            Log.e("YES", "确定");
                            Toast.makeText(getContext(), "创建成功", Toast.LENGTH_SHORT).show();
                            roomName.setText("房间号："+code);
                            dismiss();
                        })).setNegativeButton("取消", (dialogInterface, i) -> Log.e("NO", "取消")).create()
                                .show();
                    }else if (joinRoom.isChecked()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("确定加入房间号为："+ code + "的自习室吗？").setPositiveButton("确定", ((dialogInterface, i) -> {
                                    btnAdd.setBackgroundResource(R.mipmap.quit);
                                    btnAdd.setHint("quit");

                                    avatarList.add("user");
                                    itemAdapter.updateData(avatarList);
                                    if (recyclerView.getAdapter().getItemCount()>1){
                                        roomName.setText("房间号："+code);
                                        Toast.makeText(getContext(), "加入成功", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.e("YES", "确定");
                                    dismiss();
                                })).setNegativeButton("取消", (dialogInterface, i) -> Log.e("NO", "取消")).create()
                                .show();
                    }else {
                        Toast.makeText(getContext(), "请先选择创建或加入自习室！", Toast.LENGTH_SHORT).show();
                        //清除验证码
                        codeInputView.clearCode();
                    }
                }

                @Override
                public void onInput() {

                }
            });
        }
    }
}
