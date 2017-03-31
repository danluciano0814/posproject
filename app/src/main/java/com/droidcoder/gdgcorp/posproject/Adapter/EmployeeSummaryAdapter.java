package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Category;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 3/26/2017.
 */

public class EmployeeSummaryAdapter extends BaseAdapter {

    List<User> userList;
    Context context;

    public EmployeeSummaryAdapter(Context context, List<User> userList){
        this.userList = userList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final EmployeeSummaryAdapter.Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_employee_summary, parent, false);
            holder = new EmployeeSummaryAdapter.Holder(view);
            view.setTag(holder);
        }else{
            holder = (EmployeeSummaryAdapter.Holder) view.getTag();
        }
        holder.employeeId.setText(userList.get(position).getId().toString());
        holder.employeeName.setText(userList.get(position).getFirstName() + " " + userList.get(position).getLastName());
        holder.role.setText(userList.get(position).getUserRoleId() + "");
        holder.email.setText(userList.get(position).getEmail());
        holder.role.setText(userList.get(position).getUserRoleId() > 0 ?
                DBHelper.getDaoSession().getUserRoleDao().load(userList.get(position).getUserRoleId()).getRoleName() : "ADMIN");

        if(userList.get(position).getDeleted() == null){
            holder.cbxActive.setChecked(true);
        }else{
            holder.cbxActive.setChecked(false);
        }

        holder.cbxActive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxActive.isChecked()){
                        holder.cbxActive.setChecked(false);
                        User user = DBHelper.getDaoSession().getUserDao().load(userList.get(position).getId());
                        user.setDeleted(new Date());
                        DBHelper.getDaoSession().getUserDao().update(user);

                    }else{
                        holder.cbxActive.setChecked(true);
                        User user = DBHelper.getDaoSession().getUserDao().load(userList.get(position).getId());
                        user.setDeleted(null);
                        DBHelper.getDaoSession().getUserDao().update(user);
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView employeeId;
        TextView employeeName;
        TextView role;
        TextView email;
        CheckBox cbxActive;


        public Holder(View view){
            employeeId = (TextView) view.findViewById(R.id.txtEmployeeId);
            employeeName = (TextView) view.findViewById(R.id.txtEmployeeName);
            role = (TextView) view.findViewById(R.id.txtRole);
            email = (TextView) view.findViewById(R.id.txtEmail);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }

}
