package com.prancingpony.speedread.app.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.prancingpony.speedread.app.data.model.DocumentModel;
import com.prancingpony.speedread.app.data.provider.SRProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SaitSami on 17.5.2015.
 */
public class DataStore {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_TAG = "SRProvider";
    private final ContentResolver mResolver;

    public DataStore(Context context) {
        mResolver = context.getContentResolver();
    }

    public DocumentModel insertDocument(String uri, String text, int location, int total) {
        ContentValues values = new ContentValues();
        values.put(SRProvider.COLUMN_TIMESTAMP, getTimeStamp());
        values.put(SRProvider.COLUMN_URI, uri);
        values.put(SRProvider.COLUMN_TEXT, text);
        values.put(SRProvider.COLUMN_LOCATION, location);
        values.put(SRProvider.COLUMN_TOTAL, total);
        Uri insertUri = mResolver.insert(SRProvider.CONTENT_URI, values);
        Cursor cursor = mResolver.query(insertUri, null, null, null, null);
        cursor.moveToFirst();
        DocumentModel newDocument = cursorToDocument(cursor);
        cursor.close();
        return newDocument;
    }

    private DocumentModel cursorToDocument(Cursor cursor) {
        Date date = null;
        try {
            date = stringToDate(cursor.getString(1));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Date parse exception.", e);
        }
        return new DocumentModel(
                cursor.getInt(0),
                date,
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getInt(5));
    }

    private Date stringToDate(String date) throws ParseException {
        return getDateFormat().parse(date);
    }

    private String getTimeStamp() {
        Date date = new Date();
        return getDateFormat().format(date);
    }

    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
    }
}
