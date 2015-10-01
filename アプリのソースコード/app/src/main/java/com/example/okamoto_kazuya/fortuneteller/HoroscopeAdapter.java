package com.example.okamoto_kazuya.fortuneteller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.okamoto_kazuya.fortuneteller.data.HoroscopeContract;

import java.util.HashMap;

/**
 * Created by okamoto_kazuya on 15/09/10.
 */
public class HoroscopeAdapter extends CursorAdapter {

    private final Context mContext;

    public HoroscopeAdapter(final Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_fortune, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String sign = cursor.getString(FortuneFragment.COL_HOROSCOPE_SIGN);
        TextView signView = (TextView) view.findViewById(R.id.list_item_sign_textview);
        signView.setText(sign);

        int rank = cursor.getInt(FortuneFragment.COL_HOROSCOPE_RANK);
        TextView rankView = (TextView) view.findViewById(R.id.list_item_rank_textview);
        rankView.setText("順位　：" + rank);

        int total = cursor.getInt(FortuneFragment.COL_HOROSCOPE_TOTAL);
        TextView totalView = (TextView) view.findViewById(R.id.list_item_total_textview);
        totalView.setText("全体運：" + total);

        int money = cursor.getInt(FortuneFragment.COL_HOROSCOPE_MONEY);
        TextView moneyView = (TextView) view.findViewById(R.id.list_item_money_textview);
        moneyView.setText("金運　：" + money);

        int job = cursor.getInt(FortuneFragment.COL_HOROSCOPE_JOB);
        TextView jobView = (TextView) view.findViewById(R.id.list_item_job_textview);
        jobView.setText("仕事運：" + job);

        int love = cursor.getInt(FortuneFragment.COL_HOROSCOPE_LOVE);
        TextView loveView = (TextView) view.findViewById(R.id.list_item_love_textview);
        loveView.setText("恋愛運：" + love);

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        String mySignValue = sharedPrefs.getString(
                context.getString(R.string.pref_mySign_key),
                context.getString(R.string.pref_mySign_default));
        if(isMySign(mySignValue, sign)) {
            signView.setTextColor(Color.RED);
        } else {
            // rankViewのテキストカラー変える予定がないので、借りる。
            signView.setTextColor(rankView.getTextColors().getDefaultColor());
        }
    }

    private Boolean isMySign(String mySign, String sign){

        if(mySign == null || mySign.isEmpty()) { return false; }
        if(sign == null || sign.isEmpty()) { return false; }

        String mySignJP = Helpers.SIGN_DIC.get(mySign);

        return mySignJP.equals(sign);
    }
}
