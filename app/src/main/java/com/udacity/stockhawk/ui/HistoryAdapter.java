package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;


/**
 * Created by dlopezd on 21-02-2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder> {
    private ArrayList<Pair<String, String>> elements;
    final private Context mContext;
    private final DecimalFormat dollarFormat;
    final private View mEmptyView;

    public HistoryAdapter(Context context, View emptyView) {
        mContext = context;
        mEmptyView = emptyView;
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    }

    public class HistoryAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mDateView;
        public final TextView mPiceView;

        public HistoryAdapterViewHolder(View view) {
            super(view);
            mDateView = (TextView) view.findViewById(R.id.item_date);
            mPiceView = (TextView) view.findViewById(R.id.item_price);
        }
    }

    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if ( viewGroup instanceof RecyclerView ) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false);
            view.setFocusable(true);
            return new HistoryAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(HistoryAdapterViewHolder historyAdapterViewHolder, int position) {
        Pair<String,String> element = elements.get(position);
        String date = element.first;
        String price = element.second;

        // Get date from element

        historyAdapterViewHolder.mDateView.setText(getDate(Long.getLong(date),"dd/MM/yyyy"));
        //historyAdapterViewHolder.mDateView.setContentDescription(mContext.getString(R.string.a11y_high_temp, highString));

        // Read low temperature from cursor
        historyAdapterViewHolder.mPiceView.setText(dollarFormat.format(date));
        //historyAdapterViewHolder.mPiceView.setContentDescription(mContext.getString(R.string.a11y_high_temp, highString));
    }

    @Override
    public int getItemCount() {
        if ( null == elements ) return 0;
        return elements.size();
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void swapElements(ArrayList<Pair<String, String>> newElements) {
        elements = newElements;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
