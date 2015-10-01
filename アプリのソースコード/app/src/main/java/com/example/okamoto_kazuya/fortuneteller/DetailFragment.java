package com.example.okamoto_kazuya.fortuneteller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.okamoto_kazuya.fortuneteller.Views.MyCustomView;
import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;

    private static final String[] FORTUNE_COLUMNS = {
            HoroscopeContract.HoroscopeEntry.TABLE_NAME + "." + HoroscopeContract.HoroscopeEntry._ID,
            HoroscopeContract.HoroscopeEntry.COLUMN_SIGN,
            HoroscopeContract.HoroscopeEntry.COLUMN_RANK,
            HoroscopeContract.HoroscopeEntry.COLUMN_TOTAL,
            HoroscopeContract.HoroscopeEntry.COLUMN_MONEY,
            HoroscopeContract.HoroscopeEntry.COLUMN_JOB,
            HoroscopeContract.HoroscopeEntry.COLUMN_LOVE,
            HoroscopeContract.HoroscopeEntry.COLUMN_COLOR,
            HoroscopeContract.HoroscopeEntry.COLUMN_ITEM,
            HoroscopeContract.HoroscopeEntry.COLUMN_CONTENT
    };

    static final int COL_HOROSCOPE_ID = 0;
    static final int COL_HOROSCOPE_SIGN = 1;
    static final int COL_HOROSCOPE_RANK = 2;
    static final int COL_HOROSCOPE_TOTAL = 3;
    static final int COL_HOROSCOPE_MONEY = 4;
    static final int COL_HOROSCOPE_JOB = 5;
    static final int COL_HOROSCOPE_LOVE = 6;
    static final int COL_HOROSCOPE_COLOR = 7;
    static final int COL_HOROSCOPE_ITEM = 8;
    static final int COL_HOROSCOPE_CONTENT = 9;

    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private String mShareMessage;
    private String mSignKey;
    private ShareActionProvider mShareActionProvider;
    private Boolean finishedCursorLoad = false;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    FORTUNE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        View view = getView();

        String sign = data.getString(DetailFragment.COL_HOROSCOPE_SIGN);
        TextView signView = (TextView) view.findViewById(R.id.list_item_sign_textview);
        signView.setText(sign);

        int rank = data.getInt(DetailFragment.COL_HOROSCOPE_RANK);
        TextView rankView = (TextView) view.findViewById(R.id.list_item_rank_textview);
        rankView.setText("順位　：" + rank);

        int total = data.getInt(DetailFragment.COL_HOROSCOPE_TOTAL);
        TextView totalView = (TextView) view.findViewById(R.id.list_item_total_textview);
        totalView.setText("全体運：" + total);

        int money = data.getInt(DetailFragment.COL_HOROSCOPE_MONEY);
        TextView moneyView = (TextView) view.findViewById(R.id.list_item_money_textview);
        moneyView.setText("金運　：" + money);

        int job = data.getInt(DetailFragment.COL_HOROSCOPE_JOB);
        TextView jobView = (TextView) view.findViewById(R.id.list_item_job_textview);
        jobView.setText("仕事運：" + job);

        int love = data.getInt(DetailFragment.COL_HOROSCOPE_LOVE);
        TextView loveView = (TextView) view.findViewById(R.id.list_item_love_textview);
        loveView.setText("恋愛運：" + love);

        String color = data.getString(DetailFragment.COL_HOROSCOPE_COLOR);
        TextView colorView = (TextView) view.findViewById(R.id.list_item_color_textview);
        colorView.setText("ラッキーカラー：" + color);

        String item = data.getString(DetailFragment.COL_HOROSCOPE_ITEM);
        TextView itemView = (TextView) view.findViewById(R.id.list_item_item_textview);
        itemView.setText("ラッキーアイテム：" + item);

        String content = data.getString(DetailFragment.COL_HOROSCOPE_CONTENT);
        TextView contentView = (TextView) view.findViewById(R.id.list_item_content_textview);
        contentView.setText(content);

        MyCustomView myCustomView = (MyCustomView)view.findViewById(R.id.list_item_myCustomView);
        myCustomView.setData(money, job, love);
        myCustomView.setVisibility(View.VISIBLE);

        mShareMessage = sign + " ラッキーアイテム：" + item;
        mSignKey =  Helpers.getSignKey(sign);
        finishedCursorLoad = true;

        setShareIntent();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        finishedCursorLoad = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fortunefragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent();
    }

    private void setShareIntent() {
        if (finishedCursorLoad && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");
        Uri imageUri = getShareImageUri();
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareMessage);
        shareIntent.putExtra("sms_body", mShareMessage);
        return shareIntent;
    }

    private Uri getShareImageUri(){

        int imageId = getContext().getResources().getIdentifier(mSignKey, "drawable", this.getContext().getPackageName());
        Bitmap image = BitmapFactory.decodeResource(getContext().getResources(), imageId);

        // 画像を一度SDに保存する というコメントを参考にする。
        String fileFullPath = getContext().getExternalFilesDir(null) + File.separator + mSignKey + ".jpg";

        BufferedOutputStream boStream = null;
        Bitmap imageOnSDK = null;
        try {
            boStream = new BufferedOutputStream(new FileOutputStream(fileFullPath));
            imageOnSDK = image.copy(Bitmap.Config.ARGB_8888, true);
            if (!imageOnSDK.compress(Bitmap.CompressFormat.JPEG, 100, boStream)) {
            }
        } catch (Exception e) {
        } finally {
            if (imageOnSDK != null) {
                imageOnSDK.recycle();
                imageOnSDK = null;
            }
            try {
                boStream.close();
            } catch (Exception e) {

            }
        }
        return Uri.fromFile(new File(fileFullPath));
    }
}
