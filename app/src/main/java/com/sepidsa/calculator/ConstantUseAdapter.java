package com.sepidsa.calculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sepidsa.calculator.data.ConstantContract;

/**
 * Created by Farshid on 5/20/2015.
 */
public class ConstantUseAdapter extends CursorAdapter {

    private Context mContext;



    public ConstantUseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.constant_use_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.position = cursor.getInt((cursor.getColumnIndex(ConstantContract.ConstantEntry._ID)));
        String name = cursor.getString(cursor.getColumnIndex(ConstantContract.ConstantEntry.COLUMN_NAME));
        Double number = cursor.getDouble(cursor.getColumnIndex(ConstantContract.ConstantEntry.COLUMN_NUMBER));


        viewHolder.nameView.setText(name);
        viewHolder.numberView.setText(Double.toString(number));

    }



    public static class ViewHolder {
        public final TextView nameView;
        public final TextView numberView;
        public int position;

        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.constant_name);
            numberView = (TextView) view.findViewById(R.id.constant_number);
        }
    }


//    private CompoundButton.OnClickListener mStarOnClickListener = new CompoundButton.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            View parent = findParentRecursively(view);
//            if (parent != null) {
//                ViewHolder viewHolder = (ViewHolder) parent.getTag();
//                final int position = viewHolder.position;
//
//                String selection = LogContract.LogEntry._ID + "=?";
//                String[] selectionArgs = new String[]{String.valueOf(position)};
//                Uri uri = LogContract.LogEntry.CONTENT_URI;
//
//                Cursor cursor = mContext.getContentResolver().query(
//                        uri,
//                        null,
//                        selection,
//                        selectionArgs,
//                        null
//                );
//                if (cursor.moveToFirst()) {
//                    cursor.moveToFirst();
//                    int isCheckedInteger = cursor.getInt(cursor.getColumnIndex(LogContract.LogEntry.COLUMN_STARRED));
//                    isCheckedInteger = 1 - isCheckedInteger;
//                    ContentValues values = new ContentValues();
//                    values.put(LogContract.LogEntry.COLUMN_STARRED, isCheckedInteger);
//                    mContext.getContentResolver().update(
//                            uri,
//                            values,
//                            selection,
//                            selectionArgs
//                    );
//
//                    showMessage(Integer.toString(position) + isCheckedInteger);
//
//
//                }
//            }
//        }
//    };
//
//    public View findParentRecursively(View view) {
//        if (view.getTag() != null) {
//            return view;
//        }
//        View parent = (View) view.getParent();
//        if (parent == null) {
//            return null;
//        }
//        return findParentRecursively(parent);
//    }


    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


}
