package com.example.okamoto_kazuya.fortuneteller;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okamoto_kazuya on 15/09/17.
 */
public class Helpers {

    public static HashMap<String, String> SIGN_DIC;
    public static void setSignDic(Context context){
        HashMap<String, String> signDic = new HashMap<String, String>();
        signDic.put(context.getString(R.string.pref_mySign_aries), "牡羊座");
        signDic.put(context.getString(R.string.pref_mySign_taurus), "牡牛座");
        signDic.put(context.getString(R.string.pref_mySign_gemini), "双子座");
        signDic.put(context.getString(R.string.pref_mySign_cancer), "蟹座");
        signDic.put(context.getString(R.string.pref_mySign_leo), "獅子座");
        signDic.put(context.getString(R.string.pref_mySign_virgo), "乙女座");
        signDic.put(context.getString(R.string.pref_mySign_libra), "天秤座");
        signDic.put(context.getString(R.string.pref_mySign_scorpio), "蠍座");
        signDic.put(context.getString(R.string.pref_mySign_sagittarius), "射手座");
        signDic.put(context.getString(R.string.pref_mySign_capricorn), "山羊座");
        signDic.put(context.getString(R.string.pref_mySign_aquarius), "水瓶座");
        signDic.put(context.getString(R.string.pref_mySign_pisces), "魚座");
        SIGN_DIC = signDic;
    }
    public static String getSignKey(String signValue){
        for(Map.Entry<String, String> e : SIGN_DIC.entrySet()) {
            if(signValue.equals(e.getValue())){
                return e.getKey();
            }
        }
        return null;
    }

}
