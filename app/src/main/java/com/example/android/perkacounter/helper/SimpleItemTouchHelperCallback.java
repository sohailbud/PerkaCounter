package com.example.android.perkacounter.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.android.perkacounter.R;
import com.example.android.perkacounter.adapter.RecyclerViewAdapter;

/**
 * This class is the contract between ItemTouchHelper and your application.
 * It lets you control which touch behaviors are enabled per each ViewHolder and also receive
 * callbacks when user performs these actions.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private SimpleItemTouchAdapter simpleItemTouchAdapter;

    public SimpleItemTouchHelperCallback(SimpleItemTouchAdapter simpleItemTouchAdapter) {
        this.simpleItemTouchAdapter = simpleItemTouchAdapter;
    }

    /**
     * calls {@link SimpleItemTouchAdapter#onRemove(int)} to remove an item when user swipes
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        simpleItemTouchAdapter.onRemove(viewHolder.getAdapterPosition());

    }

    /**
     * calls {@link SimpleItemTouchAdapter#onMove(int, int)} to move the item from pos A to B
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        simpleItemTouchAdapter.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    /**
     * specify drag and swipe flags allowed
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * Used to draw the background of the item when the item is swiped
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;

            // Gets the context
            Context context = ((RecyclerViewAdapter) simpleItemTouchAdapter).getContext();

            // create a paint object to set background color and pass it to canvas
            Paint p = new Paint();
            p.setColor(context.getResources().getColor(R.color.colorAccent));

            // get the delete icon from resources
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_delete_grey_200_36dp);


            // If horizontal displacement caused by user's action is greater than 0
            if (dX > 0) {

                // calculate the margin to apply around the delete icon
                float margin = ((float) itemView.getBottom() - (float) itemView.getTop() - bitmap.getHeight()) / 2;

                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);
                c.drawBitmap(bitmap,
                        (float) itemView.getLeft() + margin,
                        (float) itemView.getTop() + margin,
                        p);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    /**
     * calls {@link SimpleItemTouchViewHolder#itemSelected()} when user long clicks on item
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (viewHolder instanceof SimpleItemTouchViewHolder) {
                SimpleItemTouchViewHolder itemTouchViewHolder = (SimpleItemTouchViewHolder) viewHolder;
                itemTouchViewHolder.itemSelected();
            }
        }
    }

    /**
     * calls {@link SimpleItemTouchViewHolder#itemReleased()} when user releases the item
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof SimpleItemTouchViewHolder) {
            SimpleItemTouchViewHolder itemTouchViewHolder = (SimpleItemTouchViewHolder) viewHolder;
            itemTouchViewHolder.itemReleased();
        }
    }
}
