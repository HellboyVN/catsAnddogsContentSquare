package com.example.catsanddogs;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;

public class Model extends ViewModel {

    static String CAT = "cat";
    static String DOG = "dog";
    private String[] mPetList;
    private Context mContext;
    private MyAdapter myAdapter;

    public Model(Context context){
        mContext = context;
    }

    public MyAdapter populateAdapterData() {
        /*
         * Modify populateAdapterData() to retrieve available data from configuration changes
         * Make sure data to survive configuration changes such as screen rotations.
         * If myAdapter is available then reuse it, otherwise create a new one
         */
        if (myAdapter == null) {
            myAdapter = new MyAdapter(mContext, createPetList());
        }
        return myAdapter;
    }

    public String[] createPetList() {
        mPetList = new String[200];
        for(int i=0;i<mPetList.length;i++)
        {
            mPetList[i] = randomFill();
        }
        return mPetList;
    }

    private String randomFill(){
        Random random = new Random();
        boolean isOne = random.nextBoolean();
        if (isOne) return CAT;
        else return DOG;
    }

    // a factory class for Model
    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private Context context;

        public MyViewModelFactory(Context context) {
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new Model(context);
        }
    }
}
