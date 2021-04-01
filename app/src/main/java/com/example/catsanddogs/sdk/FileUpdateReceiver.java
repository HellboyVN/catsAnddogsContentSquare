package com.example.catsanddogs.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class FileUpdateReceiver extends BroadcastReceiver {

    static final String ACTION = "com.example.catsanddogs.FILE_UPDATED";
    static final String ACTION_MESSAGE = "com.example.catsanddogs.EXTRA_TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            String receivedText = intent.getStringExtra(ACTION_MESSAGE);
            Toast.makeText(context, "Read File OK, content = \n" + receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}
