package com.droidcoder.gdgcorp.posproject.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.google.zxing.WriterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/2/2017.
 */

public class CustomerQRCodeFragment extends BaseDialogFragment {

    @BindView(R.id.btnCancel)Button btnCancel;
    @BindView(R.id.imageQRCode)ImageView imageQRCode;
    @BindView(R.id.txtEmail)TextView txtEmail;
    @BindView(R.id.btnEmail)Button btnEmail;

    private Bitmap bitmap;
    Customer customer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_qrcode, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        if(!getArguments().isEmpty()){
            customer = DBHelper.getDaoSession().getCustomerDao().load(getArguments().getLong("customerId"));
            try {
                bitmap = ImageConverter.encodeAsBitmap(getActivity(), customer.getCode());
                imageQRCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            txtEmail.setText(customer.getEmail());
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = saveImage(bitmap);

                if(isStoragePermissionGranted()){
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                            new String[]{customer.getEmail()});

                    StringBuilder body = new StringBuilder();
                    body.append("Dear " + customer.getFirstName() + " " + customer.getLastName() + ",");
                    body.append("\n\n Greetings from ");
                    if(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_NAME_FILE).equals("")){
                        body.append("Cheappos\n\n");
                    }else{
                        body.append(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_NAME_FILE) + "\n\n");
                    }

                    body.append("Thank you for joining our Loyalty Program." +
                            "\nPlease take a picture or save the QRCode image on your device." +
                            "\nThis will serve as your loyalty code, just present it to our merchant to collect and redeem points." +
                            "\nThanks and have a blessed day." +
                            "\n\n\nThis is an system generated email please do not reply.");

                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                            new String[]{customer.getEmail()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_NAME_FILE) + " Loyalty Code");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    emailIntent.setType("image/png");
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(emailIntent);

                }else{
                    Toast.makeText(getActivity(), "email sending fail", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public File saveImage(Bitmap bitmap){

        File file = null;

        try {
            File path = Environment.getExternalStoragePublicDirectory("/CheapposCSV" + "/ProductCSV");
            if(!path.exists()){
                path.mkdirs();
            }
            file = new File(path.getAbsolutePath(), "QRCode.png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            //Toast.makeText(getActivity(), fileName + " successfully Created", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(getActivity(), fileName + " Error", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getActivity(), fileName + " Error", Toast.LENGTH_LONG).show();
        }


        return file;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permision","Permission is granted");
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
