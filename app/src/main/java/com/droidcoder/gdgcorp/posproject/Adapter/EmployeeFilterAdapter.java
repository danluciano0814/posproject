package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 4/19/2017.
 */

public class EmployeeFilterAdapter extends BaseAdapter {

    List<User> userList;
    Context context;

    public EmployeeFilterAdapter(Context context, List<User> userList){
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
        final Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_employee_filter, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.employeeId.setText(userList.get(position).getId().toString());
        holder.employeeName.setText(userList.get(position).getFirstName().toString().toUpperCase() + " " + userList.get(position).getLastName().toString().toUpperCase());
        holder.email.setText(userList.get(position).getEmail());
        holder.role.setText(userList.get(position).getUserRoleId() > 0 ?
                DBHelper.getDaoSession().getUserRoleDao().load(userList.get(position).getUserRoleId()).getRoleName() : "ADMIN");

        if(userList.get(position).getDeleted() == null){
            holder.cbxActive.setChecked(true);
            holder.employeeItem.setBackground(context.getResources().getDrawable(R.drawable.line_below));
        }else{
            holder.cbxActive.setChecked(false);
            holder.employeeItem.setBackground(null);
        }

        holder.employeeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cbxActive.isChecked()){
                    holder.cbxActive.setChecked(false);
                    userList.get(position).setDeleted(new Date());
                    holder.employeeItem.setBackground(null);

                }else{
                    holder.cbxActive.setChecked(true);
                    userList.get(position).setDeleted(null);
                    holder.employeeItem.setBackground(context.getResources().getDrawable(R.drawable.line_below));
                }
            }
        });

        /*holder.employeeItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                }

                return true;
            }
        });*/

        return view;
    }

    public List<User> getUserList(){
        return userList;
    }

    public void setUserList(List<User> userList){
        this.userList = userList;
    }

    class Holder{

        TextView employeeId;
        TextView employeeName;
        TextView role;
        TextView email;
        CheckBox cbxActive;
        LinearLayout employeeItem;


        public Holder(View view){
            employeeItem = (LinearLayout) view.findViewById(R.id.employeeItem);
            employeeId = (TextView) view.findViewById(R.id.txtEmployeeId);
            employeeName = (TextView) view.findViewById(R.id.txtEmployeeName);
            role = (TextView) view.findViewById(R.id.txtRole);
            email = (TextView) view.findViewById(R.id.txtEmail);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }

}
