package com.udacity.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class DetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recyclerview_history)
    RecyclerView mRecyclerView;

    private HistoryAdapter mHistoryAdapter;

    private static final int DETAIL_LOADER = 0;
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_ABSOLUTE_CHANGE = "absolute_change";
    public static final String COLUMN_PERCENTAGE_CHANGE = "percentage_change";
    public static final String COLUMN_HISTORY = "history";
    public static final int POSITION_ID = 0;
    public static final int POSITION_SYMBOL = 1;
    public static final int POSITION_PRICE = 2;
    public static final int POSITION_ABSOLUTE_CHANGE = 3;
    public static final int POSITION_PERCENTAGE_CHANGE = 4;
    public static final int POSITION_HISTORY = 5;
    public static final String[] QUOTE_COLUMNS = {
        COLUMN_SYMBOL,
        COLUMN_PRICE,
        COLUMN_ABSOLUTE_CHANGE,
        COLUMN_PERCENTAGE_CHANGE,
        COLUMN_HISTORY
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        View emptyView = findViewById(R.id.recyclerview_history_empty);
        mRecyclerView.setHasFixedSize(true);
        mHistoryAdapter = new HistoryAdapter(this, emptyView);
        mRecyclerView.setAdapter(mHistoryAdapter);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        System.out.println(getIntent().getData());
        Uri uri = getIntent().getData();

        return new CursorLoader(this,
                uri,
                QUOTE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0 && data.moveToFirst()) {
            String str = data.getString(4);
            String[] rows = str.split("\n");
            ArrayList<Pair<String, String>> lista = new ArrayList<>();
            for (String row : rows) {
                String date = row.split(",")[0];
                String price = row.split(",")[0];
                lista.add(new Pair<String, String>(date, price));
            }
            mHistoryAdapter.swapElements(lista);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mHistoryAdapter.swapElements(null);
    }
}
