package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/6/2017.
 */

public class CancelAllFragment extends BaseDialogFragment{

    @BindView(R.id.btnOk)Button btnOk;
    @BindView(R.id.btnNo)Button btnNo;
    @BindView(R.id.txtTitle)TextView txtTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancel_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        if(!getArguments().isEmpty()){

            if(getArguments().getLong("id") > 0){
                txtTitle.setText("Warning! you are about to delete an order that was retrieve from on hold," +
                        " are you sure you want to cancel the order?");
            }

        }

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SalesActivity)getActivity()).cancelAll(getArguments().getLong("id"));
                dismiss();
            }
        });

    }


}
