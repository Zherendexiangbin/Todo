package net.onest.time.entity.list;

import net.onest.time.api.vo.TaskVo;
import net.onest.time.entity.Item;

import java.util.List;

public class ParentItem {
    private String parentItemName;
    private int parentItemColor;
    private List<TaskVo> childItemList;

    public ParentItem() {
    }

    public ParentItem(String parentItemName, int parentItemColor, List<TaskVo> childItemList) {
        this.parentItemName = parentItemName;
        this.parentItemColor = parentItemColor;
        this.childItemList = childItemList;
    }

    public String getParentItemName() {
        return parentItemName;
    }

    public void setParentItemName(String parentItemName) {
        this.parentItemName = parentItemName;
    }

    public int getParentItemColor() {
        return parentItemColor;
    }

    public void setParentItemColor(int parentItemColor) {
        this.parentItemColor = parentItemColor;
    }

    public List<TaskVo> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<TaskVo> childItemList) {
        this.childItemList = childItemList;
    }
}
