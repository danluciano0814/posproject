package com.droidcoder.gdgcorp.posproject.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Discount;
import com.droidcoder.gdgcorp.posproject.dataentity.DiscountDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/26/2017.
 */

public class DiscountFormFragment extends BaseDialogFragment{

    @BindView(R.id.editDiscountName)EditText editDiscountName;
    @BindView(R.id.editDescription)EditText editDescription;
    @BindView(R.id.editValue)EditText editValue;
    @BindView(R.id.txtTitle)TextView txtDiscountTitle;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.discountId)TextView txtDiscountId;
    @BindView(R.id.cbxActive)CheckBox cbxActive;
    @BindView(R.id.cbxPercentage)CheckBox cbxPercentage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);
        txtDiscountTitle.setText("CREATE DISCOUNT");
        cbxActive.setChecked(true);

        if(!getArguments().isEmpty()){
            Discount discount = DBHelper.getDaoSession().getDiscountDao().load(Long.parseLong(getArguments().getString("discountId")));
            txtDiscountId.setText(discount.getId() + "");
            editDiscountName.setText(discount.getName());
            editDescription.setText(discount.getDescription());
            txtDiscountTitle.setText("UPDATE DISCOUNT");
            cbxActive.setChecked(discount.getDeleted() == null);
            cbxPercentage.setChecked(discount.getIsPercentage());
            editValue.setText(StringConverter.doubleFormatter(discount.getDiscountValue()));
        }

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                boolean isSave = txtDiscountId.getText().toString().trim().equals("");
                double value;

                if(editDiscountName.getText().toString().trim().equalsIgnoreCase("")){
                    editDiscountName.setError("Discount name is required");
                    isValid = false;
                }

                if(cbxPercentage.isChecked() && Double.parseDouble(editValue.getText().toString().trim()) > 100){
                    editValue.setError("Percentage max is 100");
                    isValid = false;
                }

                if(editValue.getText().toString().trim().equals("")){
                    value = 0;
                }else{
                    value = Double.parseDouble(editValue.getText().toString().trim());
                }

                if(isValid){

                    if(isSave){

                        if(DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                                .where(DiscountDao.Properties.Name.eq(editDiscountName.getText().toString().trim().toUpperCase()))
                                .count() > 0){
                            editDiscountName.setError("Discount name already exist in database");
                            return;
                        }

                        Discount discount = new Discount();
                        discount.setCreated(new Date());
                        discount.setName(editDiscountName.getText().toString().trim().toUpperCase());
                        discount.setDescription(editDescription.getText().toString().trim());

                        if(cbxActive.isChecked()){
                            discount.setDeleted(null);
                        }else{
                            discount.setDeleted(new Date());
                        }

                        if(cbxPercentage.isChecked()){
                            discount.setIsPercentage(true);
                        }else{
                            discount.setIsPercentage(false);
                        }

                        discount.setDiscountValue(value);
                        DBHelper.getDaoSession().getDiscountDao().insert(discount);

                        Toast.makeText(getActivity(), "Product has been saved", Toast.LENGTH_LONG).show();
                        ((ProductFormFragment.OnTransactionFinish)getActivity()).refresh();
                        dismiss();

                    }else{
                        //for editing

                        Discount discount = DBHelper.getDaoSession().getDiscountDao().load(Long.parseLong(txtDiscountId.getText().toString()));
                        Discount discount2 = null;
                        if(DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                                .where(DiscountDao.Properties.Name.eq(editDiscountName.getText().toString().trim().toUpperCase()))
                                .count() != 0){
                            discount2 = DBHelper.getDaoSession().getDiscountDao().queryBuilder()
                                    .where(DiscountDao.Properties.Name.eq(editDiscountName.getText().toString().trim().toUpperCase())).list().get(0);
                        }

                        //check if product name exist and Id is not the same
                        if(discount2 != null){
                            if(discount.getId() != discount2.getId()){
                                editDiscountName.setError("Discount name already exist in database");
                                return;
                            }
                        }

                        Discount discountEdit = DBHelper.getDaoSession().getDiscountDao().load(Long.parseLong(txtDiscountId.getText().toString().trim()));
                        discountEdit.setName(editDiscountName.getText().toString().trim().toUpperCase());
                        discountEdit.setDescription(editDescription.getText().toString().trim());

                        if(cbxActive.isChecked()){
                            discountEdit.setDeleted(null);
                        }else{
                            discountEdit.setDeleted(new Date());
                        }

                        if(cbxPercentage.isChecked()){
                            discountEdit.setIsPercentage(true);
                        }else{
                            discountEdit.setIsPercentage(false);
                        }

                        discountEdit.setDiscountValue(value);
                        DBHelper.getDaoSession().getDiscountDao().update(discountEdit);

                        Toast.makeText(getActivity(), "Discount has been updated", Toast.LENGTH_SHORT).show();
                        ((DiscountFormFragment.OnTransactionFinish)getActivity()).refresh();
                        dismiss();
                    }

                }

            }
        });

    }

    public interface OnTransactionFinish{
        void refresh();
    }

}
