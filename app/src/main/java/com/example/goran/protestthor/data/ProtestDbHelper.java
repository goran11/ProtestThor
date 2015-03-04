package com.example.goran.protestthor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.goran.protestthor.data.ProtestorContract.ProtestEntry;
import com.example.goran.protestthor.data.ProtestorContract.ParoleEntry;
/**
 * Created by Goran on 1.3.2015..
 */
public class ProtestDbHelper  extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "weather.db";
    public final static int DATABASE_VERSION = 2;


    public ProtestDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ProtestDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "create table " + ProtestEntry.TABLE_NAME +
                " ( " +
                ProtestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProtestEntry.COLUMN_PROTEST_TITLE + " TEXT UNIQUE NOT NULL, " +
                ProtestEntry.COLUMN_PROTEST_DESCRIPTION + " TEXT, " +
                ProtestEntry.COLUMN_PROTEST_ACTIVE + " INTEGER " +
                ")";
        db.execSQL(sql);

        sql = "create table " + ParoleEntry.TABLE_NAME +
                "(" +
                ParoleEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ParoleEntry.COLUMN_PROTEST_ID + " INTEGER NOT NULL, " +
                ParoleEntry.COLUMN_PAROLE_TEXT + " TEXT NOT NULL, " +
                "foreign key (" + ParoleEntry.COLUMN_PROTEST_ID + ") references " +
                ProtestEntry.TABLE_NAME + "( " + ProtestEntry._ID + ")" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + ParoleEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + ProtestEntry.TABLE_NAME);
        onCreate(db);
    }
}
