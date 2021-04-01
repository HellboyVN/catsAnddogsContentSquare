package com.example.catsanddogs.sdk;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Debounced OnClickListener
 * Rejects clicks that are too close together in time.
 * This class is safe to use as an OnClickListener for multiple views, and will debounce each one separately.
 */
public class DebouncedOnClickListener {

    private final long minimumInterval;
    private Long lastClickMap;
    static String TAG = DebouncedOnClickListener.class.getName();


    /**
     * The one and only constructor
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = 0L;
    }

    public boolean onClickShowLog() {
        long currentTimestamp = SystemClock.uptimeMillis();

        if(lastClickMap == 0L || (currentTimestamp - lastClickMap > minimumInterval)) {
            lastClickMap = currentTimestamp;
            return true;
        } else {
            Log.e(TAG, (minimumInterval - (currentTimestamp - lastClickMap)) + " ms remaining until the next log can be printed");
            return false;
        }
    }
}