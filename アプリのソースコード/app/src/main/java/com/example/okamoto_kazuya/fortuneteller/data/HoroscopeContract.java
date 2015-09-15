package com.example.okamoto_kazuya.fortuneteller.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by okamoto_kazuya on 15/09/09.
 */
public class HoroscopeContract {

    public static final String CONTENT_AUTHORITY = "com.example.okamoto_kazuya.fortuneteller";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_HOROSCOPE = "horoscope";

    public static final class HoroscopeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOROSCOPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOROSCOPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOROSCOPE;

        public static final String TABLE_NAME = "horoscope";

        public static final String COLUMN_SIGN = "sign";
        public static final String COLUMN_RANK = "rank";
        public static final String COLUMN_TOTAL = "total";
        public static final String COLUMN_MONEY = "money";
        public static final String COLUMN_JOB = "job";
        public static final String COLUMN_LOVE = "love";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildHoroscopeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}