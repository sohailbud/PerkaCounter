package com.example.android.perkacounter.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.perkacounter.R;
import com.example.android.perkacounter.helper.SimpleItemTouchAdapter;
import com.example.android.perkacounter.helper.SimpleItemTouchViewHolder;
import com.example.android.perkacounter.util.Util;
import com.example.android.perkacounter.model.Counter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter to populate counter data
 * Implements {@link SimpleItemTouchAdapter} to listen to move or remove items request
 */
public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
        implements SimpleItemTouchAdapter {

    private LayoutInflater inflater;
    private List<Counter> data = new ArrayList<>();
    private Context context;
    private SharedPreferences settings;
    private RecyclerView recyclerView;

    public RecyclerViewAdapter(List<Counter> data, SharedPreferences settings, RecyclerView recyclerView) {
        this.data = data;
        context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);
        this.settings = settings;
        this.recyclerView = recyclerView;

    }

    public Context getContext() {
        return context;
    }

    /**
     * inflates the item views
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.counter_item, parent, false);
        return new MyViewHolder(view);
    }


    /**
     * @return size of the data set
     */
    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * populates data by accessing views through an instnace of {@link MyViewHolder}
     * updates counter by listening to clicks on increase decrease views
     * updates shared preferences
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        // populate data
        holder.counterName.setText(data.get(position).getName());
        holder.counterCount.setText(String.valueOf(data.get(position).getCount()));

        // implements OnItemClickListener to listen for clicks

        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Counter c = data.get(position);

                switch (view.getId()) {
                    case R.id.counter_increase:
                        c.increaseCount();
                        break;
                    case R.id.counter_decrease:
                        c.decreaseCount();
                        break;
                }

                holder.counterCount.setText(String.valueOf(c.getCount()));

                updateSharedpreferences();
            }
        });
    }

    /**
     * used to add new data to adapter
     */
    public void insertData(Counter counter) {
        data.add(counter);
        notifyItemInserted(data.size() - 1);
        updateSharedpreferences();
    }

    /**
     * Gets and holds instance of the views
     * implements {@link android.view.View.OnClickListener} to listen to clicks on the views
     * implements {@link SimpleItemTouchViewHolder} to listen to move requests and modify views accordingly
     */
    protected class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, SimpleItemTouchViewHolder {

        private TextView counterName;
        private TextView counterCount;
        private ImageView counterIncrease;
        private ImageView counterDecrease;

        private OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            counterName = (TextView) itemView.findViewById(R.id.counter_name);
            counterCount = (TextView) itemView.findViewById(R.id.counter_count);
            counterIncrease = (ImageView) itemView.findViewById(R.id.counter_increase);
            counterDecrease = (ImageView) itemView.findViewById(R.id.counter_decrease);

            counterIncrease.setOnClickListener(this);
            counterDecrease.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        /**
         * informs that a click was performed
         */
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }

        /**
         * called when item is selected through long click to initiate a move
         * changes color to inform user that item is ready to be moved
         */
        @Override
        public void itemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        /**
         * changed color back to white when item is released
         */
        @Override
        public void itemReleased() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * helper method to update shared preferences
     * converts the data to json string and inserts that into shared preferences
     */
    private void updateSharedpreferences() {
        Gson gson = new Gson();
        String json = gson.toJson(data);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Util.PREFS_COUNTER_DATA_KEY, json);

        editor.apply();
    }

    /**
     * Part of the {@link SimpleItemTouchAdapter}
     * moves the item and informs the adapter
     */
    @Override
    public boolean onMove(int oldPos, int newPos) {
        Collections.swap(data, oldPos, newPos);
        notifyItemMoved(oldPos, newPos);
        return true;
    }

    /**
     * Part of the {@link SimpleItemTouchAdapter}
     * called when remove of item is requested by swiping the item
     * removes the data and notifies the adapter
     * saves a temp instance of the data removed if user wants to undo the action
     */
    @Override
    public void onRemove(final int pos) {
        final Counter dataToRemove = data.get(pos);
        data.remove(pos);
        notifyItemRemoved(pos);

        // snackbar with undo option to insert the removed data back
        Snackbar.make(recyclerView, "Counter removed", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.add(pos, dataToRemove);
                        notifyItemInserted(pos);
                    }
                }).show();

        // update the shared preferences
        updateSharedpreferences();
    }

    /**
     * used by {@link MyViewHolder} to inform onBindViewHolder that a click on performed
     */
    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
