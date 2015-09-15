package com.example.okamoto_kazuya.fortuneteller.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by okamoto_kazuya on 15/09/09.
 */
public class HoroscopeProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private HoroscopeDBHelper mOpenHelper;

    static final int HOROSCOPE = 100;
    static final int HOROSCOPE_WITH_ID = 101;

    private static final SQLiteQueryBuilder sHoroscopeQueryBuilder;

    static{
        sHoroscopeQueryBuilder = new SQLiteQueryBuilder();
        sHoroscopeQueryBuilder.setTables(HoroscopeContract.HoroscopeEntry.TABLE_NAME);
    }

    private static final String sIdSelection = HoroscopeContract.HoroscopeEntry._ID + " = ? ";

    private Cursor getHoroscope(Uri uri, String[] projection, String sortOrder) {

        return sHoroscopeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAHoroscopeBySign(Uri uri, String[] projection, String sortOrder) {
        String idStr = HoroscopeContract.HoroscopeEntry.getIdSettingFromUri(uri);

        return sHoroscopeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sIdSelection,
                new String[]{idStr},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HoroscopeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, HoroscopeContract.PATH_HOROSCOPE, HOROSCOPE);
        // SIGNは文字列なので * でマッチング
        matcher.addURI(authority, HoroscopeContract.PATH_HOROSCOPE + "/*", HOROSCOPE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new HoroscopeDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case HOROSCOPE:
                return HoroscopeContract.HoroscopeEntry.CONTENT_TYPE;
            case HOROSCOPE_WITH_ID:
                return HoroscopeContract.HoroscopeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case HOROSCOPE_WITH_ID: {
                retCursor = getAHoroscopeBySign(uri, projection, sortOrder);
                break;
            }
            case HOROSCOPE: {
                retCursor = getHoroscope(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HOROSCOPE:
                db.beginTransaction();
                // Horoscopeテーブルのインサートは全消全入のみ対応
                db.execSQL("DELETE FROM " + HoroscopeContract.HoroscopeEntry.TABLE_NAME);
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(HoroscopeContract.HoroscopeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}