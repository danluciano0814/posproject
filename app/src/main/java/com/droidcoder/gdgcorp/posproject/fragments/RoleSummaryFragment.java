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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.Adapter.EmployeeSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.Adapter.RoleSummaryAdapter;
import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.CategoryDao;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRole;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRoleDao;
import com.droidcoder.gdgcorp.posproject.navfragments.BaseFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/28/2017.
 */

public class RoleSummaryFragment extends BaseFragment {

    @BindView(R.id.btnReturn)ImageView btnReturn;
    @BindView(R.id.btnCreate)Button btnCreate;
    @BindView(R.id.editRoleName)EditText editRoleName;
    @BindView(R.id.lv_role_summary)ListView lvRoleSummary;

    InputMethodManager imm;

    RoleSummaryAdapter roleSummaryAdapter;
    List<UserRole> userRoleList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        refreshList();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationActivity)getActivity()).showEmployeeMaintenance();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editRoleName.getWindowToken(), 0);
                boolean isValid = true;

                if(editRoleName.getText().toString().trim().equalsIgnoreCase("")){
                    editRoleName.setError("Role name is required");
                    isValid = false;
                }

                if(editRoleName.getText().toString().trim().equalsIgnoreCase("ADMIN")){
                    editRoleName.setError("Role name already exist in database");
                    isValid = false;
                }

                if(isValid){
                    if(DBHelper.getDaoSession().getUserRoleDao().queryBuilder()
                            .where(UserRoleDao.Properties.RoleName.eq(editRoleName.getText().toString().trim().toUpperCase()))
                            .count() > 0){
                        editRoleName.setError("Role name already exist in database");
                        return;
                    }

                    UserRole userRole = new UserRole();
                    userRole.setRoleName(editRoleName.getText().toString().trim().toUpperCase());
                    userRole.setCreated(new Date());
                    userRole.setDeleted(null);
                    DBHelper.getDaoSession().getUserRoleDao().insert(userRole);

                    editRoleName.setText("");
                    Toast.makeText(getActivity(), "User Role has been saved", Toast.LENGTH_LONG).show();
                    refreshList();
                }

            }
        });

    }

    public void refreshList(){
        userRoleList = DBHelper.getDaoSession().getUserRoleDao().queryBuilder()
                .orderAsc(UserRoleDao.Properties.RoleName).list();
        roleSummaryAdapter = new RoleSummaryAdapter(getActivity(), userRoleList);
        lvRoleSummary.setAdapter(roleSummaryAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
