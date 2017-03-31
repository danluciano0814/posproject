package com.droidcoder.gdgcorp.posproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.CategorySummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 2/4/2017.
 */

public class CategorySummaryFragment extends BaseFragment{

    SearchView searchView;
    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.editCategoryName)EditText editCategoryName;
    @BindView(R.id.lv_category_summary)ListView lvCategorySummary;

    CategorySummaryAdapter categorySummaryAdapter;
    CategoryFormFragment categoryFormFragment;
    InputMethodManager imm;

    List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_summary, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        populateListView();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editCategoryName.getWindowToken(), 0);
                boolean isValid = true;

                if(editCategoryName.getText().toString().trim().equalsIgnoreCase("")){
                    editCategoryName.setError("Category name is required");
                    isValid = false;
                }

                if(isValid){
                    if(DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                            .where(CategoryDao.Properties.Name.eq(editCategoryName.getText().toString().trim().toUpperCase()))
                            .count() > 0){
                        editCategoryName.setError("Category name already exist in database");
                        return;
                    }

                    Category category = new Category();
                    category.setName(editCategoryName.getText().toString().trim().toUpperCase());
                    category.setCreated(new Date());
                    category.setCategoryColor("#2196F3");
                    category.setDeleted(null);
                    DBHelper.getDaoSession().getCategoryDao().insert(category);

                    editCategoryName.setText("");
                    refreshList();
                    Toast.makeText(getActivity(), "Category has been saved", Toast.LENGTH_LONG).show();

                }
            }
        });

        lvCategorySummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryFormFragment = new CategoryFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId",((TextView)view.findViewById(R.id.txtCategoryId)).getText().toString());
                categoryFormFragment.setArguments(bundle);
                categoryFormFragment.show(getActivity().getSupportFragmentManager(), "categoryForm");
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.inventory_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Category");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchCategory(query);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchCategory(s);
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
        categoryList = DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                .orderAsc(CategoryDao.Properties.Name).list();
        categorySummaryAdapter = new CategorySummaryAdapter(getActivity(), categoryList);
        lvCategorySummary.setAdapter(categorySummaryAdapter);
    }

    public void refreshList(){
        populateListView();
        if(categoryFormFragment != null){
            categoryFormFragment.populateSubCategoryList();
        }
    }

    public void searchCategory(String value){
        categoryList = DBHelper.getDaoSession().getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Name.like("%"+value+"%"))
                .orderAsc(CategoryDao.Properties.Name).list();
        categorySummaryAdapter = new CategorySummaryAdapter(getActivity(), categoryList);
        lvCategorySummary.setAdapter(categorySummaryAdapter);
    }

}
