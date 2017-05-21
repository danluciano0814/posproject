package com.droidcoder.gdgcorp.posproject.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.ProductRecyclerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubCategoryAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubCategorySummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryProductDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/8/2017.
 */

public class SubCategoryFormFragment extends BaseDialogFragment {

    @BindView(R.id.editSubCategoryName)EditText editSubCategoryName;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.cbxActive)CheckBox cbxActive;
    @BindView(R.id.spnrCategory)Spinner spnrCategory;
    @BindView(R.id.rvAttachedProduct)RecyclerView rvAttachedProduct;
    @BindView(R.id.rvAvailableProduct)RecyclerView rvAvailableProduct;
    @BindView(R.id.search)SearchView searchView;

    ProductRecyclerAdapter productAvailableAdapter;
    ProductRecyclerAdapter productAttachedAdapter;

    SpinnerAdapter spinnerAdapter;

    long subCategoryId;
    SubCategory subCategory;

    int spinnerPosition = 0;
    long spinnerCategoryId;

    InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setCancelable(false);

        if(!getArguments().isEmpty()){
            subCategoryId = getArguments().getLong("subCategoryId");
            subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryId);
        }
        populateProductAvailable();
        populateProductAttached();

        editSubCategoryName.setText(subCategory.getName());
        cbxActive.setChecked(subCategory.getDeleted()==null);

        List<SpinnerItem> spinnerList = new ArrayList<>();
        SpinnerItem spinnerItem = new SpinnerItem();
        spinnerItem.setId(0);
        spinnerItem.setName("-N/A-");
        spinnerList.add(spinnerItem);
        int counter = 1;
        for(Category category : DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                .orderAsc(CategoryDao.Properties.Name).list()){

            SpinnerItem spnrItem = new SpinnerItem();
            spnrItem.setId(category.getId());
            spnrItem.setName(category.getName());
            spinnerList.add(spnrItem);

            if(subCategory.getCategoryId() != 0) {
                if (category.getName().equalsIgnoreCase(DBHelper.getDaoSession().getCategoryDao().load(subCategory.getCategoryId()).getName())) {
                    spinnerPosition = counter;
                }
            }
            counter++;
        }
        spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerList);
        spnrCategory.setAdapter(spinnerAdapter);

        if(spinnerPosition > 0){
            spnrCategory.setSelection(spinnerPosition);
        }

        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategoryId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(editSubCategoryName.getWindowToken(), 0);
                boolean isValid = true;

                if(editSubCategoryName.getText().toString().trim().equalsIgnoreCase("")){
                    editSubCategoryName.setError("Sub Category name is required");
                    isValid = false;
                }

                SubCategory subCategory = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryId);
                SubCategory subCategory2 = null;

                if(isValid){
                    if(DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                            .where(SubCategoryDao.Properties.Name.eq(editSubCategoryName.getText().toString().trim().toUpperCase()))
                            .count() != 0){
                        subCategory2 = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                                .where(SubCategoryDao.Properties.Name.eq(editSubCategoryName.getText().toString().trim().toUpperCase())).list().get(0);
                    }

                    //check if product name exist and Id is not the same
                    if(subCategory2 != null){
                        if(subCategory.getId() != subCategory2.getId()){
                            editSubCategoryName.setError("Sub Category name already exist in database");
                            return;
                        }
                    }

                    SubCategory subCategoryEdit = DBHelper.getDaoSession().getSubCategoryDao().load(subCategoryId);
                    subCategoryEdit.setName(editSubCategoryName.getText().toString().trim().toUpperCase());
                    subCategoryEdit.setCategoryId(spinnerCategoryId);
                    if(cbxActive.isChecked()){
                        subCategoryEdit.setDeleted(null);
                    }else{
                        subCategoryEdit.setDeleted(new Date());
                    }
                    DBHelper.getDaoSession().getSubCategoryDao().update(subCategoryEdit);

                    Toast.makeText(getActivity(), "Sub Category has been updated", Toast.LENGTH_LONG).show();
                    ((SubCategoryAdapter.OnSubCategoryRemove)getActivity()).onRefreshSubCategory();
                    dismiss();

                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print


                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchProduct(query);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                searchProduct(s);
                return false;
            }
        });

    }

    public void populateProductAttached(){

        ArrayList<Long> ids = new ArrayList<>();

        List<SubCategoryProduct> subCategoryProductList = DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                .where(SubCategoryProductDao.Properties.SubCategoryId.eq(subCategoryId)).list();

        for(SubCategoryProduct subCategoryProduct : subCategoryProductList){
            ids.add(subCategoryProduct.getProductId());
        }
        List<Product> productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .where(ProductDao.Properties.Id.in(ids))
                .orderAsc(ProductDao.Properties.Name).list();

        productAttachedAdapter = new ProductRecyclerAdapter(getActivity(), productList, false, subCategoryId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAttachedProduct.setLayoutManager(layoutManager);
        rvAttachedProduct.setAdapter(productAttachedAdapter);

    }

    public void populateProductAvailable(){

        ArrayList<Long> ids = new ArrayList<>();
        List<SubCategoryProduct> subCategoryProductList = DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                .where(SubCategoryProductDao.Properties.SubCategoryId.eq(subCategoryId)).list();

        for(SubCategoryProduct subCategoryProduct : subCategoryProductList){
            ids.add(subCategoryProduct.getProductId());
        }

        List<Product> productList = DBHelper.getDaoSession().getProductDao()
                .queryBuilder().where(ProductDao.Properties.Id.notIn(ids))
                .where(ProductDao.Properties.Deleted.isNull())
                .orderAsc(ProductDao.Properties.Name).list();

        productAvailableAdapter = new ProductRecyclerAdapter(getActivity(), productList, true, subCategoryId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAvailableProduct.setLayoutManager(layoutManager);
        rvAvailableProduct.setAdapter(productAvailableAdapter);
    }

    public void searchProduct(String value){
        ArrayList<Long> ids = new ArrayList<>();
        List<SubCategoryProduct> subCategoryProductList = DBHelper.getDaoSession().getSubCategoryProductDao().queryBuilder()
                .where(SubCategoryProductDao.Properties.SubCategoryId.eq(subCategoryId)).list();

        for(SubCategoryProduct subCategoryProduct : subCategoryProductList){
            ids.add(subCategoryProduct.getProductId());
        }

        List<Product> productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .where(ProductDao.Properties.Id.notIn(ids))
                .where(ProductDao.Properties.Name.like("%"+value+"%"))
                .where(ProductDao.Properties.Deleted.isNull())
                .orderAsc(ProductDao.Properties.Name).list();

        productAvailableAdapter = new ProductRecyclerAdapter(getActivity(), productList, true, subCategoryId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAvailableProduct.setLayoutManager(layoutManager);
        rvAvailableProduct.setAdapter(productAvailableAdapter);
    }
}
