package com.macrew.entities;

public class StateListItem {
    public String itemTitle;
    public long id;
    public Boolean isItemSelected;
 
    public StateListItem(String name, long id) {
        this.itemTitle = name;
        this.isItemSelected = false;
        this.id = id;
    }
 
    @Override
    public String toString() {
        return this.itemTitle;
    }
}