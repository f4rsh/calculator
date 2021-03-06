package com.sepidsa.fortytwocalculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sepidsa.fortytwocalculator.data.LogContract.LogEntry;

/**
 * Created by Farshid on 5/15/2015.
 */
public class LogDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "log.db";

    public LogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOG_TABLE = "CREATE TABLE " + LogContract.LogEntry.TABLE_NAME + " (" +
                LogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LogEntry.COLUMN_RESULT + " TEXT NOT NULL, " +
                LogEntry.COLUMN_RESULT_NO_COMMA + " TEXT NOT NULL, " +
                LogEntry.COLUMN_OPERATION + " TEXT NOT NULL, " +
                LogEntry.COLUMN_TAG + " TEXT NOT NULL, " +
                LogEntry.COLUMN_STARRED + " INTEGER NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);

    }
}
