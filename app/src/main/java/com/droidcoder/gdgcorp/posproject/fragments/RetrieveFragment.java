
package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.roughike.bottombar.BottomBar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/9/2017.
 */

public class RetrieveFragment extends BaseDialogFragment {

    @BindView(R.id.fragmentContainer)FrameLayout container;
    @BindView(R.id.btnCancel)Button btnCancel;

    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrieve, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        fm = getChildFragmentManager();

        OnHoldReceiptFragment onHoldReceiptFragment = new OnHoldReceiptFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(container.getId(), onHoldReceiptFragment, "onHoldReceipt").commit();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
