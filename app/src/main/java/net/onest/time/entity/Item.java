package net.onest.time.entity;

public class Item {
    private String itemName;
    private String time;

    public Item() {
    }

    public Item(String itemName, String time) {
        this.itemName = itemName;
        this.time = time;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
