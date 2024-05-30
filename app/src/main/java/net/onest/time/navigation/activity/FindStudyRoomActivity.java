package net.onest.time.navigation.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.time.R;
import net.onest.time.adapter.studyroom.StudyRoomAdapter;
import net.onest.time.api.RoomApi;
import net.onest.time.api.dto.RoomDto;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.RoomVo;

import java.util.ArrayList;
import java.util.List;

public class FindStudyRoomActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView roomList;
    private StudyRoomAdapter studyRoomAdapter;
    private StudyRoomAdapter filteredItemAdapter;//用于模糊查询
    private List<RoomVo> roomVos = new ArrayList<>();
    private List<RoomVo> filteredData;//用于模糊查询
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_findroom_page);

        View rootLayout = getWindow().getDecorView();//获取根布局

        findView();//获取控件
        initData();//获取数据

        //点击SearchView外收起键盘
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchView.clearFocus();
                //获取点击位置的坐标
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();

                //创建矩形对象，表示SearchView的边界
                Rect searchViewBounds = new Rect();
                searchView.getHitRect(searchViewBounds);

                //判断点击位置是否在SearchView之外
                if(searchViewBounds.contains(x, y)){
                    //收起键盘
                    searchView.clearFocus();
                }
                return true;
            }
        });

        //模糊查询
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filteredItemAdapter = new StudyRoomAdapter(FindStudyRoomActivity.this, filteredData);
                roomList.setAdapter(filteredItemAdapter);
                filterData(query);
                return true;
            }

            private void filterData(String query){
                if(query == null || query.isEmpty()){
                    filteredData = new ArrayList<>(roomVos);
                }else {
                    filteredData = new ArrayList<>();
                    for (RoomVo roomVo : roomVos){
                        if (roomVo.getRoomName().contains(query)){
                            filteredData.add(roomVo);
                        }
                    }
                }
                if (filteredData != null){
                    filteredItemAdapter.updateData(filteredData);
                }
            }
        });

    }

    private void findView() {
        searchView = findViewById(R.id.search_room);
        roomList = findViewById(R.id.room_list);

        searchView.setQueryHint("输入想要加入的自习室名称");
    }

    private void initData() {
        Page<RoomVo> roomVoList = new Page<>();
        roomVoList = RoomApi.findRooms(null, 1, 10);
        roomVos = roomVoList.getRecords();


//        for (int i=0; i<20; i++){
//            RoomVo roomVo = new RoomVo();
//            roomVo.setRoomName("Room--" + i);
//            roomVo.setRoomId(Long.parseLong("111"));
//            roomVo.setRoomAvatar("");
//            roomVos.add(roomVo);
//        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(FindStudyRoomActivity.this);
        if (roomVos != null){
            studyRoomAdapter = new StudyRoomAdapter(FindStudyRoomActivity.this, roomVos);
        }
        roomList.setLayoutManager(manager);
        roomList.setAdapter(studyRoomAdapter);
    }

    //取消键盘
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
