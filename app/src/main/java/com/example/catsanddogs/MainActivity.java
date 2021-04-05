package com.example.catsanddogs;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import com.example.catsanddogs.sdk.FileUpdateReceiver;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Model mActivityViewModel;
    private FileUpdateReceiver fileUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // mActivityViewModel = new Model(this);
        /*
         * Update the model to allow data to survive configuration changes such as screen rotations using ViewModel and ViewModelProvider
         * Another method is to add  android:configChanges="orientation|screenSize|screenLayout|keyboardHidden" to AndroidManifest.xml
         */
        mActivityViewModel =  new ViewModelProvider(this, new Model.MyViewModelFactory(this)).get(Model.class);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getGridLayoutManager());
        mRecyclerView.setAdapter(getAdapter());
    }

    @VisibleForTesting
    public MyAdapter getAdapter(){
        return mActivityViewModel.populateAdapterData();
    }

    @VisibleForTesting
    GridLayoutManager getGridLayoutManager(){
        return new GridLayoutManager(this, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //initial BroadcastReceiver to get notice when file is updated
        fileUpdateReceiver = new FileUpdateReceiver();
        IntentFilter filter = new IntentFilter(FileUpdateReceiver.ACTION);
        registerReceiver(fileUpdateReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregister the receiver when app stopped
        unregisterReceiver(fileUpdateReceiver);
    }
}
