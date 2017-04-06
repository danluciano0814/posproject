package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.CustomerDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.navfragments.CustomerFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.LFHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/18/2017.
 */

public class CustomerFormFragment extends BaseDialogFragment {

    @BindView(R.id.editEmail)EditText editEmail;
    @BindView(R.id.editAddress)EditText editAddress;
    @BindView(R.id.editFirstName)EditText editFirstName;
    @BindView(R.id.editLastName)EditText editLastName;
    @BindView(R.id.txtCode)TextView txtCode;
    @BindView(R.id.txtPoints)TextView txtPoints;
    @BindView(R.id.txtTitle)TextView txtProductTitle;
    @BindView(R.id.btnOpen)Button btnOpen;
    @BindView(R.id.btnDelete)Button btnDelete;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.customerId)TextView txtCustomerId;
    @BindView(R.id.imgCustomer)ImageView imageCustomer;
    @BindView(R.id.cbxActive)CheckBox cbxActive;
    @BindView(R.id.customerFrame)LinearLayout customerFrame;
    @BindView(R.id.editContact)EditText editContact;
    @BindView(R.id.btnCode)Button btnCode;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        if(getActivity() instanceof SalesActivity && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.N )){
            customerFrame.setPadding(0, 20, 0, 0);
        }

        imageCustomer.setImageResource(R.drawable.noimage);

        txtProductTitle.setText("CREATE CUSTOMER");
        cbxActive.setChecked(true);

        if(!getArguments().isEmpty()){
            Customer customer = DBHelper.getDaoSession().getCustomerDao().load(Long.parseLong(getArguments().getString("customerId")));
            txtCustomerId.setText(customer.getId() + "");
            editFirstName.setText(customer.getFirstName());
            editLastName.setText(customer.getLastName());
            editAddress.setText(customer.getAddress());
            editEmail.setText(customer.getEmail());
            editContact.setText(customer.getContact());
            txtCode.setText(customer.getCode());
            txtPoints.setText(StringConverter.doubleFormatter(customer.getPoints()));
            txtProductTitle.setText("UPDATE PRODUCT");
            cbxActive.setChecked(customer.getDeleted() == null);
            imageCustomer.setImageBitmap(ImageConverter.bytesToBitmap(customer.getImage()));
            btnCode.setVisibility(View.VISIBLE);
        }

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
                imageCustomer.setImageResource(R.drawable.noimage);
            }
        });

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LFHelper.getLocalData(getActivity(), GlobalConstants.CUSTOMER_FEATURE).equals("0")){
                    CustomerQRCodeFragment customerQRCodeFragment = new CustomerQRCodeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("customerId", Long.parseLong(txtCustomerId.getText().toString()));
                    customerQRCodeFragment.setArguments(bundle);
                    customerQRCodeFragment.show(getActivity().getSupportFragmentManager(), "customerQRCode");
                }else{
                    Toast.makeText(getActivity(), "Customer Rewarding Feature was off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                boolean isSave = txtCustomerId.getText().toString().trim().equals("");
                double quantity;
                Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
                Matcher matcher = pattern.matcher(editEmail.getText().toString().trim());

                if(editFirstName.getText().toString().trim().equalsIgnoreCase("")){
                    editFirstName.setError("First name is required");
                    isValid = false;
                }

                if(editLastName.getText().toString().trim().equalsIgnoreCase("")){
                    editLastName.setError("Last name is required");
                    isValid = false;
                }

                if(editEmail.getText().toString().trim().equalsIgnoreCase("")){
                    editEmail.setError("Email is required");
                    isValid = false;
                }

                if(!matcher.matches()){
                    editEmail.setError("Please input a valid email");
                    isValid = false;
                }

                if(isValid){

                    if(isSave){

                        if(DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                                .where(CustomerDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase()))
                                .count() > 0){
                            editEmail.setError("Email already exist in database");
                            return;
                        }

                        bitmap = ((BitmapDrawable) imageCustomer.getDrawable()).getBitmap();
                        Customer customer = new Customer();
                        customer.setCreated(new Date());
                        customer.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                        customer.setLastName(editLastName.getText().toString().trim().toUpperCase());
                        customer.setAddress(editAddress.getText().toString().trim().toUpperCase());
                        customer.setEmail(editEmail.getText().toString().trim().toUpperCase());
                        customer.setContact(editContact.getText().toString().trim().toUpperCase());
                        customer.setCode("");
                        customer.setPoints(0);
                        customer.setImage(ImageConverter.bitmapToBytes(bitmap));
                        if(cbxActive.isChecked()){
                            customer.setDeleted(null);
                        }else{
                            customer.setDeleted(new Date());
                        }

                        long customerId = DBHelper.getDaoSession().getCustomerDao().insert(customer);

                        //add code on customer
                        Customer customerUpdate = DBHelper.getDaoSession().getCustomerDao().load(customerId);
                        customerUpdate.setCode(new Date().getTime() + "" + customerId);
                        DBHelper.getDaoSession().getCustomerDao().update(customerUpdate);

                        if(getActivity() instanceof NavigationActivity){
                            ((NavigationActivity)getActivity()).refreshList();
                        }

                        if(!LFHelper.getLocalData(getActivity(), GlobalConstants.CUSTOMER_FEATURE).equals("0")){
                            CustomerQRCodeFragment customerQRCodeFragment = new CustomerQRCodeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putLong("customerId", customerId);
                            customerQRCodeFragment.setArguments(bundle);
                            customerQRCodeFragment.show(getActivity().getSupportFragmentManager(), "customerQRCode");
                        }


                        Toast.makeText(getActivity(), "Customer has been saved", Toast.LENGTH_LONG).show();
                        dismiss();

                    }else{
                        //for editing

                        Customer customer = DBHelper.getDaoSession().getCustomerDao().load(Long.parseLong(txtCustomerId.getText().toString()));
                        Customer customer2 = null;
                        if(DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                                .where(CustomerDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase()))
                                .count() != 0){
                            customer2 = DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                                    .where(CustomerDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase())).list().get(0);
                        }

                        //check if product name exist and Id is not the same
                        if(customer2 != null){
                            if(customer.getId() != customer2.getId()){
                                editEmail.setError("Email already exist in database");
                                return;
                            }
                        }
                        customer.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                        customer.setLastName(editLastName.getText().toString().trim().toUpperCase());
                        customer.setAddress(editAddress.getText().toString().trim().toUpperCase());
                        customer.setEmail(editEmail.getText().toString().trim().toUpperCase());
                        customer.setContact(editContact.getText().toString().trim().toUpperCase());

                        bitmap = ((BitmapDrawable) imageCustomer.getDrawable()).getBitmap();
                        Customer customerEdit = DBHelper.getDaoSession().getCustomerDao().load(Long.parseLong(txtCustomerId.getText().toString().trim()));
                        customerEdit.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                        customerEdit.setLastName(editLastName.getText().toString().trim().toUpperCase());
                        customerEdit.setAddress(editAddress.getText().toString().trim().toUpperCase());
                        customerEdit.setEmail(editEmail.getText().toString().trim().toUpperCase());
                        customerEdit.setImage(ImageConverter.bitmapToBytes(bitmap));
                        if(cbxActive.isChecked()){
                            customerEdit.setDeleted(null);
                        }else{
                            customerEdit.setDeleted(new Date());
                        }
                        DBHelper.getDaoSession().getCustomerDao().update(customerEdit);
                        Toast.makeText(getActivity(), "Customer has been updated", Toast.LENGTH_SHORT).show();
                        //((ProductFormFragment.OnTransactionFinish)getActivity()).refresh();

                        if(getActivity() instanceof NavigationActivity){
                            ((NavigationActivity)getActivity()).refreshList();
                        }

                        dismiss();
                    }

                }

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
                imageCustomer.setImageBitmap(bitmap);
            } catch (IOException e) {
                //Toast toast = Toast.makeText(getActivity(),"Exception", Toast.LENGTH_LONG);
                //toast.show();
                e.printStackTrace();
            }
        }
    }

}
