package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.SubCategoryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/6/2017.
 */

public class CategoryFormFragment extends BaseDialogFragment {

    @BindView(R.id.btnDismiss)ImageView btnDismiss;
    @BindView(R.id.btnSave)Button btnSave;
    @BindView(R.id.editCategoryName)EditText editCategoryName;
    @BindView(R.id.cbxActive)CheckBox cbxActive;
    @BindView(R.id.colorGroup)RadioGroup colorGroup;
    @BindView(R.id.rbBlue)RadioButton rbBlue;
    @BindView(R.id.rbRed)RadioButton rbRed;
    @BindView(R.id.rbGreen)RadioButton rbGreen;
    @BindView(R.id.rbPurple)RadioButton rbPurple;
    @BindView(R.id.rbOrange)RadioButton rbOrange;
    @BindView(R.id.rbTeal)RadioButton rbTeal;
    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.editSubCategoryName)EditText editSubCategoryName;
    @BindView(R.id.lvSubCategory)ListView lvSubCategory;

    long categoryId;
    SubCategoryAdapter subCategoryAdapter;
    List<SubCategory> subCategoryList;
    InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if(!getArguments().isEmpty()){
            categoryId = Long.parseLong(getArguments().getString("categoryId"));
            Category category = DBHelper.getDaoSession().getCategoryDao().load(categoryId);
            editCategoryName.setText(category.getName());
            cbxActive.setChecked(category.getDeleted()==null);

            switch(category.getCategoryColor()){
                case "#F44336":
                    rbRed.setChecked(true);
                    break;
                case "#2196F3":
                    rbBlue.setChecked(true);
                    break;
                case "#009688":
                    rbTeal.setChecked(true);
                    break;
                case "#4CAF50":
                    rbGreen.setChecked(true);
                    break;
                case "#FF9800":
                    rbOrange.setChecked(true);
                    break;
                case "#9C27B0":
                    rbPurple.setChecked(true);
                    break;
                default:
                    rbBlue.setChecked(true);
            }

            populateSubCategoryList();
        }

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //save update of category
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editCategoryName.getWindowToken(), 0);
                boolean isValid = true;
                String categoryColor = "";

                switch(colorGroup.getCheckedRadioButtonId()){
                    case R.id.rbRed:
                        categoryColor = "#F44336";
                        break;
                    case R.id.rbBlue:
                        categoryColor = "#2196F3";
                        break;
                    case R.id.rbGreen:
                        categoryColor = "#4CAF50";
                        break;
                    case R.id.rbTeal:
                        categoryColor = "#009688";
                        break;
                    case R.id.rbOrange:
                        categoryColor = "#FF9800";
                        break;
                    case R.id.rbPurple:
                        categoryColor = "#9C27B0";
                        break;
                }

                if(editCategoryName.getText().toString().trim().equalsIgnoreCase("")){
                    editCategoryName.setError("Category name is required");
                    isValid = false;
                }

                Category category = DBHelper.getDaoSession().getCategoryDao().load(categoryId);
                Category category2 = null;

                if(isValid){
                    if(DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                            .where(CategoryDao.Properties.Name.eq(editCategoryName.getText().toString().trim().toUpperCase()))
                            .count() != 0){
                        category2 = DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                                .where(CategoryDao.Properties.Name.eq(editCategoryName.getText().toString().trim().toUpperCase())).list().get(0);
                    }

                    //check if product name exist and Id is not the same
                    if(category2 != null){
                        if(category.getId() != category2.getId()){
                            editCategoryName.setError("Category name already exist in database");
                            return;
                        }
                    }

                    Category categoryEdit = DBHelper.getDaoSession().getCategoryDao().load(categoryId);
                    categoryEdit.setName(editCategoryName.getText().toString().trim().toUpperCase());
                    categoryEdit.setCategoryColor(categoryColor);
                    if(cbxActive.isChecked()){
                        categoryEdit.setDeleted(null);
                    }else{
                        categoryEdit.setDeleted(new Date());
                    }
                    DBHelper.getDaoSession().getCategoryDao().update(categoryEdit);

                    editCategoryName.setText("");
                    Toast.makeText(getActivity(), "Category has been updated", Toast.LENGTH_LONG).show();
                    ((SubCategoryAdapter.OnSubCategoryRemove)getActivity()).onRefreshSubCategory();
                    dismiss();

                }
            }
        });

        //create new sub category under this category
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editSubCategoryName.getWindowToken(), 0);
                boolean isValid = true;

                if(editSubCategoryName.getText().toString().trim().equalsIgnoreCase("")){
                    editSubCategoryName.setError("Sub Category name is required");
                    isValid = false;
                }

                if(isValid){
                    if(DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                            .where(SubCategoryDao.Properties.Name.eq(editSubCategoryName.getText().toString().trim().toUpperCase()))
                            .count() > 0){
                        editSubCategoryName.setError("Sub Category name already exist in database");
                        return;
                    }

                    SubCategory subCategory = new SubCategory();
                    subCategory.setName(editSubCategoryName.getText().toString().trim().toUpperCase());
                    subCategory.setCategoryId(categoryId);
                    subCategory.setCreated(new Date());
                    subCategory.setDeleted(null);
                    DBHelper.getDaoSession().getSubCategoryDao().insert(subCategory);

                    editSubCategoryName.setText("");
                    Toast.makeText(getActivity(), "Sub Category has been saved", Toast.LENGTH_LONG).show();
                    ((SubCategoryAdapter.OnSubCategoryRemove)getActivity()).onRefreshSubCategory();
                    populateSubCategoryList();
                }
            }
        });

    }

    public void populateSubCategoryList(){
        DBHelper.getDaoSession().getCategoryDao().load(categoryId).resetSubCategories();
        subCategoryList = DBHelper.getDaoSession().getCategoryDao().load(categoryId).getSubCategories();
        subCategoryAdapter = new SubCategoryAdapter(getActivity(), subCategoryList, categoryId);
        lvSubCategory.setAdapter(subCategoryAdapter);
    }

    public interface OnSaveFinish{
        void refreshCategory();
    }
}
