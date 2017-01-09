package com.dyszlewskiR.edu.scientling.services.repetitions;

import android.provider.SyncStateContract;
import android.util.Pair;

import com.dyszlewskiR.edu.scientling.data.models.tableModels.Repetition;
import com.dyszlewskiR.edu.scientling.utils.Constants;

/**
 * Created by Razjelll on 09.01.2017.
 */

public class Interval {
    public enum State{
        START,
        MIDDLE,
        FINISH
    }

    private static final Pair<Integer, Integer>[] mIntervals = new Pair[]{
            new Pair(10, 0),
            new Pair(20, 1),
            new Pair(40, 2),
            new Pair(60, 7),
            new Pair(80, 30)
    };

    public static int getInterval(int masterLevel){
        for(int i = mIntervals.length-1; i>= 0; i--){
            if(mIntervals[i].first <= masterLevel){
                return mIntervals[i].second;
            }
        }
        return 0;
    }

    public static int getNextMasterLevel(int masterLevel){
        for(int i = mIntervals.length-1; i>= 0; i--){
            if(mIntervals[i].first <= masterLevel){
                if(i != mIntervals.length-1){
                    return mIntervals[i+1].first;
                } else {
                    return mIntervals[mIntervals.length-1].first;
                }
            }
        }
        return mIntervals[0].first;// zwracane jeśli masterLevel = 0
    }

    public static State getLearningState(int masterLevel){
        if(masterLevel == mIntervals[0].first){
            return State.START;
        } else if (masterLevel == mIntervals[mIntervals.length-1].first){
            return State.FINISH;
        } else {
            return State.MIDDLE;
        }
    }
}
