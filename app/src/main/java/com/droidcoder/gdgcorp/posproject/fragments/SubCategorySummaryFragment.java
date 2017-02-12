package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.CategorySummaryAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.SubCategorySummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategory;
import com.droidcoder.gdgcorp.posproject.dataentity.SubCategoryDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/7/2017.
 */

public class SubCategorySummaryFragment extends BaseFragment {

    InputMethodManager imm;
    SearchView searchView;
    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.editSubCategoryName)EditText editSubCategoryName;
    @BindView(R.id.lv_subcategory_summary)ListView lvSubCategory;
    @BindView(R.id.spnrCategory)Spinner spnrCategory;

    SubCategorySummaryAdapter subCategorySummaryAdapter;
    List<SubCategory> subCategoryList;
    SpinnerAdapter spinnerAdapter;
    long spnrCategoryId = 0;

    SubCategoryFormFragment subCategoryFormFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        populateListView();
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //populate spinner category
        List<SpinnerItem> spinnerList = new ArrayList<>();
        SpinnerItem spinnerItem = new SpinnerItem();
        spinnerItem.setId(0);
        spinnerItem.setName("ALL CATEGORY");
        spinnerList.add(spinnerItem);
        for(Category category : DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                .orderAsc(CategoryDao.Properties.Name).list()){

            SpinnerItem spnrItem = new SpinnerItem();
            spnrItem.setId(category.getId());
            spnrItem.setName(category.getName());
            spinnerList.add(spnrItem);
        }

        spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerList);
        spnrCategory.setAdapter(spinnerAdapter);
        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnrCategoryId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());

                if(position == 0){
                    subCategoryList = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                            .orderAsc(SubCategoryDao.Properties.Name).list();
                    subCategorySummaryAdapter = new SubCategorySummaryAdapter(getActivity(), subCategoryList);
                    lvSubCategory.setAdapter(subCategorySummaryAdapter);
                }else{
                    subCategoryList = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                            .where(SubCategoryDao.Properties.CategoryId.eq(spnrCategoryId))
                            .orderAsc(SubCategoryDao.Properties.Name).list();
                    subCategorySummaryAdapter = new SubCategorySummaryAdapter(getActivity(), subCategoryList);
                    lvSubCategory.setAdapter(subCategorySummaryAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //end of populate spinner

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editSubCategoryName.getWindowToken(), 0);
                boolean isValid = true;

                if(editSubCategoryName.getText().toString().trim().equalsIgnoreCase("")){
                    editSubCategoryName.setError("Category name is required");
                    isValid = false;
                }

                if(isValid){
                    if(DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                            .where(SubCategoryDao.Properties.Name.eq(editSubCategoryName.getText().toString().trim().toUpperCase()))
                            .count() > 0){
                        editSubCategoryName.setError("Category name already exist in database");
                        return;
                    }
                    SubCategory subCategory = new SubCategory();
                    subCategory.setName(editSubCategoryName.getText().toString().trim().toUpperCase());
                    subCategory.setCreated(new Date());
                    subCategory.setDeleted(null);
                    if(spnrCategoryId == 0){
                        subCategory.setCategoryId(spnrCategoryId);
                    }else{
                        subCategory.setCategoryId(spnrCategoryId);
                    }
                    DBHelper.getDaoSession().getSubCategoryDao().insert(subCategory);

                    editSubCategoryName.setText("");
                    Toast.makeText(getActivity(), "Sub Category has been saved", Toast.LENGTH_LONG).show();
                    populateListView();

                }
            }
        });

        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCategoryFormFragment = new SubCategoryFormFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("subCategoryId", Long.parseLong(((TextView)view.findViewById(R.id.txtSubCategoryId)).getText().toString()));
                subCategoryFormFragment.setArguments(bundle);
                subCategoryFormFragment.show(getActivity().getSupportFragmentManager(), "subCategoryForm");
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.inventory_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchSubCategory(query);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchSubCategory(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_search){
            Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateListView(){
        subCategoryList = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                .orderAsc(SubCategoryDao.Properties.Name).list();
        subCategorySummaryAdapter = new SubCategorySummaryAdapter(getActivity(), subCategoryList);
        lvSubCategory.setAdapter(subCategorySummaryAdapter);
        spnrCategory.setSelection(0);
    }

    public void refreshForm(boolean isAdd){
        populateListView();
        if(subCategoryFormFragment != null){
            if(isAdd){
                subCategoryFormFragment.populateProductAttached();
            }else{
                subCategoryFormFragment.populateProductAvailable();
            }
        }

    }

    public void searchSubCategory(String value){
        subCategoryList = DBHelper.getDaoSession().getSubCategoryDao().queryBuilder()
                .where(SubCategoryDao.Properties.Name.like("%"+value+"%"))
                .orderAsc(SubCategoryDao.Properties.Name).list();
        subCategorySummaryAdapter = new SubCategorySummaryAdapter(getActivity(), subCategoryList);
        lvSubCategory.setAdapter(subCategorySummaryAdapter);
        spnrCategory.setSelection(0);
    }
}
