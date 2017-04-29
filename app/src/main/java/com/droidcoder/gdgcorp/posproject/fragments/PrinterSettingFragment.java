package com.droidcoder.gdgcorp.posproject.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.DeviceListActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.printer.BluetoothService;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/25/2017.
 */

public class PrinterSettingFragment extends BaseFragment {

    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnSearchPrinter)Button btnSearchPrinter;
    @BindView(R.id.switchPrinter)Switch switchPrinter;
    @BindView(R.id.txtPrinterName)TextView txtPrinterName;
    @BindView(R.id.radio58mm)RadioButton radio58mm;
    @BindView(R.id.cbxAddress)CheckBox cbxAddress;
    @BindView(R.id.cbxEmail)CheckBox cbxEmail;
    @BindView(R.id.cbxMobile)CheckBox cbxMobile;
    @BindView(R.id.cbxLandline)CheckBox cbxLandline;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int BLUETOOTH_REQUEST = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mService = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_setting, container, false);
        return view;
    }

    @SuppressLint("HandlerLeak")
    private final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case BluetoothService.STATE_CONNECTING:

                    break;

                case BluetoothService.STATE_CONNECTED:
                    Toast.makeText(getActivity(), "Printer Connected Successfully", Toast.LENGTH_SHORT).show();
                    //bluetoothService.write("connected\n".getBytes());
                    break;

                case BluetoothService.STATE_NONE:
                case BluetoothService.STATE_LISTEN:

                    break;
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mService = new BluetoothService(getActivity(), mHandler);

        if(mBluetoothAdapter != null){
            if(!mBluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BLUETOOTH_REQUEST);
            }
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_ENABLE).equalsIgnoreCase("0")){
            switchPrinter.setChecked(true);
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME).equalsIgnoreCase("0")){
            txtPrinterName.setText(getFromLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME));
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_ADDRESS).equalsIgnoreCase("0")){
            cbxAddress.setChecked(true);
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_EMAIL).equalsIgnoreCase("0")){
            cbxEmail.setChecked(true);
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_MOBILE).equalsIgnoreCase("0")){
            cbxMobile.setChecked(true);
        }

        if(!getFromLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_LANDLINE).equalsIgnoreCase("0")){
            cbxLandline.setChecked(true);
        }

        btnSearchPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                if(isValid){
                    if(switchPrinter.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_ENABLE, "true");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_ENABLE, "");
                    }

                    if(cbxAddress.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_ADDRESS, "true");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_ADDRESS, "");
                    }

                    if(cbxEmail.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_EMAIL, "true");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_EMAIL, "");
                    }

                    if(cbxMobile.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_MOBILE, "true");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_MOBILE, "");
                    }

                    if(cbxLandline.isChecked()){
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_LANDLINE, "true");
                    }else{
                        saveToLocalData(getActivity(), GlobalConstants.RECEIPT_SHOW_LANDLINE, "");
                    }

                    Toast.makeText(getActivity(), "Printer Setting was saved successfully", Toast.LENGTH_LONG).show();
                }

                String printerName = txtPrinterName.getText().toString().trim();
                saveToLocalData(getActivity(), GlobalConstants.BLUETOOTH_PRINTER_NAME, printerName);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BLUETOOTH_REQUEST){
            //bluetooth request
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getActivity(), "BlueTooth was Turned On", Toast.LENGTH_LONG).show();
            }
        }else{
            //on click of result
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras().getString(
                        DeviceListActivity.EXTRA_DEVICE_ADDRESS).substring(data.getExtras().getString(
                        DeviceListActivity.EXTRA_DEVICE_ADDRESS).length() - 17);
                // Get the BLuetoothDevice object
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    mService.connect(device);
                }

                txtPrinterName.setText(data.getExtras().getString(
                        DeviceListActivity.EXTRA_DEVICE_ADDRESS));
            }

        }


    }


}
