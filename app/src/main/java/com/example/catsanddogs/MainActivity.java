package com.example.catsanddogs;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Model mActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivityViewModel = new Model(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getGridLayoutManager());
        mRecyclerView.setAdapter(getAdapter());
    }

    @VisibleForTesting
    MyAdapter getAdapter(){
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


}
