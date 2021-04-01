package com.example.catsanddogs.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class CsAnalytics {

    private Context context;
    private HashMap<String, SortedSet<Integer>> petMap;//petMap stores the position of each dog or cat like <dog> [1, 3, 4, 5, 6] and <cat> [0, 2, 7]
    static String CAT = "cat";
    static String DOG = "dog";
    static String TAG = CsAnalytics.class.getName();
    static final String fileName = "cat_and_dog.txt";
    DebouncedOnClickListener debouncedOnClickListener;
    FileUpdateReceiver fileUpdateReceiver;

    public CsAnalytics(@NonNull Context context) {
        this.context = context;
        petMap = new HashMap<>();
        //using SortedSet to make sure no duplicate value
        SortedSet<Integer> cats = new TreeSet<>();
        SortedSet<Integer> dogs = new TreeSet<>();
        petMap.put(CAT, cats);
        petMap.put(DOG, dogs);
        debouncedOnClickListener = new DebouncedOnClickListener(2000);
        //register receiver here but where to unregister
        fileUpdateReceiver = new FileUpdateReceiver();
        IntentFilter filter = new IntentFilter(FileUpdateReceiver.ACTION);
        context.registerReceiver(fileUpdateReceiver, filter);
    }

    public void trigger(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Don't forget the thread and debounce timer.
        //find which pet correspond to current position and count how many pet
        int catAmount = getIndex(petMap.get(CAT), position) + 1;
        int dogAmount = getIndex(petMap.get(DOG), position) + 1;
        String result = "";

        if (catAmount > 0 && dogAmount == 0) {
            result = "Position " + position + ". There are " + catAmount + " cats.";
        } else if (catAmount == 0 && dogAmount > 0){
            result = "Position " + position + ". There are " + dogAmount + " dogs.";
        }

        if (debouncedOnClickListener.onClickShowLog()) {
            //Displays the pop-up (Toast) with the dog or cat count and uses the debounce function of 2s.
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG, result);
            //write log to file
            boolean writeFile = writeStringAsFile(context, result, fileName);
            // read file and notify
            if (writeFile) {
                String textToSend = readFileAsString(context, fileName);
                Intent intent = new Intent(FileUpdateReceiver.ACTION);
                intent.putExtra(FileUpdateReceiver.ACTION_MESSAGE, textToSend);
                context.sendBroadcast(intent);
            }

        }

    }

    public void track(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Registers all the pets from each grid.
        if (holder.itemView.getContentDescription().equals(CAT)) {
            petMap.get(CAT).add(position);
        } else {
            petMap.get(DOG).add(position);
        }
    }

    public void clear() {
        //Clears the log file.
    }

    public int getIndex(SortedSet<Integer> set, int value) {
        int result = 0;
        for (int entry:set) {
            if (entry == value) return result;
            result++;
        }
        return -1;
    }

    public boolean writeStringAsFile(Context context, final String fileContents, String fileName) {

        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public static String readFileAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return stringBuilder.toString();
    }

}
