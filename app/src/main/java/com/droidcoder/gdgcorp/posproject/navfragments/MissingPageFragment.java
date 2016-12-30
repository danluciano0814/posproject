package com.droidcoder.gdgcorp.posproject.navfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidcoder.gdgcorp.posproject.R;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public class MissingPageFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_404, container, false);
    }

}
