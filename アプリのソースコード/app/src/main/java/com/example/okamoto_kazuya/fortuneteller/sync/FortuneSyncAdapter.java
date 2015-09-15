package com.example.okamoto_kazuya.fortuneteller.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.example.okamoto_kazuya.fortuneteller.R;
import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;

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
import java.util.Vector;

/**
 * Created by okamoto_kazuya on 15/09/11.
 */
public class FortuneSyncAdapter extends AbstractThreadedSyncAdapter {

    public FortuneSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String horoscopeJsonStr = null;
        String targetDayStr = new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(System.currentTimeMillis()));

        try {
            URL url = new URL("http://api.jugemkey.jp/api/horoscope/free/" + targetDayStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return ;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return ;
            }
            horoscopeJsonStr = buffer.toString();
            getHoroscopeDataFromJson(horoscopeJsonStr, targetDayStr);
        } catch (IOException e) {
            return ;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

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

            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_SIGN , sign);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_RANK , rank);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_TOTAL , total);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_MONEY , money);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_JOB, job);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_LOVE , love);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_COLOR , color);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_ITEM , item);
            horoscopeValues.put(HoroscopeContract.HoroscopeEntry.COLUMN_CONTENT , content);
            cVVector.add(horoscopeValues);
        }

        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(HoroscopeContract.HoroscopeEntry.CONTENT_URI, cvArray);
        }
        return null;
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        }
        return newAccount;
    }
}

