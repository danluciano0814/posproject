package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/28/2017.
 */

public class SalesSettingFragment extends BaseFragment{

    @BindView(R.id.editTax)EditText editTax;
    @BindView(R.id.editSC)EditText editSC;
    @BindView(R.id.editButton1)EditText editBtn1;
    @BindView(R.id.editButton2)EditText editBtn2;
    @BindView(R.id.editButton3)EditText editBtn3;
    @BindView(R.id.btnSave)Button btnSave;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        editTax.setText(StringConverter.doubleFormatter(Double.parseDouble(getFromLocalData(getActivity(), GlobalConstants.TAX_FILE))));
        editSC.setText(StringConverter.doubleFormatter(Double.parseDouble(getFromLocalData(getActivity(), GlobalConstants.SC_FILE))));
        editBtn1.setText(getFromLocalData(getActivity(), GlobalConstants.BUTTON_ONE_FILE));
        editBtn2.setText(getFromLocalData(getActivity(), GlobalConstants.BUTTON_TWO_FILE));
        editBtn3.setText(getFromLocalData(getActivity(), GlobalConstants.BUTTON_THREE_FILE));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                if(Double.parseDouble(editTax.getText().toString().trim()) > 100){
                    editTax.setError("Tax limit is 100%");
                    isValid = false;
                }

                if(isValid){
                    saveToLocalData(getActivity(), GlobalConstants.TAX_FILE, editTax.getText().toString().trim());
                    saveToLocalData(getActivity(), GlobalConstants.SC_FILE, editSC.getText().toString().trim());
                    saveToLocalData(getActivity(), GlobalConstants.BUTTON_ONE_FILE, editBtn1.getText().toString().trim());
                    saveToLocalData(getActivity(), GlobalConstants.BUTTON_TWO_FILE, editBtn2.getText().toString().trim());
                    saveToLocalData(getActivity(), GlobalConstants.BUTTON_THREE_FILE, editBtn3.getText().toString().trim());
                    Toast.makeText(getActivity(), "Sale Setting was saved successfully", Toast.LENGTH_SHORT).show();
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



}
