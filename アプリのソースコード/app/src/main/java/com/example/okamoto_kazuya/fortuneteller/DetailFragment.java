package com.example.okamoto_kazuya.fortuneteller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;

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

        mShareMessage = sign + " ラッキーアイテム：" + item;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fortunefragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareMessage);
        return shareIntent;
    }
}
