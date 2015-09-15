package com.example.okamoto_kazuya.fortuneteller.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by okamoto_kazuya on 15/09/11.
 */
public class FortuneSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FortuneSyncAdapter sFortuneSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sFortuneSyncAdapter == null) {
                sFortuneSyncAdapter = new FortuneSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sFortuneSyncAdapter.getSyncAdapterBinder();
    }
}
