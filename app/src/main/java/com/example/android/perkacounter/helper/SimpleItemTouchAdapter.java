package com.example.android.perkacounter.helper;

/**
 * Implemented by {@link com.example.android.perkacounter.adapter.RecyclerViewAdapter} so adapter can
 * listen to onMove and onRemove requests from user and apply the changes to the data set
 */
public interface SimpleItemTouchAdapter {

    /**
     * allows adapter to move the item from old to new position
     * @param oldPos old position of the item
     * @param newPos new position of the item
     */
    boolean onMove(int oldPos, int newPos);

    /**
     * allows adapter to remove item at the specified position
     * @param pos position of the item to be removed
     */
    void onRemove(int pos);

}
