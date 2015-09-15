package com.example.okamoto_kazuya.fortuneteller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;
import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract.HoroscopeEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by okamoto_kazuya on 15/09/10.
 */
public class FetchHoroscopeTask  extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchHoroscopeTask.class.getSimpleName();

    private Context mContext;

    public FetchHoroscopeTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String horoscopeJsonStr = null;
        String paramDayStr = params[0];

        try {
            URL url = new URL("http://api.jugemkey.jp/api/horoscope/free/" + paramDayStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            horoscopeJsonStr = buffer.toString();
            getHoroscopeDataFromJson(horoscopeJsonStr, paramDayStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private Void getHoroscopeDataFromJson(String horoscopeJsonStr, String dayStr)
            throws JSONException {

        final String OWM_TOP = "horoscope";

        final String OWN_SIGN = "sign";
        final String OWN_RANK  = "rank";
        final String OWN_TOTAL  = "total";
        final String OWN_MONEY  = "money";
        final String OWN_JOB = "job";
        final String OWN_LOVE  = "love";
        final String OWN_COLOR = "color";
        final String OWN_ITEM = "item";
        final String OWN_CONTENT  = "content";

        JSONObject horoscopeJson = new JSONObject(horoscopeJsonStr).getJSONObject(OWM_TOP);
        JSONArray aweakHoroscope = horoscopeJson.getJSONArray(dayStr);

        Vector<ContentValues> cVVector = new Vector<ContentValues>(aweakHoroscope.length());

        for(int i = 0; i < aweakHoroscope.length(); i++) {
            JSONObject aHoroscope = aweakHoroscope.getJSONObject(i);

            String sign = aHoroscope.getString(OWN_SIGN);
            int rank = aHoroscope.getInt(OWN_RANK);
            int total = aHoroscope.getInt(OWN_TOTAL);
            int money = aHoroscope.getInt(OWN_MONEY);
            int job = aHoroscope.getInt(OWN_JOB);
            int love = aHoroscope.getInt(OWN_LOVE);
            String color = aHoroscope.getString(OWN_COLOR);
            String item = aHoroscope.getString(OWN_ITEM);
            String content = aHoroscope.getString(OWN_CONTENT);

            ContentValues horoscopeValues = new ContentValues();

            horoscopeValues.put(HoroscopeEntry.COLUMN_SIGN , sign);
            horoscopeValues.put(HoroscopeEntry.COLUMN_RANK , rank);
            horoscopeValues.put(HoroscopeEntry.COLUMN_TOTAL , total);
            horoscopeValues.put(HoroscopeEntry.COLUMN_MONEY , money);
            horoscopeValues.put(HoroscopeEntry.COLUMN_JOB, job);
            horoscopeValues.put(HoroscopeEntry.COLUMN_LOVE , love);
            horoscopeValues.put(HoroscopeEntry.COLUMN_COLOR , color);
            horoscopeValues.put(HoroscopeEntry.COLUMN_ITEM , item);
            horoscopeValues.put(HoroscopeEntry.COLUMN_CONTENT , content);
            cVVector.add(horoscopeValues);
        }

        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(HoroscopeEntry.CONTENT_URI, cvArray);
        }
        return null;
    }
}
