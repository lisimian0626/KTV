package com.beidousat.karaoke.ui.fragment.topic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.BaseFragment;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmMoodNew extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fm_mood, null);
        return mRootView;
    }


}
