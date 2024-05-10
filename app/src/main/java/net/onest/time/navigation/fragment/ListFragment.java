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
import net.onest.time.entity.Item;
import net.onest.time.entity.list.ParentItem;
import net.onest.time.utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {
    private ExpandableListView backLog;
    private Button addParentBtn,menuBtn;
    private List<ParentItem> parentItemList = new ArrayList<>();
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
        backLog.expandGroup(0);

        backLog.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        backLog.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), "点击了"+parentItemList.get(groupPosition).getChildItemList().get(childPosition).getItemName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //添加待办集:
        addParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.list_fragment_add_parent_item_pop_window,null);
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button addYes = dialogView.findViewById(R.id.add_list_item_yes);
                Button addNo = dialogView.findViewById(R.id.add_list_item_no);
                RadioGroup groupOne = dialogView.findViewById(R.id.list_fragment_pop_window_group_one);
                RadioGroup groupTwo = dialogView.findViewById(R.id.list_fragment_pop_window_group_two);

                EditText edit = dialogView.findViewById(R.id.list_fragment_group_edit);

                groupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.list_fragment_group_one_card_view_blue:
                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000ff")));
                                edit.setTextColor(Color.parseColor("#0000ff"));
                                edit.setHintTextColor(Color.parseColor("#0000ff"));

//                                groupTwo.clearCheck();
                                break;
                            case R.id.list_fragment_group_one_card_view_brown:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a52a2a")));
                                edit.setTextColor(Color.parseColor("#a52a2a"));
                                edit.setHintTextColor(Color.parseColor("#a52a2a"));
//                                groupTwo.clearCheck();
                                break;
                            case R.id.list_fragment_group_one_card_view_gray:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                                edit.setTextColor(Color.parseColor("#808080"));
                                edit.setHintTextColor(Color.parseColor("#808080"));
//                                groupTwo.clearCheck();
                                break;
                            case R.id.list_fragment_group_one_card_view_pink:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc0cb")));
                                edit.setTextColor(Color.parseColor("#ffc0cb"));
                                edit.setHintTextColor(Color.parseColor("#ffc0cb"));
//                                groupTwo.clearCheck();
                                break;
                        }
                    }
                });

                groupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.list_fragment_group_two_card_view_blueviolet:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8a2be2")));
                                edit.setTextColor(Color.parseColor("#8a2be2"));
                                edit.setHintTextColor(Color.parseColor("#8a2be2"));
//                                groupOne.clearCheck();
                                break;
                            case R.id.list_fragment_group_two_card_view_lightgreen:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#90ee90")));
                                edit.setTextColor(Color.parseColor("#90ee90"));
                                edit.setHintTextColor(Color.parseColor("#90ee90"));
//                                groupOne.clearCheck();
                                break;
                            case R.id.list_fragment_group_two_card_view_purple:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#800080")));
                                edit.setTextColor(Color.parseColor("#800080"));
                                edit.setHintTextColor(Color.parseColor("#800080"));
//                                groupOne.clearCheck();
                                break;
                            case R.id.list_fragment_group_two_card_view_red:

                                edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E83141")));
                                edit.setTextColor(Color.parseColor("#E83141"));
                                edit.setHintTextColor(Color.parseColor("#E83141"));
//                                groupOne.clearCheck();
                                break;
                        }
                    }
                });

                addYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String parentName = edit.getText().toString().trim();
                        if(!"".equals(parentName)){
                            int parentColor = edit.getCurrentTextColor();
                            List<Item> childItemList = new ArrayList<>();
                            ParentItem parentItem = new ParentItem(parentName,parentColor,childItemList);
                            parentItemList.add(parentItem);
                            expandableListAdapter.setParentItemList(parentItemList);
                            expandableListAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }else{
                            Toast toast = Toast.makeText(getContext(), "请输入待办集名称", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,0);
                            toast.show();
                        }
                    }
                });
                addNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private List<ParentItem> init() {
        parentItemList = new ArrayList<>();

        List<Item> itemList = new ArrayList<>();
        List<Item> itemList1 = new ArrayList<>();
        List<Item> itemList2 = new ArrayList<>();

        Item item1 = new Item("英语阅读","25",ColorUtil.getColorByRgb(null));
        Item item2 = new Item("数学题","25",ColorUtil.getColorByRgb(null));
        Item item3 = new Item("政治-习近平新时代中国特色社会主义思想","25",ColorUtil.getColorByRgb(null));
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        ParentItem parentItem1 = new ParentItem("考研计划", ColorUtil.getColorByRgb(null),itemList);
        ParentItem parentItem2 = new ParentItem("空计划", ColorUtil.getColorByRgb(null),itemList1);
        ParentItem parentItem3 = new ParentItem("空计划2", ColorUtil.getColorByRgb(null),itemList2);

        parentItemList.add(parentItem1);
        parentItemList.add(parentItem2);
        parentItemList.add(parentItem3);
        return parentItemList;
    }

    private void findViews(View view) {
        backLog = view.findViewById(R.id.list_fragment_backlog);
        addParentBtn = view.findViewById(R.id.list_fragment_add_btn);
        menuBtn = view.findViewById(R.id.list_fragment_menu_btn);
    }
}
