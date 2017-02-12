package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by DanLuciano on 2/6/2017.
 */

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

    }
}
