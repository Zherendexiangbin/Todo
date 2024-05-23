package net.onest.time.navigation.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.onest.time.R;
import net.onest.time.adapter.list.ExpandableListAdapter;
import net.onest.time.api.TaskApi;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.components.AddTaskCollectionDialog;
import net.onest.time.components.AddTaskDialog;
import net.onest.time.entity.Item;
import net.onest.time.entity.list.ParentItem;
import net.onest.time.utils.ColorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ListFragment extends Fragment {
    private ExpandableListView backLog;
    private Button addParentBtn,menuBtn;
    private List<ParentItem> parentItemList = new ArrayList<>();
    private Map<String, List<TaskVo>> parentMap;
    private ExpandableListAdapter expandableListAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        expandableListAdapter = new ExpandableListAdapter(R.layout.list_fragment_expandable_parent_list,R.layout.list_fragment_expandable_child_list,getContext(),init());
        backLog.setAdapter(expandableListAdapter);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int screenWidth = size.x;
        int width = display.getWidth();
//        backLog.setIndicatorBoundsRelative(100,100);
        backLog.setIndicatorBounds(width-320,width-290);

        //默认展开第一项
//        backLog.expandGroup(0);


        backLog.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean groupExpanded = backLog.isGroupExpanded(groupPosition);
                if(groupExpanded){
                    v.findViewById(R.id.list_fragment_parent_arrow).setBackgroundResource(R.drawable.arrow_right2);
                }else{
                    v.findViewById(R.id.list_fragment_parent_arrow).setBackgroundResource(R.drawable.arrow_down2);
                }
                return false;
            }
        });

        backLog.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), "点击了"+parentItemList.get(groupPosition).getChildItemList().get(childPosition).getTaskName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //添加待办集:
        addParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTaskCollectionDialog(
                        getContext(),
                        expandableListAdapter,
                        parentItemList
                );
            }
        });

    }

    private List<ParentItem> init() {
        parentMap = TaskApi.allByCategory();
        parentItemList = new ArrayList<>();
        parentMap.forEach((key,value)->{
                ParentItem parentItem = new ParentItem(key,ColorUtil.getColorByRgb(null),value);
                parentItemList.add(parentItem);
        });

        return parentItemList;

    }

    private void findViews(View view) {
        backLog = view.findViewById(R.id.list_fragment_backlog);
        addParentBtn = view.findViewById(R.id.list_fragment_add_btn);
        menuBtn = view.findViewById(R.id.list_fragment_menu_btn);
    }
}
