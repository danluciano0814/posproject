package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 1/21/2017.
 */

public class ProductFormFragment extends BaseDialogFragment{

    @BindView(R.id.editCostPrice)EditText editCostPrice;
    @BindView(R.id.editDescription)EditText editDescription;
    @BindView(R.id.editProductName)EditText editProductName;
    @BindView(R.id.editSellPrice)EditText editSellPrice;
    @BindView(R.id.editStocks)EditText editStocks;
    @BindView(R.id.txtTitle)TextView txtProductTitle;
    @BindView(R.id.spnrSubCategory)Spinner spnrSubCategory;
    @BindView(R.id.btnOpen)Button btnOpen;
    @BindView(R.id.btnDelete)Button btnDelete;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.productId)TextView txtProductId;
    @BindView(R.id.imgProduct)ImageView imageProduct;
    @BindView(R.id.cbxActive)CheckBox cbxActive;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        imageProduct.setImageResource(R.drawable.noimage);

        txtProductTitle.setText("CREATE PRODUCT");
        cbxActive.setChecked(true);

        if(!getArguments().isEmpty()){
            Product product = DBHelper.getDaoSession().getProductDao().load(Long.parseLong(getArguments().getString("productId")));
            txtProductId.setText(product.getId() + "");
            editProductName.setText(product.getName());
            editCostPrice.setText(StringConverter.doubleFormatter(product.getCostPrice()));
            editDescription.setText(product.getDescription());
            editSellPrice.setText(StringConverter.doubleFormatter(product.getSellPrice()));
            editStocks.setText(StringConverter.doubleFormatter(product.getStocks()));
            txtProductTitle.setText("UPDATE PRODUCT");
            cbxActive.setChecked(product.getDeleted() == null);
            imageProduct.setImageBitmap(ImageConverter.bytesToBitmap(product.getImage()));
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
                imageProduct.setImageResource(R.drawable.noimage);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                boolean isSave = txtProductId.getText().toString().trim().equals("");
                double quantity;

                if(editProductName.getText().toString().trim().equalsIgnoreCase("")){
                    editProductName.setError("Product Category is required");
                    isValid = false;
                }

                if(editCostPrice.getText().toString().trim().equalsIgnoreCase("")
                        || Double.parseDouble(editCostPrice.getText().toString().trim()) <= 0){
                    editCostPrice.setError("Cost price is required");
                    isValid = false;
                }

                if(editSellPrice.getText().toString().trim().equalsIgnoreCase("")
                        || Double.parseDouble(editSellPrice.getText().toString().trim()) <= 0){
                    editSellPrice.setError("Cost price is required");
                    isValid = false;
                }

                if(editStocks.getText().toString().trim().equals("")){
                    quantity = 0;
                }else{
                    quantity = Double.parseDouble(editStocks.getText().toString().trim());
                }

                if(isValid){

                    if(isSave){

                        if(DBHelper.getDaoSession().getProductDao().queryBuilder()
                                .where(ProductDao.Properties.Name.eq(editProductName.getText().toString().trim().toUpperCase()))
                                .count() > 0){
                            editProductName.setError("Product name already exist in database");
                            return;
                        }

                        bitmap = ((BitmapDrawable) imageProduct.getDrawable()).getBitmap();
                        Product product = new Product();
                        product.setCreated(new Date());
                        product.setName(editProductName.getText().toString().trim().toUpperCase());
                        product.setDescription(editDescription.getText().toString().trim());
                        product.setSellPrice(Double.parseDouble(editSellPrice.getText().toString().trim()));
                        product.setCostPrice(Double.parseDouble(editCostPrice.getText().toString().trim()));
                        product.setStocks(quantity);
                        product.setImage(ImageConverter.bitmapToBytes(bitmap));
                        if(cbxActive.isChecked()){
                            product.setDeleted(null);
                        }else{
                            product.setDeleted(new Date());
                        }
                        DBHelper.getDaoSession().insert(product);

                        Toast.makeText(getActivity(), "Product has been saved", Toast.LENGTH_LONG).show();
                        ((OnTransactionFinish)getActivity()).refresh();
                        dismiss();

                    }else{
                        //for editing

                        Product product = DBHelper.getDaoSession().getProductDao().load(Long.parseLong(txtProductId.getText().toString()));
                        Product product2 = null;
                        if(DBHelper.getDaoSession().getProductDao().queryBuilder()
                                .where(ProductDao.Properties.Name.eq(editProductName.getText().toString().trim().toUpperCase()))
                                .count() != 0){
                            product2 = DBHelper.getDaoSession().getProductDao().queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(editProductName.getText().toString().trim().toUpperCase())).list().get(0);
                        }

                        //check if product name exist and Id is not the same
                        if(product2 != null){
                            if(product.getId() != product2.getId()){
                                editProductName.setError("Category name already exist in database");
                                return;
                            }
                        }

                        bitmap = ((BitmapDrawable) imageProduct.getDrawable()).getBitmap();
                        Product productEdit = DBHelper.getDaoSession().getProductDao().load(Long.parseLong(txtProductId.getText().toString().trim()));
                        productEdit.setName(editProductName.getText().toString().trim().toUpperCase());
                        productEdit.setDescription(editDescription.getText().toString().trim());
                        productEdit.setSellPrice(Double.parseDouble(editSellPrice.getText().toString().trim()));
                        productEdit.setCostPrice(Double.parseDouble(editCostPrice.getText().toString().trim()));
                        productEdit.setStocks(quantity);
                        productEdit.setImage(ImageConverter.bitmapToBytes(bitmap));
                        if(cbxActive.isChecked()){
                            productEdit.setDeleted(null);
                        }else{
                            productEdit.setDeleted(new Date());
                        }
                        DBHelper.getDaoSession().update(productEdit);

                        Toast.makeText(getActivity(), "Product has been updated", Toast.LENGTH_SHORT).show();
                        ((OnTransactionFinish)getActivity()).refresh();
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
                imageProduct.setImageBitmap(bitmap);
            } catch (IOException e) {
                //Toast toast = Toast.makeText(getActivity(),"Exception", Toast.LENGTH_LONG);
                //toast.show();
                e.printStackTrace();
            }
        }
    }

    public interface OnTransactionFinish{
        void refresh();
    }
}
