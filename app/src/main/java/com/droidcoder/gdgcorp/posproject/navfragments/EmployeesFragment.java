package com.droidcoder.gdgcorp.posproject.navfragments;

import android.graphics.Paint;
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

import com.droidcoder.gdgcorp.posproject.Adapter.EmployeeSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.datasystem.CurrentUser;
import com.droidcoder.gdgcorp.posproject.fragments.EmployeeFormFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/26/2017.
 */

public class EmployeesFragment extends BaseFragment {

    SearchView searchView;
    @BindView(R.id.lv_employee_summary) ListView lvEmployeeSummary;
    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.txtRole)TextView txtRole;

    EmployeeSummaryAdapter employeeSummaryAdapter;
    List<User> userList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        refreshEmployeeList();
        txtRole.setPaintFlags(txtRole.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        lvEmployeeSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeFormFragment employeeFormFragment = new EmployeeFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("txtId", ((TextView) view.findViewById(R.id.txtEmployeeId)).getText().toString());
                employeeFormFragment.setArguments(bundle);
                employeeFormFragment.show(getActivity().getSupportFragmentManager(), "employeeForm");
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeFormFragment employeeFormFragment = new EmployeeFormFragment();
                Bundle bundle = new Bundle();
                employeeFormFragment.setArguments(bundle);
                employeeFormFragment.show(getActivity().getSupportFragmentManager(), "employeeForm");
            }
        });

        txtRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationActivity)getActivity()).showRoleMaintenance();
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void refreshEmployeeList(){
        userList = DBHelper.getDaoSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Id.notEq(CurrentUser.getUser().getId()))
                .orderAsc(UserDao.Properties.FirstName).list();
        employeeSummaryAdapter = new EmployeeSummaryAdapter(getActivity(), userList);
        lvEmployeeSummary.setAdapter(employeeSummaryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.inventory_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Receipt");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchReceipt(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchReceipt(String value) {

    }


}
