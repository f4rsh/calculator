package com.sepidsa.fortytwocalculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sepidsa.fortytwocalculator.data.CurrencyContract;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by Farshid on 5/20/2015.
 */
public class CurrencyUseAdapter extends CursorAdapter {

    private Context mContext;
    private HashMap<String, String> currencyMap;


    public CurrencyUseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
        currencyMap = new HashMap<>(14);
        currencyMap.put("dollar", context.getString(R.string.farsi_dollar));
        currencyMap.put("eur", context.getString(R.string.farsi_eur));
        currencyMap.put("gbp", context.getString(R.string.farsi_gbp));
        currencyMap.put("try", context.getString(R.string.farsi_try));
        currencyMap.put("aed", context.getString(R.string.farsi_aed));
        currencyMap.put("cad", context.getString(R.string.farsi_cad));
        currencyMap.put("cny", context.getString(R.string.farsi_cny));
        currencyMap.put("dkk", context.getString(R.string.farsi_dkk));
        currencyMap.put("hkd", context.getString(R.string.farsi_hkd));
        currencyMap.put("myr", context.getString(R.string.farsi_myr));
        currencyMap.put("nok", context.getString(R.string.farsi_nok));
        currencyMap.put("pkr", context.getString(R.string.farsi_pkr));
        currencyMap.put("rub", context.getString(R.string.farsi_rub));
        currencyMap.put("sar", context.getString(R.string.farsi_sar));

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_use_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.position = cursor.getInt((cursor.getColumnIndex(CurrencyContract.CurrencyEntry._ID)));
        String key = cursor.getString(cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_KEY));
        String number = cursor.getString(cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_VALUE));


        viewHolder.nameView.setText(currencyMap.get(key));
        viewHolder.numberView.setText(number);

    }



    public static class ViewHolder {
        public final TextView nameView;
        public final TextView numberView;
        public int position;

        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.currency_translation);
            numberView = (TextView) view.findViewById(R.id.currency_value);
        }
    }


}
