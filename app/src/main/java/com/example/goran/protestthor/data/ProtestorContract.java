package com.example.goran.protestthor.data;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;

/**
 * Created by Goran on 28.2.2015..
 */
public class ProtestorContract {
    private final static String LOG_TAG = ProtestorContract.class.getSimpleName();
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);


    public static class ProtestEntry implements BaseColumns {

        public static final String TABLE_NAME = "protest";

        public static final String COLUMN_PROTEST_TITLE = "protest_title";
        public static final String COLUMN_PROTEST_DESCRIPTION = "protest_description";
        public static final String COLUMN_PROTEST_ACTIVE = "protest_active";

    }

    public static class ParoleEntry implements BaseColumns  {

        public static final String TABLE_NAME = "parole";

        public  static final String COLUMN_PROTEST_ID = "protest_id";
        public static final String COLUMN_PAROLE_TEXT = "parole_text";
    }

}
