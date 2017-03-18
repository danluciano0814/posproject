package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/6/2017.
 */

public class TaxAllFragment extends BaseDialogFragment {

    @BindView(R.id.btnOk)Button btnOk;
    @BindView(R.id.btnNo)Button btnNo;
    @BindView(R.id.cbxTaxExempt)CheckBox cbxTaxExempt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tax_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SalesActivity)getActivity()).taxExemptAll(cbxTaxExempt.isChecked());
                dismiss();
            }
        });

    }

}
