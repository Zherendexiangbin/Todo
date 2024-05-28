package net.onest.time.navigation.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.wynsbin.vciv.VerificationCodeInputView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.ApplicationItemAdapter;
import net.onest.time.adapter.studyroom.StudyRoomItemAdapter;
import net.onest.time.api.RoomApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.RoomDto;
import net.onest.time.api.vo.RoomVo;
import net.onest.time.api.vo.UserVo;
import net.onest.time.navigation.activity.PersonEditActivity;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomFragment extends Fragment {
    private RoomDto roomDto;
    private RoomVo roomVo;
    private UserVo userVo;
    private View view;
    private ImageView roomAvatar, userRefresh;
    private TextView roomName, roomManager;
    private String roomId;
    private StudyRoomItemAdapter itemAdapter;
    private List<UserVo> userVos;
    private RecyclerView recyclerView;
    private Button btnMenu, btnAdd;
    private Boolean isMaster;//是否为房间创建者

    //调用相册
    private String avatarString = "null";
    private static final int PICK_IMAGE = 1;
    private Uri avatarUri;
    private ImageView popRoomAvatar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.study_room_fragment, container, false);
        userVo = UserApi.getUserInfo();
        userVos = new ArrayList<>();
        avatarString = userVo.getAvatar();
        roomVo = new RoomVo();
        loadData();
        findViewById(view);
        setListeners();

        return view;
    }

    private void setListeners() {
        //刷新自习室信息
        userRefresh.setOnClickListener(view1 -> {

            userVos = RoomApi.listUsers(roomVo.getRoomId());

            Glide.with(getContext())
                    .load(roomVo.getRoomAvatar())
                    .into(roomAvatar);
            roomName.setText(roomVo.getRoomName());
            roomManager.setText("管理员：" + userVo.getUserName());

            if (userVos != null){
                itemAdapter.updateData(userVos);
            }
        });

        btnAdd.setOnClickListener(view -> {
            if(btnAdd.getHint().equals("add")){
                RoomCodePopWindow chatMenu = new RoomCodePopWindow(getContext());
                chatMenu.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.TOP, 0, 0);
            } else if (btnAdd.getHint().equals("dissolution")) {
                //解散自习室
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .asConfirm("", "确定解散自习室吗？",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        btnAdd.setBackgroundResource(R.mipmap.add3);
                                        btnAdd.setHint("add");

                                        RoomApi.deleteRoom(roomVo.getRoomId());
                                        userVos.clear();
                                        loadData();
                                        itemAdapter.updateData(userVos);

                                        roomName.setText("时光自习室");
                                        roomManager.setVisibility(View.GONE);
                                        userRefresh.setVisibility(View.GONE);
                                        btnMenu.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "解散成功！", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .show();
            } else {
                //退出自习室
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .asConfirm("", "确定退出自习室吗？",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                btnAdd.setBackgroundResource(R.mipmap.add3);
                                btnAdd.setHint("add");

                                RoomApi.userExit(roomVo.getRoomId());
                                userVos.clear();
                                loadData();
                                itemAdapter.updateData(userVos);

                                roomName.setText("时光自习室");
                                roomManager.setVisibility(View.GONE);
                                userRefresh.setVisibility(View.GONE);
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
        UserVo user = new UserVo();
        user.setUserId(userVo.getUserId());
        user.setUserName(userVo.getUserName());
        user.setAvatar(userVo.getAvatar());
        user.setSignature(userVo.getSignature());
        userVos.add(user);
    }

    private void findViewById(View view) {
        roomAvatar = view.findViewById(R.id.room_avatar);
        roomName = view.findViewById(R.id.room_name);
        roomManager = view.findViewById(R.id.room_manager);
        roomManager.setVisibility(View.GONE);

        userRefresh = view.findViewById(R.id.user_refresh);
        userRefresh.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.user_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new StudyRoomItemAdapter(getContext(), userVos);
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
        private LinearLayout llRoomAvatar, llRoomCode;
//        private ImageView popRoomAvatar;
        private TextView setRoomAvatar;
        private Button btnCreate;

        public RoomCodePopWindow(Context context) {
            //设置view
            LayoutInflater inflater = LayoutInflater.from(context);
            View view1 = inflater.inflate((R.layout.activity_create_room_pop), null);
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
            llRoomAvatar = view.findViewById(R.id.room_avatar_set);
            llRoomCode = view.findViewById(R.id.ll_room_code);
            popRoomAvatar = view.findViewById(R.id.pop_room_avatar);
            setRoomAvatar = view.findViewById(R.id.set_room_avatar);
            btnCreate = view.findViewById(R.id.btn_create);

            Glide.with(getContext())
                    .load(userVo.getAvatar())
                    .into(popRoomAvatar);

            joinRoom.setChecked(false);
            llRoomCode.setVisibility(View.GONE);
            setName.setVisibility(View.VISIBLE);
            llRoomAvatar.setVisibility(View.VISIBLE);
            createRoom.setBackgroundResource(R.drawable.button_wheat);
            joinRoom.setBackgroundResource(R.drawable.button_white);
            Toast.makeText(getContext(), "请设置自习室房间名称和房间号", Toast.LENGTH_SHORT).show();

            createRoom.setOnClickListener(view1 -> {
                joinRoom.setChecked(false);
                llRoomCode.setVisibility(View.GONE);
                setName.setVisibility(View.VISIBLE);
                llRoomAvatar.setVisibility(View.VISIBLE);
                createRoom.setBackgroundResource(R.drawable.button_wheat);
                joinRoom.setBackgroundResource(R.drawable.button_white);
                Toast.makeText(getContext(), "请设置自习室房间名称和房间号", Toast.LENGTH_SHORT).show();
            });

            joinRoom.setOnClickListener(view1 -> {
                createRoom.setChecked(false);
                setName.setVisibility(View.GONE);
                llRoomAvatar.setVisibility(View.GONE);
                llRoomCode.setVisibility(View.VISIBLE);
                createRoom.setBackgroundResource(R.drawable.button_white);
                joinRoom.setBackgroundResource(R.drawable.button_wheat);
                Toast.makeText(getContext(), "请输入要加入的自习室邀请码", Toast.LENGTH_SHORT).show();
            });

            //创建自习室
            btnCreate.setOnClickListener(view1 -> {
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .asConfirm("创建自习室", setName.getText().toString(),
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        roomDto = new RoomDto();
                                        //设置自习室名称
                                        if (setName.getText().toString().equals("")){
                                            roomName.setText("时光自习室");
                                            roomDto.setRoomName("时光自习室");
                                        }else {
                                            roomName.setText(setName.getText().toString());
                                            roomDto.setRoomName(setName.getText().toString());
                                        }
                                        //设置自习室管理员
                                        roomManager.setVisibility(View.VISIBLE);
                                        roomManager.setText("管理员：" + userVo.getUserName());
                                        //设置自习室头像
                                        roomAvatar.setImageURI(avatarUri);
                                        roomDto.setRoomAvatar(avatarString);

                                        userRefresh.setVisibility(View.VISIBLE);
                                        btnMenu.setVisibility(View.VISIBLE);
                                        btnAdd.setBackgroundResource(R.mipmap.quit);
                                        btnAdd.setHint("dissolution");

                                        //创建自习室，获取自习室信息
                                        roomVo = RoomApi.createRoom(roomDto);

                                        dismiss();
                                        Toast.makeText(getContext(), "创建成功！", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .show();
            });

            //设置自习室头像
            setRoomAvatar.setOnClickListener(view1 -> {
                //点击修改头像
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            });

            //加入自习室
            idInputView.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
                @Override
                public void onComplete(String code) {
                    new XPopup.Builder(getContext())
                            .dismissOnTouchOutside(false)
                            .asConfirm("确认加入", "是否加入自习室？",
                                new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    //向自习室管理员/创建者提交加入申请
                                    RoomApi.acceptInvitation(code);
                                    userRefresh.setVisibility(View.VISIBLE);
                                    dismiss();
                                    Toast.makeText(getContext(), "加入成功！", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
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
        private ImageView refresh;
        private RecyclerView applicationList;
        private ApplicationItemAdapter applicationItemAdapter;
        private List<UserVo> userVos2 = new ArrayList<>();

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
            refresh = view2.findViewById(R.id.refresh);
            applicationList = view2.findViewById(R.id.application_list);

            //获取自习室邀请码
            applicationCode.setText(RoomApi.generateInvitationCode(roomVo.getRoomId()));

            //刷新获取房间申请列表
            refresh.setOnClickListener(view1 -> {
                userVos2 = RoomApi.findRequests(roomVo.getRoomId());
                if (userVos2 != null){
                    applicationItemAdapter.updateData(userVos2);
                }
            });

            //绑定数据

            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            if(userVos2 != null){
                //绑定适配器
                applicationItemAdapter = new ApplicationItemAdapter(roomVo.getRoomId(), getContext(), userVos2);
            }
            applicationList.setLayoutManager(manager);
            applicationList.setAdapter(applicationItemAdapter);
        }
    }

    //调用本地相册
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                popRoomAvatar.setImageURI(selectedImageUri);
                avatarUri = selectedImageUri;
                avatarString = UserApi.uploadAvatar(getPathFromUri(getContext(), selectedImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //获取图片路径
    private Pair<String, String> getImagePath(Uri avatarUri) {
        Cursor cursor = getContext().getContentResolver()
                .query(avatarUri, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE}, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        return new Pair<>(cursor.getString(0), cursor.getString(1));
    }
    //图片URI转String
    public String getPathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }
}
