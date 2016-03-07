package com.example.android.perkacounter.activity;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.perkacounter.R;
import com.example.android.perkacounter.adapter.RecyclerViewAdapter;
import com.example.android.perkacounter.helper.SimpleItemTouchHelperCallback;
import com.example.android.perkacounter.util.Util;
import com.example.android.perkacounter.fragment.AddCounterDialogFragment;
import com.example.android.perkacounter.model.Counter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AddCounterDialogFragment.NewCounterListener {

    public final String TAG = this.getClass().getSimpleName();

    private RecyclerViewAdapter recyclerViewAdapter;

    // name of the shared preferences being created
    public static final String PREFS_COUNTER = "counterDataPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // access the shared preferences while the app is loading
        // instead of accessing it multiple times through out the activity
        SharedPreferences settings = getSharedPreferences(PREFS_COUNTER, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(fabOnClickListener);

        // checks SharedPreferences for stored data
        // data is as a json string so parse it to Counter object and pass a list of it to adapter
        List<Counter> data = new ArrayList<>();
        String json = settings.getString(Util.PREFS_COUNTER_DATA_KEY, null);
        if (json != null) {

            Gson gson = new Gson();
            Counter[] dataArray = gson.fromJson(json, Counter[].class);
            data.addAll(Arrays.asList(dataArray));
        }

        // instantiate RecyclerView, add a linear layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.counter_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an instance of adapter and attach it to the recycler view
        recyclerViewAdapter = new RecyclerViewAdapter(data, settings, recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);

        // create an instance of item decorator and add it to recycler view
        recyclerView.addItemDecoration(new FeedItemDecoration(getResources().
                getDimensionPixelSize(R.dimen.feed_item_spacing)));

        // create new instance of SimpleItemTouchHelperCallback, add it to an instance of ItemTouchHelper
        // and attach it to recycler view
        // this is to listen to swipe and move action on items in recycler view
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    /**
     * starts new {@link AddCounterDialogFragment} instance
     */
    private View.OnClickListener fabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddCounterDialogFragment addCounterDialogFragment = new AddCounterDialogFragment();
            addCounterDialogFragment.show(getFragmentManager(), null);
            addCounterDialogFragment.newCounterListener = MainActivity.this;

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
            /**
             * Initially I updated the shared preferences in onPause method
             * but it didn't work when I called to shutdown the application and
             * the onPause method would never gets called
             */
//            List<Counter> data = recyclerViewAdapter.getData();
//            String json = gson.toJson(data);
//
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString(PREFS_COUNTER_DATA_KEY, json);
//
//            editor.apply();
//
//            if (Util.DEBUG) Log.i(TAG, json);

    }

    /**
     * Item decorator for recycler view to equally space each item in the grid
     */
    private class FeedItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public FeedItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
            outRect.top = 0;
        }
    }

    /**
     * add new counter
     */
    @Override
    public void newCounterListener(Counter counter) {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.insertData(counter);
        }
    }
}
