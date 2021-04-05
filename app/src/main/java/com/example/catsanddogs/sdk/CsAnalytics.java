package com.example.catsanddogs.sdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class CsAnalytics {

    private Context context;
    //pet tracker stores the position of each dog or cat like <dog> [1, 3, 4, 5, 6] and <cat> [0, 2, 7]
    private HashMap<String, SortedSet<Integer>> petMap;
    private static String CAT = "cat";
    private static String DOG = "dog";
    private final static String TAG = CsAnalytics.class.getName();
    private static final String fileName = "cat_and_dog.txt";

    private final Runnable debouncedExec;
    private boolean isWaiting = false;
    private static final long DELAY_MILLIS = 2000L;

    public CsAnalytics(@NonNull Context context) {
        this.context = context;
        petMap = new HashMap<>();
        //Initial our pet tracker using SortedSet to make sure no duplicate value
        SortedSet<Integer> cats = new TreeSet<>();
        SortedSet<Integer> dogs = new TreeSet<>();
        petMap.put(CAT, cats);
        petMap.put(DOG, dogs);

        //our debounce
        this.debouncedExec = new DebouncedRunnable(
                new Runnable() {
                    @Override
                    public void run() {
                        //We can print log and access to file while isWaiting = true, otherwise it is forbidden.
                        isWaiting = true;
                    }
                },
                "CATS_AND_DOGS",
                DELAY_MILLIS
        );
    }

    public void trigger(@NonNull RecyclerView.ViewHolder holder, int position) {
        //find which pet correspond to current position and count how many pet that we have
        String result = "";
        if (holder.itemView.getContentDescription().equals(CAT)) {
            int catAmount = getIndex(petMap.get(CAT), position) + 1;
            result = "Position " + position + ". There are " + catAmount + " cats.";
        } else {
            int dogAmount = getIndex(petMap.get(DOG), position) + 1;
            result = "Position " + position + ". There are " + dogAmount + " dogs.";
        }

        debouncedExec.run();

        if (isWaiting) {
            //Displays the pop-up (Toast) with the dog or cat count and uses the debounce function of 2s.
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG, result);
            //write log to file
            boolean writeFile = writeStringAsFile(context, result, fileName);
            // read file and notify
            if (writeFile) {
                //notify the receiver that file is updated and send the file name to the receiver in order to read file
                Intent intent = new Intent(FileUpdateReceiver.ACTION);
                intent.putExtra(FileUpdateReceiver.ACTION_MESSAGE, fileName);
                context.sendBroadcast(intent);
            }
            isWaiting = false;
        }

    }

    public void track(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Registers all the pets from each grid to the pet tracker.
        if (holder.itemView.getContentDescription().equals(CAT)) {
            petMap.get(CAT).add(position);
        } else {
            petMap.get(DOG).add(position);
        }
    }

    public void clear() {
        //Clears the log file.
        File file = new File(context.getFilesDir(), fileName);
        if(file.exists())
            file.delete();
    }

    public int getIndex(SortedSet<Integer> set, int value) {
        // return the index of position on the set
        int result = 0;
        if (set != null && !set.isEmpty()) {
            for (int entry:set) {
                if (entry == value) return result;
                result++;
            }
        }
        return -1;
    }

    public synchronized boolean writeStringAsFile(Context context, final String fileContents, String fileName) {

        try {
            /*
             * Using a BufferedWriter is recommended for an expensive writer (such as FileWriter).
             * Using a PrintWriter gives access to println syntax.
             */
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(context.getFilesDir(), fileName), true)));
            out.println(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Error: Can not write to file.");
            return false;
        }
        return true;
    }

}
