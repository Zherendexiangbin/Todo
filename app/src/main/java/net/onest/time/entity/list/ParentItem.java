package net.onest.time.entity.list;

import net.onest.time.entity.Item;

import java.util.List;

public class ParentItem {
    private String parentItemName;
    private int parentItemColor;
    private List<Item> childItemList;

    public ParentItem() {
    }

    public ParentItem(String parentItemName, int parentItemColor, List<Item> childItemList) {
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

    public List<Item> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<Item> childItemList) {
        this.childItemList = childItemList;
    }
}
