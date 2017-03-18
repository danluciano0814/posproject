package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.ImageConverter;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by DanLuciano on 3/19/2017.
 */

public class CustomerSummaryAdapter extends BaseAdapter {

    List<Customer> customerList;
    Context context;

    public CustomerSummaryAdapter(Context context, List<Customer> customerList){
        this.customerList = customerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final CustomerSummaryAdapter.Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_customer_summary, parent, false);
            holder = new CustomerSummaryAdapter.Holder(view);
            view.setTag(holder);
        }else{
            holder = (CustomerSummaryAdapter.Holder) view.getTag();
        }
        holder.customerId.setText(customerList.get(position).getId().toString());
        holder.productName.setText(customerList.get(position).getFirstName() + " " + customerList.get(position).getLastName());
        holder.email.setText(customerList.get(position).getEmail());
        holder.contact.setText(customerList.get(position).getContact());
        holder.points.setText(StringConverter.doubleFormatter(customerList.get(position).getPoints()));
        if(customerList.get(position).getImage() != null ){
            holder.customerImage.setImageBitmap(ImageConverter.bytesToBitmap(customerList.get(position).getImage()));
        }else{
            holder.customerImage.setImageResource(R.drawable.noimage);
        }

        if(customerList.get(position).getDeleted() == null){
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
                        Product product = DBHelper.getDaoSession().getProductDao().load(customerList.get(position).getId());
                        product.setDeleted(new Date());
                        DBHelper.getDaoSession().getProductDao().update(product);
                        //((InventoryActivity)context).refresh();
                    }else{
                        holder.cbxActive.setChecked(true);
                        Product product = DBHelper.getDaoSession().getProductDao().load(customerList.get(position).getId());
                        product.setDeleted(null);
                        DBHelper.getDaoSession().getProductDao().update(product);
                        //((InventoryActivity)context).refresh();
                    }
                }

                return true;
            }
        });

        return view;
    }

    class Holder{

        TextView customerId;
        TextView productName;
        TextView email;
        TextView contact;
        TextView points;
        ImageView customerImage;
        CheckBox cbxActive;


        public Holder(View view){
            customerId = (TextView) view.findViewById(R.id.txtCustomerId);
            productName = (TextView) view.findViewById(R.id.txtCustomerName);
            email = (TextView) view.findViewById(R.id.txtEmail);
            contact = (TextView) view.findViewById(R.id.txtContact);
            points = (TextView) view.findViewById(R.id.txtPoints);
            customerImage = (ImageView) view.findViewById(R.id.imageCustomer);
            cbxActive = (CheckBox) view.findViewById(R.id.cbxActive);

        }
    }

}
