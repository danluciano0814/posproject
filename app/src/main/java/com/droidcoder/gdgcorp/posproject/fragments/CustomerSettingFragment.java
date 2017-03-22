package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/20/2017.
 */

public class CustomerSettingFragment extends BaseFragment {

    @BindView(R.id.switchCustomer)Switch switchCustomer;
    @BindView(R.id.editPurchase)EditText editPurchase;
    @BindView(R.id.editPoints)EditText editPoints;
    @BindView(R.id.btnSave)Button btnSave;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if(getFromLocalData(getActivity(), GlobalConstants.CUSTOMER_FEATURE).equalsIgnoreCase("0")){
            switchCustomer.setChecked(false);
            dissableEditText(false);
        }else{
            switchCustomer.setChecked(true);
            dissableEditText(true);
        }
        editPurchase.setText(getFromLocalData(getActivity(), GlobalConstants.CUSTOMER_PURCHASE));
        editPoints.setText(getFromLocalData(getActivity(), GlobalConstants.CUSTOMER_PURCHASE_POINTS));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                if(isValid){
                    if(switchCustomer.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.CUSTOMER_FEATURE, "1");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.CUSTOMER_FEATURE, "0");
                    }
                    saveToLocalData(getActivity(), GlobalConstants.CUSTOMER_PURCHASE, editPurchase.getText().toString().trim());
                    saveToLocalData(getActivity(), GlobalConstants.CUSTOMER_PURCHASE_POINTS, editPoints.getText().toString().trim());
                    Toast.makeText(getActivity(), "Customer Setting was saved successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        switchCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dissableEditText(true);
                }else{
                    dissableEditText(false);
                }
            }
        });
    }

    public void saveToLocalData(Context context, String fileName, String value){

        String data = "";

        if(value.equalsIgnoreCase("")){
            data = "0";
        }else{
            data = value;
        }

        LFHelper.saveLocalData(context, fileName, data);
    }

    public String getFromLocalData(Context context, String fileName){
        String result = LFHelper.getLocalData(context, fileName);
        return result;
    }

    public void dissableEditText(boolean value){
        editPoints.setEnabled(value);
        editPurchase.setEnabled(value);
    }

}
