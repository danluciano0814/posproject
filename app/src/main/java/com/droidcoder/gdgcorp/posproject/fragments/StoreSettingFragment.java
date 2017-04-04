package com.droidcoder.gdgcorp.posproject.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 4/2/2017.
 */

public class StoreSettingFragment extends BaseFragment {

    @BindView(R.id.btnOpen)Button btnOpen;
    @BindView(R.id.btnDelete)Button btnDelete;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.imgStore)ImageView imageStore;
    @BindView(R.id.editName)EditText editName;
    @BindView(R.id.editAddress1)EditText editAddress1;
    @BindView(R.id.editAddress2)EditText editAddress2;
    @BindView(R.id.editEmail)EditText editEmail;
    @BindView(R.id.editEmailPassword)EditText editEmailPassword;
    @BindView(R.id.editMobile)EditText editMobile;
    @BindView(R.id.editLandline)EditText editLandline;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imageStore.setImageResource(R.drawable.noimage);

        if(LFHelper.getLocalByteData(getActivity(), GlobalConstants.STORE_LOGO_FILE) != null){
            byte[] imageByte = LFHelper.getLocalByteData(getActivity(), GlobalConstants.STORE_LOGO_FILE);
            imageStore.setImageBitmap(ImageConverter.bytesToBitmap(imageByte));
        }
        editName.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_NAME_FILE));
        editAddress1.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_ADDRESS1_FILE));
        editAddress2.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_ADDRESS2_FILE));
        editEmail.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_EMAIL_FILE));
        editEmailPassword.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_PASSWORD_FILE));
        editMobile.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_MOBILE_FILE));
        editLandline.setText(LFHelper.getLocalStringData(getActivity(), GlobalConstants.STORE_LANDLINE_FILE));

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageStore.setImageResource(R.drawable.noimage);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmap = ((BitmapDrawable) imageStore.getDrawable()).getBitmap();
                byte[] image = ImageConverter.bitmapToBytes(bitmap);

                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_NAME_FILE, editName.getText().toString().toUpperCase());
                LFHelper.saveLocalByteData(getActivity(), GlobalConstants.STORE_LOGO_FILE, image);
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_ADDRESS1_FILE, editAddress1.getText().toString().toUpperCase());
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_ADDRESS2_FILE, editAddress2.getText().toString().toUpperCase());
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_EMAIL_FILE, editEmail.getText().toString().toUpperCase());
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_PASSWORD_FILE, editEmailPassword.getText().toString().toUpperCase());
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_MOBILE_FILE, editMobile.getText().toString().toUpperCase());
                LFHelper.saveLocalData(getActivity(), GlobalConstants.STORE_LANDLINE_FILE, editLandline.getText().toString().toUpperCase());

                Toast.makeText(getActivity(), "Store settings was saved successfully", Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageStore.setImageBitmap(bitmap);
            } catch (IOException e) {
                //Toast toast = Toast.makeText(getActivity(),"Exception", Toast.LENGTH_LONG);
                //toast.show();
                e.printStackTrace();
            }
        }
    }


}
