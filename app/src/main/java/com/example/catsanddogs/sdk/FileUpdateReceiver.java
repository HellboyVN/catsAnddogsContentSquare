package com.example.catsanddogs.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUpdateReceiver extends BroadcastReceiver {

    static final String ACTION = "com.example.catsanddogs.FILE_UPDATED";
    static final String ACTION_MESSAGE = "com.example.catsanddogs.EXTRA_TEXT";
    private final static String TAG = FileUpdateReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            String fileName = intent.getStringExtra(ACTION_MESSAGE);
            //get file name and read file content
            String result = readFileAsString(context, fileName);
            if (!result.equals(""))
                Toast.makeText(context, "Read File OK, content = \n" + result, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Read File Failed, content = \n" + result, Toast.LENGTH_SHORT).show();
        }
        //unregister receiver, it is easier to register and unregister in MainActivity
        context.unregisterReceiver(this);
    }

    public String readFileAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: No such file or directory.");
        } catch (IOException e) {
            Log.e(TAG, "Error: Read file failed.");
        }

        return stringBuilder.toString();
    }
}
