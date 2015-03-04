package com.example.goran.protestthor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Goran on 1.3.2015..
 */
public class DbUtil {

    public static SQLiteDatabase getWritableDatabase(Context context){
        ProtestDbHelper dbHelper = new ProtestDbHelper(context, ProtestDbHelper.DATABASE_NAME,
                null, ProtestDbHelper.DATABASE_VERSION);
        return dbHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context context){
        ProtestDbHelper dbHelper = new ProtestDbHelper(context, ProtestDbHelper.DATABASE_NAME,
                null, ProtestDbHelper.DATABASE_VERSION);
        return dbHelper.getReadableDatabase();
    }

}
