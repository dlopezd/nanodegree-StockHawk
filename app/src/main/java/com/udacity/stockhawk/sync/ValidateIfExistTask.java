package com.udacity.stockhawk.sync;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.ui.MainActivity;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by user on 03-03-2017.
 */

public class ValidateIfExistTask extends AsyncTask<String, Boolean, Boolean> {

    private final String LOG_TAG = ValidateIfExistTask.class.getSimpleName();
    private Activity context;
    private String symbol;

    public ValidateIfExistTask(Activity context, String symbol){
        this.context = context;
        this.symbol = symbol;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        // If yahoo get an error, is because the symbol doesn't exist.
        try {
            Stock stock = YahooFinance.get(symbol, false);
            if(stock.getName() == null){
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(!result) {
            Toast.makeText(context.getApplicationContext(), R.string.stock_not_exist, Toast.LENGTH_LONG).show();
        }
        else{

            ((SwipeRefreshLayout)(context.findViewById(R.id.swipe_refresh))).setRefreshing(true);
            PrefUtils.addStock(context, symbol);
            QuoteSyncJob.syncImmediately(context);
        }
    }
}
