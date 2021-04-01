package com.example.catsanddogs;

import android.content.Context;

import java.util.Random;

public class Model {

    static String CAT = "cat";
    static String DOG = "dog";
    private String[] mPetList;
    private Context mContext;

    public Model(Context context){
        mContext = context;
    }

    public MyAdapter populateAdapterData(){
        return new MyAdapter(mContext, createPetList());
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
}
