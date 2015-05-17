package com.prancingpony.speedread.app.data;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by SaitSami on 17.5.2015.
 */
public class DocumentsDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "db_speedread.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DOCUMENTS = "table_documents";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "s_timestamp";
    public static final String COLUMN_URI = "s_uri";
    public static final String COLUMN_TEXT = "s_text";
    public static final String COLUMN_LOCATION = "i_location";
    public static final String COLUMN_TOTAL = "i_total";

    public DocumentsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
