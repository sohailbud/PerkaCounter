package com.example.android.perkacounter.helper;

/**
 * Implemented by {@link com.example.android.perkacounter.adapter.RecyclerViewAdapter.MyViewHolder} and
 * allows it to listen to when the user selects an item through long click and releases it
 * so appropriate UI modifications can be made to the items
 */
public interface SimpleItemTouchViewHolder {

    void itemSelected();
    void itemReleased();
}
