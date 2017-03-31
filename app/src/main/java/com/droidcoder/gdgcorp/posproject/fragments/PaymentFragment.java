package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/25/2017.
 */

public class PaymentFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.btn0)Button btn0;
    @BindView(R.id.btn1)Button btn1;
    @BindView(R.id.btn2)Button btn2;
    @BindView(R.id.btn3)Button btn3;
    @BindView(R.id.btn4)Button btn4;
    @BindView(R.id.btn5)Button btn5;
    @BindView(R.id.btn6)Button btn6;
    @BindView(R.id.btn7)Button btn7;
    @BindView(R.id.btn8)Button btn8;
    @BindView(R.id.btn9)Button btn9;
    @BindView(R.id.btnEditable1)Button btnEditable1;
    @BindView(R.id.btnEditable2)Button btnEditable2;
    @BindView(R.id.btnEditable3)Button btnEditable3;
    @BindView(R.id.btnClear)Button btnClear;
    @BindView(R.id.btnDecimal)Button btnDecimal;
    @BindView(R.id.btnBack)Button btnBack;
    @BindView(R.id.btnHide)Button btnHide;
    @BindView(R.id.btnCash)Button btnCash;
    @BindView(R.id.btnCredit)Button btnCredit;
    @BindView(R.id.btnPoints)Button btnPoints;
    @BindView(R.id.btnPayment)Button btnPayment;

    @BindView(R.id.txtServiceCharge)TextView txtSC;
    @BindView(R.id.txtTender)TextView txtTender;
    @BindView(R.id.txtTotalBalance)TextView txtTotal;
    @BindView(R.id.txtChange)TextView txtChange;
    @BindView(R.id.txtPaymentType)TextView txtPaymentType;

    double sc;
    double total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnEditable1.setOnClickListener(this);
        btnEditable2.setOnClickListener(this);
        btnEditable3.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCash.setOnClickListener(this);
        btnCredit.setOnClickListener(this);
        btnPoints.setOnClickListener(this);

        String editable1 = StringConverter.doubleFormatter(Double.parseDouble(LFHelper.getLocalData(getActivity(), GlobalConstants.BUTTON_ONE_FILE)));
        String editable2 = StringConverter.doubleFormatter(Double.parseDouble(LFHelper.getLocalData(getActivity(), GlobalConstants.BUTTON_TWO_FILE)));
        String editable3 = StringConverter.doubleFormatter(Double.parseDouble(LFHelper.getLocalData(getActivity(), GlobalConstants.BUTTON_THREE_FILE)));
        btnEditable1.setText(editable1);
        btnEditable2.setText(editable2);
        btnEditable3.setText(editable3);

        if(!getArguments().isEmpty()){
            sc = (getArguments().getDouble("total" , 0) / 100) * getArguments().getDouble("serviceCharge");
            total = getArguments().getDouble("total", 0) + sc;

            txtSC.setText(StringConverter.doubleFormatter(sc));
            txtTotal.setText(StringConverter.doubleFormatter(total));
            txtChange.setText("0");
        }

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                        .remove(PaymentFragment.this)
                        .commit();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                double tender;
                double change = Double.parseDouble(txtChange.getText().toString());

                if(txtTender.getText().toString().equalsIgnoreCase("")){
                    tender = 0;
                }else{
                    tender = Double.parseDouble(txtTender.getText().toString());
                }

                if(txtPaymentType.getText().toString().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CASH)){
                    if(tender < total || change < 0){
                        isValid = false;
                        Toast.makeText(getActivity(), "Invalid payment, cash tender should be greater than total", Toast.LENGTH_LONG).show();
                    }
                }

                if(!txtPaymentType.getText().toString().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CASH) &&
                        ((SalesActivity)getActivity()).getCustomer() == null ){
                    isValid = false;
                    Toast.makeText(getActivity(), "Invalid payment type, customer must be registered to allow payment type for credit and points", Toast.LENGTH_LONG).show();
                }

                if(txtPaymentType.getText().toString().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS) && ((SalesActivity)getActivity()).getCustomer() != null){

                    if(total > ((SalesActivity)getActivity()).getCustomer().getPoints()){
                        isValid = false;
                        Toast.makeText(getActivity(), "Invalid payment, customer points is short by (" + ((total - sc) - ((SalesActivity)getActivity()).getCustomer().getPoints()) + ")", Toast.LENGTH_LONG).show();
                    }

                }

                if(isValid){
                    if(txtPaymentType.getText().toString().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_POINTS)){
                        ((SalesActivity)getActivity()).saveToSales(txtPaymentType.getText().toString(), total - sc, 0);
                    }else if(txtPaymentType.getText().toString().equalsIgnoreCase(GlobalConstants.PAYMENT_TYPE_CREDIT)){
                        ((SalesActivity)getActivity()).saveToSales(txtPaymentType.getText().toString(), 0, sc);
                    }else{
                        ((SalesActivity)getActivity()).saveToSales(txtPaymentType.getText().toString(), tender, sc);
                    }
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                            .remove(PaymentFragment.this)
                            .commit();
                }

            }
        });

        txtTender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double tender = 0;
                double total = 0;

                if(!txtTender.getText().toString().equalsIgnoreCase("")){

                    tender = Double.parseDouble(txtTender.getText().toString());
                    total = Double.parseDouble(txtTotal.getText().toString());

                    txtChange.setText(StringConverter.doubleFormatter(tender - total));

                }else{
                    txtChange.setText(0 + "");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn0:
                setTxtQuantity("0");
                break;
            case R.id.btn1:
                setTxtQuantity("1");
                break;
            case R.id.btn2:
                setTxtQuantity("2");
                break;
            case R.id.btn3:
                setTxtQuantity("3");
                break;
            case R.id.btn4:
                setTxtQuantity("4");
                break;
            case R.id.btn5:
                setTxtQuantity("5");
                break;
            case R.id.btn6:
                setTxtQuantity("6");
                break;
            case R.id.btn7:
                setTxtQuantity("7");
                break;
            case R.id.btn8:
                setTxtQuantity("8");
                break;
            case R.id.btn9:
                setTxtQuantity("9");
                break;
            case R.id.btnEditable1:
                addToTender(btnEditable1.getText().toString());
                break;
            case R.id.btnEditable2:
                addToTender(btnEditable2.getText().toString());
                break;
            case R.id.btnEditable3:
                addToTender(btnEditable3.getText().toString());
                break;
            case R.id.btnDecimal:
                if(!txtTender.getText().toString().contains(".")){
                    setTxtQuantity(".");
                }
                break;
            case R.id.btnClear:
                txtTender.setText("");
                break;
            case R.id.btnBack:
                if(txtTender.getText().toString().length() > 0){
                    txtTender.setText(txtTender.getText().toString().substring(0, txtTender.getText().toString().length() - 1));
                }
                break;
            case R.id.btnCash:
                txtPaymentType.setText(GlobalConstants.PAYMENT_TYPE_CASH);
                txtSC.setText(StringConverter.doubleFormatter(sc));
                txtTotal.setText(StringConverter.doubleFormatter(total));
                break;
            case R.id.btnCredit:
                txtPaymentType.setText(GlobalConstants.PAYMENT_TYPE_CREDIT);
                txtSC.setText(StringConverter.doubleFormatter(sc));
                txtTotal.setText(StringConverter.doubleFormatter(total));
                break;
            case R.id.btnPoints:
                txtPaymentType.setText(GlobalConstants.PAYMENT_TYPE_POINTS);
                txtSC.setText("0.00");
                txtTotal.setText(StringConverter.doubleFormatter(total - sc));
                break;

        }
    }
    public void addToTender(String value){
        double addValue = Double.parseDouble(value);
        String result = "";

        if(txtTender.getText().toString().equalsIgnoreCase("")){
            result = StringConverter.doubleFormatter(0 + addValue);
        }else{
            result = StringConverter.doubleFormatter(Double.parseDouble(txtTender.getText().toString()) + addValue);
        }

        txtTender.setText(result);
    }

    public void setTxtQuantity(String value){
        txtTender.setText(txtTender.getText() + value);
    }
}
