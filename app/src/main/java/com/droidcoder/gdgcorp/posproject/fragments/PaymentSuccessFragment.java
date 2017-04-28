package com.droidcoder.gdgcorp.posproject.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.DeviceListActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.printer.BluetoothService;
import com.droidcoder.gdgcorp.posproject.utils.BluetoothPrinterHelper;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/18/2017.
 */

public class PaymentSuccessFragment extends BaseDialogFragment {

    @BindView(R.id.txtTotal)TextView txtTotal;
    @BindView(R.id.txtChange)TextView txtChange;
    @BindView(R.id.imageDone)ImageView imageDone;
    @BindView(R.id.txtEmail)TextView txtEmail;
    @BindView(R.id.btnEmail)Button btnEmail;
    @BindView(R.id.btnPrint)Button btnPrint;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mService = null;

    private static final int BLUETOOTH_REQUEST = 2;

    long orderReceiptId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_success, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mService = new BluetoothService(getActivity(), mHandler);

        txtChange.setText(StringConverter.doubleCommaFormatter(Double.parseDouble(getArguments().getString("change"))));
        txtTotal.setText(StringConverter.doubleCommaFormatter(Double.parseDouble(getArguments().getString("total"))));
        txtEmail.setText(getArguments().getString("email"));

        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper.getDaoSession().getOrderReceiptDao().load(orderReceiptId).refresh();
                OrderReceipt orderReceipt = DBHelper.getDaoSession().getOrderReceiptDao().load(orderReceiptId);

                BluetoothPrinterHelper.printReceipt(getActivity(), mService, true, orderReceipt);

            }
        });

    }

    public void setOrderReceiptId(long id){
        this.orderReceiptId = id;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_ENABLE).equalsIgnoreCase("0")){
            if(mBluetoothAdapter != null){
                if(mBluetoothAdapter.isEnabled()){
                    if(mService!=null){

                        if(!LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME).equalsIgnoreCase("0")){
                            String address = LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME);
                            address = address.substring(address.length() - 17);

                            if(mBluetoothAdapter.checkBluetoothAddress(address)){
                                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                                mService.connect(device);
                            }
                        }else{
                            Toast.makeText(getActivity(), "Please Set up the printer setting first", Toast.LENGTH_LONG).show();
                        }

                    }
                }else{
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, BLUETOOTH_REQUEST);
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mBluetoothAdapter!=null){
            if(mBluetoothAdapter.isEnabled()){
                if(mService!=null){
                    if(mService.getState()==BluetoothService.STATE_NONE){
                        mService.start();
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(mBluetoothAdapter!=null) {
            if (requestCode == BLUETOOTH_REQUEST) {
                if (resultCode == getActivity().RESULT_OK) {
                    if(!LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_ENABLE).equalsIgnoreCase("0")){
                        if (mService != null) {

                            if(!LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME).equalsIgnoreCase("0")){
                                String address = LFHelper.getLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME);
                                address = address.substring(address.length() - 17);

                                if(mBluetoothAdapter.checkBluetoothAddress(address)){
                                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                                    mService.connect(device);
                                }
                            }else{
                                Toast.makeText(getActivity(), "Please Set up the printer setting first", Toast.LENGTH_LONG).show();
                            }

                        }
                    }else{
                        Toast.makeText(getActivity(), "Printer was disabled", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(OrderMainActivity.this, "BlueTooth Turned On", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if(mService!=null){
            mService.stop();
        }
    }

    @SuppressLint("HandlerLeak")
    private final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case BluetoothService.STATE_CONNECTING:

                    break;

                case BluetoothService.STATE_CONNECTED:

                    break;

                case BluetoothService.STATE_NONE:
                case BluetoothService.STATE_LISTEN:

                    break;
            }
        }
    };

}
