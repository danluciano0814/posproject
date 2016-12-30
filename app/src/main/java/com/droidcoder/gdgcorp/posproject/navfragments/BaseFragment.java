package com.droidcoder.gdgcorp.posproject.navfragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.utils.Delayer;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public abstract class BaseFragment extends Fragment {

    int secondsDelayBeforeReady = 1;

    @Override
    public void onStart() {
        super.onStart();
        Delayer.delay(secondsDelayBeforeReady, new Delayer.DelayCallback() {
            @Override
            public void afterDelay() {
                onReady();
            }
        });
    }

    public void onReady()
    {
        Toast.makeText(getActivity(), "Fragment Attached", Toast.LENGTH_SHORT).show();
    }

}
