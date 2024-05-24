package net.onest.time.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import net.onest.time.R;
import net.onest.time.adapter.list.ExpandableListAdapter;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.entity.list.ParentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加待办集合
 */
public class AddTaskCollectionDialog extends AlertDialog {
    private Button addYes;
    private Button addNo;
    private RadioGroup groupOne;
    private RadioGroup groupTwo;
    private EditText edit;

    private final ExpandableListAdapter expandableListAdapter;
    private final List<ParentItem> parentItemList;

    public AddTaskCollectionDialog(@NonNull Context context, ExpandableListAdapter expandableListAdapter, List<ParentItem> parentItemList) {
        super(context);

        this.expandableListAdapter = expandableListAdapter;
        this.parentItemList = parentItemList;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.list_fragment_add_parent_item_pop_window, null);

        show();
        getWindow().setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViews(dialogView);
        setListeners();
    }

    private void findViews(View dialogView) {
        addYes = dialogView.findViewById(R.id.add_list_item_yes);
        addNo = dialogView.findViewById(R.id.add_list_item_no);
        groupOne = dialogView.findViewById(R.id.list_fragment_pop_window_group_one);
        groupTwo = dialogView.findViewById(R.id.list_fragment_pop_window_group_two);
        edit = dialogView.findViewById(R.id.list_fragment_group_edit);
    }

    private void setListeners() {
        groupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (groupOne.getCheckedRadioButtonId() != -1
                        && groupTwo.getCheckedRadioButtonId() != -1) {
                    groupTwo.clearCheck();
                }
                switch (checkedId) {
                    case R.id.list_fragment_group_one_card_view_blue:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000ff")));
                        edit.setTextColor(Color.parseColor("#0000ff"));
                        edit.setHintTextColor(Color.parseColor("#0000ff"));
                        break;

                    case R.id.list_fragment_group_one_card_view_brown:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a52a2a")));
                        edit.setTextColor(Color.parseColor("#a52a2a"));
                        edit.setHintTextColor(Color.parseColor("#a52a2a"));
                        break;

                    case R.id.list_fragment_group_one_card_view_gray:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                        edit.setTextColor(Color.parseColor("#808080"));
                        edit.setHintTextColor(Color.parseColor("#808080"));
                        break;

                    case R.id.list_fragment_group_one_card_view_pink:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc0cb")));
                        edit.setTextColor(Color.parseColor("#ffc0cb"));
                        edit.setHintTextColor(Color.parseColor("#ffc0cb"));
                        break;
                }
            }
        });

        groupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 没选 或 另一个选了
                if (groupTwo.getCheckedRadioButtonId() != -1
                        && groupOne.getCheckedRadioButtonId() != -1) {
                    groupOne.clearCheck();
                }
                switch (checkedId) {
                    case R.id.list_fragment_group_two_card_view_blueviolet:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8a2be2")));
                        edit.setTextColor(Color.parseColor("#8a2be2"));
                        edit.setHintTextColor(Color.parseColor("#8a2be2"));
                        break;

                    case R.id.list_fragment_group_two_card_view_lightgreen:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#90ee90")));
                        edit.setTextColor(Color.parseColor("#90ee90"));
                        edit.setHintTextColor(Color.parseColor("#90ee90"));
                        break;

                    case R.id.list_fragment_group_two_card_view_purple:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#800080")));
                        edit.setTextColor(Color.parseColor("#800080"));
                        edit.setHintTextColor(Color.parseColor("#800080"));
                        break;

                    case R.id.list_fragment_group_two_card_view_red:
                        edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E83141")));
                        edit.setTextColor(Color.parseColor("#E83141"));
                        edit.setHintTextColor(Color.parseColor("#E83141"));
                        break;
                }
            }
        });

        addYes.setOnClickListener(v -> {
            String parentName = edit.getText().toString().trim();
            if (!parentName.isEmpty()) {
                int parentColor = edit.getCurrentTextColor();
                List<TaskVo> childItemList = new ArrayList<>();
                ParentItem parentItem = new ParentItem(parentName, parentColor, childItemList);
                parentItemList.add(parentItem);
                expandableListAdapter.setParentItemList(parentItemList);
                expandableListAdapter.notifyDataSetChanged();
                dismiss();
            } else {
                Toast toast = Toast.makeText(getContext(), "请输入待办集名称", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        });
        addNo.setOnClickListener(v -> dismiss());
    }
}
