package com.example.catsanddogs.sdk;

import android.util.Log;

public class DebouncedRunnable implements Runnable {

    private final Runnable operation;
    private final String name;
    private final long delayMillis;

    // state
    private long lastRunTime = -1;

    private final static String TAG = DebouncedRunnable.class.getName();
    /*
    * This class implements debounce using thread safe synchronized.
    * When we make a request:
        * If it hasn't been made within the past delayMillis milliseconds, then the request gets made immediately
        * If there IS a recent call, then we drop the current call and next request is called after delayMillis milliseconds pass
    */
    public DebouncedRunnable(Runnable operation, String name, long delayMillis) {
        this.operation = operation;
        this.name = name;
        this.delayMillis = delayMillis;
    }

    public synchronized void run() {
        long currentTime = getCurrentTimeMillis();
        if (shouldRunNow(currentTime)) {
            // we've never called this before or called a long time ago, call it now
            lastRunTime = currentTime;
            Log.d(TAG,"calling " + name + " immediately");
            operation.run();
        } else {
            // we've called it recently, which suggests that we might have more of these incoming
            // any incoming calls will get ignored
            Log.d(TAG, "Task " + name + " to be called in " + (-currentTime + (lastRunTime + delayMillis)) + " ms");
        }
    }

    // Should run now if we've never run it before or we've run it more than `delayMillis` ms in the past
    private boolean shouldRunNow(long currentTime) {
        return lastRunTime == -1 || lastRunTime + delayMillis < currentTime;
    }

    long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

}
