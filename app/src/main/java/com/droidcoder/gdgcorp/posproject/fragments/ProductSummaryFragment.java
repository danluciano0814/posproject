package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.ProductSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.dataentity.ProductDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 1/18/2017.
 */

public class ProductSummaryFragment extends BaseFragment {

    SearchView searchView;
    @BindView(R.id.lv_product_summary)ListView lvProductSummary;
    @BindView(R.id.btnCreate)Button btnCreate;

    ProductSummaryAdapter productSummaryAdapter;
    List<Product> productList;
    ProductFormFragment productFormFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .orderAsc(ProductDao.Properties.Name).list();

        productSummaryAdapter = new ProductSummaryAdapter(getActivity(),productList);
        lvProductSummary.setAdapter(productSummaryAdapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productFormFragment = new ProductFormFragment();
                Bundle bundle = new Bundle();
                productFormFragment.setArguments(bundle);
                productFormFragment.show(getActivity().getSupportFragmentManager(), "product_form");
            }
        });

        lvProductSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productFormFragment = new ProductFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("productId",((TextView)view.findViewById(R.id.txtProductId)).getText().toString());
                productFormFragment.setArguments(bundle);
                productFormFragment.show(getActivity().getSupportFragmentManager(), "product_form");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_search){
            Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshList(){
        productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .orderAsc(ProductDao.Properties.Name).list();
        lvProductSummary.setAdapter(new ProductSummaryAdapter(getActivity(),productList));
    }

    public void searchProduct(String value){
        productList = DBHelper.getDaoSession().getProductDao().queryBuilder()
                .where(ProductDao.Properties.Name.like("%"+value+"%"))
                .orderAsc(ProductDao.Properties.Name).list();
        lvProductSummary.setAdapter(new ProductSummaryAdapter(getActivity(),productList));
    }
}
