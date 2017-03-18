package com.droidcoder.gdgcorp.posproject.navfragments;

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
import com.droidcoder.gdgcorp.posproject.Adapter.CustomerSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.CustomerDao;
import com.droidcoder.gdgcorp.posproject.fragments.CustomerFormFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/19/2017.
 */

public class CustomerFragment extends BaseFragment {

    SearchView searchView;
    @BindView(R.id.lv_customer_summary)ListView lvCustomerSummary;
    @BindView(R.id.btnCreate)Button btnCreate;

    CustomerSummaryAdapter customerSummaryAdapter;
    List<Customer> customerList;
    CustomerFormFragment customerFormFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        customerList = DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                .orderAsc(CustomerDao.Properties.FirstName).list();

        customerSummaryAdapter = new CustomerSummaryAdapter(getActivity(), customerList);
        lvCustomerSummary.setAdapter(customerSummaryAdapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerFormFragment = new CustomerFormFragment();
                Bundle bundle = new Bundle();
                customerFormFragment.setArguments(bundle);
                customerFormFragment.show(getActivity().getSupportFragmentManager(), "customer_form");
            }
        });

        lvCustomerSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customerFormFragment = new CustomerFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("customerId",((TextView)view.findViewById(R.id.txtCustomerId)).getText().toString());
                customerFormFragment.setArguments(bundle);
                customerFormFragment.show(getActivity().getSupportFragmentManager(), "customer_form");
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
        customerList = DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                .orderAsc(CustomerDao.Properties.FirstName).list();
        lvCustomerSummary.setAdapter(new CustomerSummaryAdapter(getActivity(), customerList));
    }

    public void searchProduct(String value){
        customerList = DBHelper.getDaoSession().getCustomerDao().queryBuilder()
                .where(CustomerDao.Properties.FirstName.like("%"+value+"%"))
                .orderAsc(CustomerDao.Properties.FirstName).list();
        lvCustomerSummary.setAdapter(new CustomerSummaryAdapter(getActivity(), customerList));
    }

}
