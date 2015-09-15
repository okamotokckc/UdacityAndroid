package com.example.okamoto_kazuya.fortuneteller.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by okamoto_kazuya on 15/09/11.
 */
public class FortuneAuthenticatorService extends Service {
    private FortuneAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new FortuneAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}