package com.example.okamoto_kazuya.fortuneteller.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract.HoroscopeEntry;

/**
 * Created by okamoto_kazuya on 15/09/09.
 */
public class HoroscopeDBHelper  extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "horoscope.db";

    public HoroscopeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_HOROSCOPE_TABLE = "CREATE TABLE " + HoroscopeEntry.TABLE_NAME + " (" +

                HoroscopeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HoroscopeEntry.COLUMN_SIGN + " TEXT NOT NULL, " +
                HoroscopeEntry.COLUMN_RANK + " INTEGER NOT NULL, " +
                HoroscopeEntry.COLUMN_TOTAL + " INTEGER NOT NULL, " +
                HoroscopeEntry.COLUMN_MONEY + " INTEGER NOT NULL, " +
                HoroscopeEntry.COLUMN_JOB + " INTEGER NOT NULL," +
                HoroscopeEntry.COLUMN_LOVE + " INTEGER NOT NULL, " +
                HoroscopeEntry.COLUMN_COLOR + " TEXT NOT NULL, " +
                HoroscopeEntry.COLUMN_ITEM + " TEXT NOT NULL, " +
                HoroscopeEntry.COLUMN_CONTENT + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_HOROSCOPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
