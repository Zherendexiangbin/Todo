package net.onest.time.entity;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Item {
    private String itemName;
    private String time;

    private Date createdTime;
    private int color;
    private Drawable drawable;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Item() {
    }

    public Item(String itemName, String time) {
        this.itemName = itemName;
        this.time = time;
    }

    public Item(String itemName, String time, Date createdTime) {
        this.itemName = itemName;
        this.time = time;
        this.createdTime = createdTime;
    }

    public Item(String itemName, String time, int color) {
        this.itemName = itemName;
        this.time = time;
        this.color = color;
    }

    public Item(String itemName, String time, Date createdTime, int color) {
        this.itemName = itemName;
        this.time = time;
        this.createdTime = createdTime;
        this.color = color;
    }

    public Item(String itemName, String time, Date createdTime, Drawable drawable) {
        this.itemName = itemName;
        this.time = time;
        this.createdTime = createdTime;
        this.drawable = drawable;
    }

    public Item(String itemName, String time, Drawable drawable) {
        this.itemName = itemName;
        this.time = time;
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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
