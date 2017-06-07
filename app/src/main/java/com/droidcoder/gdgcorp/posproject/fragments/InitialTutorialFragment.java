package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.ViewSizeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 5/28/2017.
 */

public class InitialTutorialFragment extends BaseFragment {

    @BindView(R.id.main_frame)RelativeLayout mainFrame;
    @BindView(R.id.results)TextView result;

    int width;
    int height;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initial_tutorial, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(500);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1,1,0,1, Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,0);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setStartOffset(1700);
        scaleAnimation.setFillAfter(true);

        width = ViewSizeHelper.getScreenWidthInDPs(getActivity());
        height = ViewSizeHelper.getScreenHeightInDPs(getActivity());

        TextView txtSample = new TextView(getActivity());
        txtSample.setTextSize(18);
        txtSample.setText(" Download Manual or\nWatch Video Tutorials");
        txtSample.measure(0,0);

        txtSample.setBackground(getActivity().getResources().getDrawable(R.drawable.button_bg));
        //set x computes via pixel, getMeasuredWidth returns pixels
        txtSample.setX(ViewSizeHelper.dpToPx(getActivity(), width) - (txtSample.getMeasuredWidth() + ViewSizeHelper.dpToPx(getActivity(), 98)));
        if(Build.VERSION.SDK_INT < 21){
            txtSample.setY(ViewSizeHelper.dpToPx(getActivity(), height) - (ViewSizeHelper.dpToPx(getActivity(), height) - (ViewSizeHelper.dpToPx(getActivity(), 55))));
        } else {
            txtSample.setY(ViewSizeHelper.dpToPx(getActivity(), height) - (ViewSizeHelper.dpToPx(getActivity(), height) - (ViewSizeHelper.dpToPx(getActivity(), 75))));
        }
        txtSample.setBackground(getResources().getDrawable(R.drawable.smallmessage));
        txtSample.setGravity(Gravity.CENTER);

        mainFrame.addView(txtSample);
        mainFrame.startAnimation(alphaAnimation);
        txtSample.startAnimation(scaleAnimation);

        mainFrame.setEnabled(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainFrame.setEnabled(true);
            }
        }, 2000); // afterDelay will be executed after (secs*1000) milliseconds.

        mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NavigationActivity)getActivity()).removeInitialTutorial();
                LFHelper.saveLocalData(getActivity(), GlobalConstants.INITIAL_INSTALL, "initialized");

            }
        });





    }




}
