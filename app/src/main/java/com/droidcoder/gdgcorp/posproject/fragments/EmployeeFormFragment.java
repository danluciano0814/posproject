package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRole;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.AsyncCheckEmail;
import com.droidcoder.gdgcorp.posproject.utils.ConnectionHelper;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/26/2017.
 * todo list
 * 1. user role spinner
 *
 */

public class EmployeeFormFragment extends BaseDialogFragment{

    @BindView(R.id.spnrUserRole)Spinner spnrUserRole;
    @BindView(R.id.editRemarks)EditText editRemarks;
    @BindView(R.id.editEmail)EditText editEmail;
    @BindView(R.id.editFirstName)EditText editFirstName;
    @BindView(R.id.editLastName)EditText editLastName;
    @BindView(R.id.txtTitle)TextView txtEmployeeTitle;
    @BindView(R.id.btnOpen)Button btnOpen;
    @BindView(R.id.btnDelete)Button btnDelete;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.employeeId)TextView txtEmployeeId;
    @BindView(R.id.imgEmployee)ImageView imageEmployee;
    @BindView(R.id.cbxActive)CheckBox cbxActive;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    SpinnerAdapter spinnerAdapter;
    long spnrUserRoleId = 0;
    String passwordCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        imageEmployee.setImageResource(R.drawable.noimage);

        txtEmployeeTitle.setText("CREATE EMPLOYEE");
        cbxActive.setChecked(true);

        //populate spinner category
        List<SpinnerItem> spinnerList = new ArrayList<>();
        SpinnerItem spinnerItem = new SpinnerItem();
        spinnerItem.setId(0);
        spinnerItem.setName("ADMIN");
        spinnerList.add(spinnerItem);
        for(UserRole userRole : DBHelper.getDaoSession().getUserRoleDao().loadAll()){

            SpinnerItem spnrItem = new SpinnerItem();
            spnrItem.setId(userRole.getId());
            spnrItem.setName(userRole.getRoleName());
            spinnerList.add(spnrItem);
        }

        spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerList);
        spnrUserRole.setAdapter(spinnerAdapter);
        spnrUserRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    spnrUserRoleId = 0;
                }else{
                    spnrUserRoleId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //end of populate spinner


        if(!getArguments().isEmpty()){
            User user = DBHelper.getDaoSession().getUserDao().load(Long.parseLong(getArguments().getString("txtId")));
            txtEmployeeId.setText(user.getId() + "");
            editEmail.setText(user.getEmail());
            editFirstName.setText(user.getFirstName());
            editLastName.setText(user.getLastName());
            cbxActive.setChecked(user.getDeleted() == null);
            imageEmployee.setImageBitmap(ImageConverter.bytesToBitmap(user.getImage()));
            editEmail.setEnabled(false);
            int counter = 1;
            if(user.getUserRoleId() > 0){
                for(UserRole userRole : DBHelper.getDaoSession().getUserRoleDao().loadAll()){
                    if(userRole.getId() == user.getUserRoleId()){
                        spnrUserRole.setSelection(counter);
                        break;
                    }
                    counter++;
                }
            }else{
                spnrUserRole.setSelection(0);
            }
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
                imageEmployee.setImageResource(R.drawable.noimage);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                boolean isSave = txtEmployeeId.getText().toString().trim().equals("");
                passwordCode = getPasswordCode();

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


                if(isValid){

                    if(isSave){

                        if(ConnectionHelper.isNetworkAvailable(getActivity(), GlobalConstants.getConnectivityTypes())){

                            if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                                    .where(UserDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase()))
                                    .count() > 0){
                                editEmail.setError("Email already exist in database");
                                return;
                            }

                            if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                                    .where(UserDao.Properties.FirstName.eq(editFirstName.getText().toString().trim().toUpperCase()))
                                    .count() > 0){
                                editFirstName.setError("First name already exist in database");
                                return;
                            }

                            bitmap = ((BitmapDrawable) imageEmployee.getDrawable()).getBitmap();
                            User user = new User();
                            user.setCreated(new Date());
                            user.setEmail(editEmail.getText().toString().trim().toUpperCase());
                            user.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                            user.setLastName(editLastName.getText().toString().trim().toUpperCase());
                            user.setRemarks(editRemarks.getText().toString().trim().toUpperCase());
                            user.setPasswordCode(passwordCode);
                            user.setUserRoleId(spnrUserRoleId);

                            user.setImage(ImageConverter.bitmapToBytes(bitmap));
                            if(cbxActive.isChecked()){
                                user.setDeleted(null);
                            }else{
                                user.setDeleted(new Date());
                            }
                            AsyncCheckEmail asyncCheckEmail = new AsyncCheckEmail(getActivity(), user);
                            asyncCheckEmail.execute(editEmail.getText().toString().trim());
                            btnSave.setEnabled(false);
                            //((ProductFormFragment.OnTransactionFinish)getActivity()).refresh();

                        }else{
                            Toast.makeText(getActivity(), "Device is not Connected to the internet", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        //for editing

                        User user = DBHelper.getDaoSession().getUserDao().load(Long.parseLong(txtEmployeeId.getText().toString()));
                        User user2 = null;
                        if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                                .where(UserDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase()))
                                .count() != 0){
                            user2 = DBHelper.getDaoSession().getUserDao().queryBuilder()
                                    .where(UserDao.Properties.Email.eq(editEmail.getText().toString().trim().toUpperCase())).list().get(0);
                        }

                        if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                                .where(UserDao.Properties.FirstName.eq(editFirstName.getText().toString().trim().toUpperCase()))
                                .count() != 0){
                            user2 = DBHelper.getDaoSession().getUserDao().queryBuilder()
                                    .where(UserDao.Properties.FirstName.eq(editFirstName.getText().toString().trim().toUpperCase())).list().get(0);
                        }

                        //check if product name exist and Id is not the same
                        if(user2 != null){
                            if(user.getId() != user2.getId()){
                                editEmail.setError("Email already exist in database");
                                return;
                            }
                        }

                        if(user2 != null){
                            if(user.getId() != user2.getId()){
                                editFirstName.setError("First name already exist in database");
                                return;
                            }
                        }

                        bitmap = ((BitmapDrawable) imageEmployee.getDrawable()).getBitmap();
                        User userEdit = DBHelper.getDaoSession().getUserDao().load(Long.parseLong(txtEmployeeId.getText().toString().trim()));
                        userEdit.setEmail(editEmail.getText().toString().trim().toUpperCase());
                        userEdit.setFirstName(editFirstName.getText().toString().trim().toUpperCase());
                        userEdit.setLastName(editLastName.getText().toString().trim().toUpperCase());
                        userEdit.setRemarks(editRemarks.getText().toString().trim().toUpperCase());
                        userEdit.setImage(ImageConverter.bitmapToBytes(bitmap));
                        userEdit.setUserRoleId(spnrUserRoleId);
                        if(cbxActive.isChecked()){
                            userEdit.setDeleted(null);
                        }else{
                            userEdit.setDeleted(new Date());
                        }
                        DBHelper.getDaoSession().getUserDao().update(userEdit);

                        Toast.makeText(getActivity(), "Employee has been updated", Toast.LENGTH_SHORT).show();
                        ((NavigationActivity)getActivity()).refreshList();
                        dismiss();
                    }

                }

            }
        });

    }

    public void setEmailInvalid(boolean emailExist){
        if(emailExist){
            editEmail.setError("Email already exist in server database");
            btnSave.setEnabled(true);
        }else{
            Toast.makeText(getActivity(), "Employee has been saved", Toast.LENGTH_LONG).show();
            ((NavigationActivity)getActivity()).refreshList();
            dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageEmployee.setImageBitmap(bitmap);
            } catch (IOException e) {
                //Toast toast = Toast.makeText(getActivity(),"Exception", Toast.LENGTH_LONG);
                //toast.show();
                e.printStackTrace();
            }
        }
    }

    public String getPasswordCode(){

        String passCode;

        while (true) {

            passCode = generatePassCode();

            //checks if password code was used on offline database already
            if(DBHelper.getDaoSession().getUserDao().queryBuilder()
                    .where(UserDao.Properties.PasswordCode.eq(passCode)).list().size() <= 0
                    ) {
                break;
            }

        }

        return passCode;
    }

    public String generatePassCode(){
        String result = "";

        int first = (int) (Math.random() * 10);
        int second = (int) (Math.random() * 10);
        int third = (int) (Math.random() * 10);
        int fourth = (int) (Math.random() * 10);

        result += (first+"") + (second+"") + (third+"") + (fourth+"");

        return result;
    }


}

