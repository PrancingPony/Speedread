package com.prancingpony.speedread.app.data.accesor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.prancingpony.speedread.app.data.DocumentsDB;
import com.prancingpony.speedread.app.data.model.DocumentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SaitSami on 17.5.2015.
 */
public class DocumentAccessor {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_TAG = "DocumentAccessor";

    private SQLiteDatabase database;
    private DocumentsDB dbHelper;

    private String[] allColumns = {DocumentsDB.COLUMN_ID,
            DocumentsDB.COLUMN_TIMESTAMP,
            DocumentsDB.COLUMN_URI,
            DocumentsDB.COLUMN_TEXT,
            DocumentsDB.COLUMN_LOCATION,
            DocumentsDB.COLUMN_TOTAL};

    public DocumentAccessor(Context context) {
        dbHelper = new DocumentsDB(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DocumentModel insertDocument(String uri, String text, int location, int total) {
        ContentValues values = new ContentValues();
        values.put(DocumentsDB.COLUMN_TIMESTAMP, getTimeStamp());
        values.put(DocumentsDB.COLUMN_URI, uri);
        values.put(DocumentsDB.COLUMN_TEXT, text);
        values.put(DocumentsDB.COLUMN_LOCATION, location);
        values.put(DocumentsDB.COLUMN_TOTAL, total);
        long insertId = database.insert(DocumentsDB.TABLE_DOCUMENTS, null, values);
        Cursor cursor = database.query(DocumentsDB.TABLE_DOCUMENTS,
                allColumns, DocumentsDB.COLUMN_ID + " = " + insertId, null, null, null, null);
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
