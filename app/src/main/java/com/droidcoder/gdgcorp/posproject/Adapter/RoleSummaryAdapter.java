package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.NavigationActivity;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.UserRole;
import com.droidcoder.gdgcorp.posproject.fragments.TransferRoleFragment;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 3/29/2017.
 */

public class RoleSummaryAdapter extends BaseAdapter {

    List<UserRole> userRoleList;
    Context context;

    public RoleSummaryAdapter(Context context, List<UserRole> userRoleList){
        this.userRoleList = userRoleList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userRoleList.size();
    }

    @Override
    public Object getItem(int position) {
        return userRoleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final RoleSummaryAdapter.Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_role_summary, parent, false);
            holder = new RoleSummaryAdapter.Holder(view);
            view.setTag(holder);
        }else{
            holder = (RoleSummaryAdapter.Holder) view.getTag();
        }
        holder.roleId.setText(userRoleList.get(position).getId().toString());
        holder.roleName.setText(userRoleList.get(position).getRoleName());
        holder.cbxSales.setChecked(userRoleList.get(position).getAllowSales());
        holder.cbxInvoice.setChecked(userRoleList.get(position).getAllowInvoice());
        holder.cbxReport.setChecked(userRoleList.get(position).getAllowReport());
        holder.cbxInventory.setChecked(userRoleList.get(position).getAllowInventory());
        holder.cbxCustomer.setChecked(userRoleList.get(position).getAllowCustomer());
        holder.cbxEmployee.setChecked(userRoleList.get(position).getAllowEmployee());
        holder.cbxData.setChecked(userRoleList.get(position).getAllowData());
        holder.cbxStorage.setChecked(userRoleList.get(position).getAllowStorage());
        holder.cbxSetting.setChecked(userRoleList.get(position).getAllowSetting());

        holder.cbxSales.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxSales.isChecked()){
                        holder.cbxSales.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowSales(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxSales.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowSales(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxInvoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxInvoice.isChecked()){
                        holder.cbxInvoice.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowInvoice(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxInvoice.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowInvoice(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxReport.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxReport.isChecked()){
                        holder.cbxReport.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowReport(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxReport.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowReport(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxInventory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxInventory.isChecked()){
                        holder.cbxInventory.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowInventory(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxInventory.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowInventory(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxCustomer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxCustomer.isChecked()){
                        holder.cbxCustomer.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowCustomer(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxCustomer.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowCustomer(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxEmployee.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxEmployee.isChecked()){
                        holder.cbxEmployee.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowEmployee(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxEmployee.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowEmployee(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxData.isChecked()){
                        holder.cbxData.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowData(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxData.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowData(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxStorage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxStorage.isChecked()){
                        holder.cbxStorage.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowStorage(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxStorage.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowStorage(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.cbxSetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.cbxSetting.isChecked()){
                        holder.cbxSetting.setChecked(false);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowSetting(false);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);

                    }else{
                        holder.cbxSetting.setChecked(true);
                        UserRole userRole = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                        userRole.setAllowSetting(true);
                        DBHelper.getDaoSession().getUserRoleDao().update(userRole);
                    }
                }

                return true;
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId()).getUsers().size() > 0){

                    TransferRoleFragment transferRoleFragment = new TransferRoleFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("roleId", userRoleList.get(position).getId());
                    transferRoleFragment.setArguments(bundle);
                    transferRoleFragment.show(((NavigationActivity) context).getSupportFragmentManager(), "transferRole");

                }else{
                    UserRole role = DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Warning");
                    alertDialogBuilder
                            .setMessage("Are you sure you want to delete (" + role.getRoleName() + ")?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DBHelper.getDaoSession().getUserRoleDao().delete(DBHelper.getDaoSession().getUserRoleDao().load(userRoleList.get(position).getId()));
                                    Toast.makeText(context, "Role successfully deleted", Toast.LENGTH_LONG).show();
                                    ((NavigationActivity)context).refreshList();

                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }
        });

        return view;
    }

    class Holder{

        TextView roleId;
        TextView roleName;
        CheckBox cbxSales;
        CheckBox cbxInvoice;
        CheckBox cbxReport;
        CheckBox cbxInventory;
        CheckBox cbxCustomer;
        CheckBox cbxEmployee;
        CheckBox cbxData;
        CheckBox cbxStorage;
        CheckBox cbxSetting;
        ImageView btnDelete;


        public Holder(View view){
            roleId = (TextView) view.findViewById(R.id.txtRoleId);
            roleName = (TextView) view.findViewById(R.id.txtRoleName);
            cbxSales = (CheckBox) view.findViewById(R.id.cbxSales);
            cbxInvoice = (CheckBox) view.findViewById(R.id.cbxInvoice);
            cbxReport = (CheckBox) view.findViewById(R.id.cbxReport);
            cbxInventory = (CheckBox) view.findViewById(R.id.cbxInventory);
            cbxCustomer = (CheckBox) view.findViewById(R.id.cbxCustomer);
            cbxEmployee = (CheckBox) view.findViewById(R.id.cbxEmployee);
            cbxData = (CheckBox) view.findViewById(R.id.cbxData);
            cbxStorage = (CheckBox) view.findViewById(R.id.cbxStorage);
            cbxSetting = (CheckBox) view.findViewById(R.id.cbxSetting);
            btnDelete = (ImageView) view.findViewById(R.id.btnDelete);
        }
    }

}
