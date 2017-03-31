package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.Customer;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DanLuciano on 3/22/2017.
 */

public class InvoiceSummaryAdapter extends BaseAdapter {

    List<OrderReceipt> orderReceiptList;
    Context context;

    public InvoiceSummaryAdapter(Context context, List<OrderReceipt> orderReceiptList){
        this.orderReceiptList = orderReceiptList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orderReceiptList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderReceiptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final InvoiceSummaryAdapter.Holder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_invoice_summary, parent, false);
            holder = new InvoiceSummaryAdapter.Holder(view);
            view.setTag(holder);
        }else{
            holder = (InvoiceSummaryAdapter.Holder) view.getTag();
        }

        String customerName = "";
        String userName = "";

        if(orderReceiptList.get(position).getCustomerId() > 0){
            Customer customer = DBHelper.getDaoSession().getCustomerDao().load((orderReceiptList.get(position).getCustomerId()));
            customerName = customer.getFirstName() + " " + customer.getLastName();
        }

        if(orderReceiptList.get(position).getUserId() > 0){
            User user = DBHelper.getDaoSession().getUserDao().load((orderReceiptList.get(position).getUserId()));
            userName = user.getFirstName() + " " + user.getLastName();
        }

        holder.txtId.setText(orderReceiptList.get(position).getId().toString());
        holder.txtReceiptId.setText(orderReceiptList.get(position).getReceiptId() + "");
        holder.txtTransDate.setText(new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(orderReceiptList.get(position).getCreated()));
        holder.txtTotal.setText(StringConverter.doubleFormatter(orderReceiptList.get(position).getTotalDeductedPrice() + orderReceiptList.get(position).getServiceChargeTotal()));
        holder.txtCustomer.setText(customerName);
        holder.txtUser.setText(userName);

        return view;
    }

    class Holder{

        TextView txtId;
        TextView txtReceiptId;
        TextView txtTransDate;
        TextView txtTotal;
        TextView txtCustomer;
        TextView txtUser;


        public Holder(View view){
            txtId = (TextView) view.findViewById(R.id.txtId);
            txtReceiptId = (TextView) view.findViewById(R.id.txtReceiptId);
            txtTransDate = (TextView) view.findViewById(R.id.txtTransDate);
            txtTotal = (TextView) view.findViewById(R.id.txtTotal);
            txtCustomer = (TextView) view.findViewById(R.id.txtCustomer);
            txtUser = (TextView) view.findViewById(R.id.txtUser);
        }
    }

}
