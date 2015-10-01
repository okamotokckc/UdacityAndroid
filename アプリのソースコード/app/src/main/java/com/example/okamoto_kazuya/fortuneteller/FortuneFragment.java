package com.example.okamoto_kazuya.fortuneteller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;
import com.example.okamoto_kazuya.fortuneteller.sync.FortuneSyncAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class FortuneFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORTUNE_LOADER = 0;

    private static final String[] FORTUNE_COLUMNS = {
            HoroscopeContract.HoroscopeEntry.TABLE_NAME + "." + HoroscopeContract.HoroscopeEntry._ID,
            HoroscopeContract.HoroscopeEntry.COLUMN_SIGN,
            HoroscopeContract.HoroscopeEntry.COLUMN_RANK,
            HoroscopeContract.HoroscopeEntry.COLUMN_TOTAL,
            HoroscopeContract.HoroscopeEntry.COLUMN_MONEY,
            HoroscopeContract.HoroscopeEntry.COLUMN_JOB,
            HoroscopeContract.HoroscopeEntry.COLUMN_LOVE
    };

    static final int COL_HOROSCOPE_ID = 0;
    static final int COL_HOROSCOPE_SIGN = 1;
    static final int COL_HOROSCOPE_RANK = 2;
    static final int COL_HOROSCOPE_TOTAL = 3;
    static final int COL_HOROSCOPE_MONEY = 4;
    static final int COL_HOROSCOPE_JOB = 5;
    static final int COL_HOROSCOPE_LOVE = 6;

    private HoroscopeAdapter mHoroscopeAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public FortuneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fortunefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            updateHoroscope();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHoroscopeAdapter = new HoroscopeAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_fortune);
        mListView.setAdapter(mHoroscopeAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(
                                    HoroscopeContract
                                            .HoroscopeEntry
                                            .buildHoroscopeUri(cursor.getLong(COL_HOROSCOPE_ID))
                            );
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORTUNE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateHoroscope();
    }

    private void updateHoroscope() {
        FortuneSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri horoscopeUri = HoroscopeContract.HoroscopeEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                horoscopeUri,
                FORTUNE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mHoroscopeAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
            mListView.setItemChecked(mPosition, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mHoroscopeAdapter.swapCursor(null);
    }
}
