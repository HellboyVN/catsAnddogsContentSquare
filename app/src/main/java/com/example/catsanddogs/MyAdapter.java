package com.example.catsanddogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.catsanddogs.sdk.CsAnalytics;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PetViewHolder> {

    private Context mContext;
    private String[] mPetList;
    private CsAnalytics mCsAnalytics;

    public MyAdapter(@NonNull Context context, @NonNull String[] petList) {
        mContext = context;
        mPetList = petList;
        mCsAnalytics = new CsAnalytics(context);
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_layout, parent, false);
        mCsAnalytics.clear();
        return new PetViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PetViewHolder holder, final int position) {
        if (mPetList[position] == Model.CAT) {
            holder.mPet.setImageResource(R.drawable.cat);
            holder.mPet.setContentDescription(Model.CAT);
        } else {
            holder.mPet.setImageResource(R.drawable.dog);
            holder.mPet.setContentDescription(Model.DOG);
        }
        mCsAnalytics.track(holder, position);
        holder.mPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCsAnalytics.trigger(holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPetList.length;
    }

    @VisibleForTesting
    public String[] getmPetList() {
        return mPetList;
    }

    @VisibleForTesting
    public CsAnalytics getCsAnalytics() {
        return mCsAnalytics;
    }

    class PetViewHolder extends RecyclerView.ViewHolder {

        ImageView mPet;

        public PetViewHolder(View itemView) {
            super(itemView);
            mPet = itemView.findViewById(R.id.pet);
        }

    }

}
