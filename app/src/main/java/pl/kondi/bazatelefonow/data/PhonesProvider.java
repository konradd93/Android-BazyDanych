package pl.kondi.bazatelefonow.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static pl.kondi.bazatelefonow.data.PhonesContract.CONTENT_AUTHORITY;
import static pl.kondi.bazatelefonow.data.PhonesContract.PATH_PHONES;

public class PhonesProvider extends ContentProvider {

    //constants for the operations
    private static final int PHONES = 1;
    private static final int PHONES_ID = 2;

    //urimatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer
    //All the code put here will execute the first time anything is called from this class
    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PHONES, PHONES);
        //single row of the table;  # - accepts any integer number
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_PHONES + "/#", PHONES_ID);
    }

    private DBHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case PHONES:
                cursor = db.query(PhonesContract.PhonesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case PHONES_ID:
                cursor = db.query(PhonesContract.PhonesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Query unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = uriMatcher.match(uri);
        switch (match) {
            case PHONES:
                return insertRecord(uri, contentValues, PhonesContract.PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("PhonesProvider", "Error insert " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PHONES:
                return deleteRecord(uri, selection, selectionArgs, PhonesContract.PhonesEntry.TABLE_NAME);
            case PHONES_ID:
                return deleteRecord(uri, selection, selectionArgs, PhonesContract.PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Delete unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("PhonesProvider", "Error delete " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match){
            case PHONES:
                return updateRecord(uri, values, selection, selectionArgs, PhonesContract.PhonesEntry.TABLE_NAME);
            case PHONES_ID:
                selection = PhonesContract.PhonesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri,values,selection,selectionArgs, PhonesContract.PhonesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("PhonesProvider", "Error update" + uri);
            return -1;
        }
        return id;
    }
}
