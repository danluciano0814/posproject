package com.droidcoder.gdgcorp.posproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.Adapter.SpinnerAdapter;
import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.SpinnerItem;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRole;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRoleDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DanLuciano on 3/31/2017.
 */

public class TransferRoleFragment extends BaseDialogFragment {

    @BindView(R.id.btnOk)Button btnOk;
    @BindView(R.id.btnNo)Button btnNo;
    @BindView(R.id.spnrUserRole)Spinner spnrUserRole;

    SpinnerAdapter spinnerAdapter;
    long spnrUserRoleId = 0;
    long roleId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_role, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setCancelable(false);

        if(!getArguments().isEmpty()){

            roleId = getArguments().getLong("roleId");

            //populate spinner category
            List<SpinnerItem> spinnerList = new ArrayList<>();
            SpinnerItem spinnerItem = new SpinnerItem();
            spinnerItem.setId(0);
            spinnerItem.setName("ADMIN");
            spinnerList.add(spinnerItem);

            for(UserRole userRole : DBHelper.getDaoSession().getUserRoleDao().queryBuilder()
                    .where(UserRoleDao.Properties.Id.notEq(roleId)).list()){

                SpinnerItem spnrItem = new SpinnerItem();
                spnrItem.setId(userRole.getId());
                spnrItem.setName(userRole.getRoleName());
                spinnerList.add(spnrItem);
            }

            spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerList);
            spnrUserRole.setAdapter(spinnerAdapter);
            spnrUserRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position == 0){
                        spnrUserRoleId = 0;
                    }else{
                        spnrUserRoleId = Long.parseLong(((TextView)view.findViewById(R.id.txtId)).getText().toString());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //end of populate spinner
        }

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<User> userList = new ArrayList<User>();
                UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(roleId);

                for(User user : userRole.getUsers()){
                    user.setUserRoleId(spnrUserRoleId);
                    userList.add(user);
                }

                DBHelper.getDaoSession().getUserDao().updateInTx(userList);
                DBHelper.getDaoSession().getUserRoleDao().delete(userRole);
                Toast.makeText(getActivity(), "All user roles has been transfered successfully", Toast.LENGTH_LONG).show();
                dismiss();
                ((NavigationActivity)getActivity()).refreshList();
            }
        });

    }

}
