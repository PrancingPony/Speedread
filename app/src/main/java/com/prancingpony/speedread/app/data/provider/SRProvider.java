package com.prancingpony.speedread.app.data.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;

public class SRProvider extends ContentProvider {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_TAG = "SRProvider";

    private static final String DATABASE_NAME = "db_speedread.db";
    private static final int DATABASE_VERSION = 1;

    private static final String PROVIDER_NAME = "com.prancingpony.speedread.app.provider.documents";
    private static final String URL = "content://" + PROVIDER_NAME + "/table_documents";
    private static final int DOCUMENTS = 1;
    private static final int DOCUMENT_ID = 2;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String TABLE_DOCUMENTS = "table_documents";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "s_timestamp";
    public static final String COLUMN_URI = "s_uri";
    public static final String COLUMN_TEXT = "s_text";
    public static final String COLUMN_LOCATION = "i_location";
    public static final String COLUMN_TOTAL = "i_total";

    private static HashMap<String, String> DOCUMENTS_PROJECTION_MAP;

    private SQLiteDatabase db;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "table_documents", DOCUMENTS);
        uriMatcher.addURI(PROVIDER_NAME, "table_documents/#", DOCUMENT_ID);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)){
            case DOCUMENTS:
                count = db.delete(TABLE_DOCUMENTS, selection, selectionArgs);
                break;
            case DOCUMENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( TABLE_DOCUMENTS, COLUMN_ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case DOCUMENTS:
                return "vnd.android.cursor.dir/vnd.com.prancingpony.speedread.app.provider";
            case DOCUMENT_ID:
                return "vnd.android.cursor.item/vnd.com.prancingpony.speedread.app.provider";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_DOCUMENTS, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        Log.e(LOG_TAG, "Failed to add a record into " + uri);
        return uri;
    }

    @Override
    public boolean onCreate() {
        DocumentsDB mOpenHelper = new DocumentsDB(getContext());
        db = mOpenHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_DOCUMENTS);

        switch (uriMatcher.match(uri)) {
            case DOCUMENTS:
                qb.setProjectionMap(DOCUMENTS_PROJECTION_MAP);
                break;
            case DOCUMENT_ID:
                qb.appendWhere(COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = COLUMN_TIMESTAMP;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)){
            case DOCUMENTS:
                count = db.update(TABLE_DOCUMENTS, values,
                        selection, selectionArgs);
                break;
            case DOCUMENT_ID:
                count = db.update(TABLE_DOCUMENTS, values, COLUMN_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }



    public class DocumentsDB extends SQLiteAssetHelper {

        public DocumentsDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    }
}
