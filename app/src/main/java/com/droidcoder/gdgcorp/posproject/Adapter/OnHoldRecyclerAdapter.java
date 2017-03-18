package com.droidcoder.gdgcorp.posproject.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidcoder.gdgcorp.posproject.R;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderProduct;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceipt;
import com.droidcoder.gdgcorp.posproject.dataentity.OrderReceiptDao;
import com.droidcoder.gdgcorp.posproject.dataentity.Product;
import com.droidcoder.gdgcorp.posproject.fragments.OnHoldFragment;
import com.droidcoder.gdgcorp.posproject.navactivities.SalesActivity;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.droidcoder.gdgcorp.posproject.utils.StringConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DanLuciano on 3/10/2017.
 */

public class OnHoldRecyclerAdapter extends RecyclerView.Adapter<OnHoldRecyclerAdapter.OnHoldVH> {

    LayoutInflater inflater;
    Context context;
    ArrayList<OrderReceipt> orderReceiptList;

    public OnHoldRecyclerAdapter(Context context, ArrayList<OrderReceipt> orderReceiptList){

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.orderReceiptList = orderReceiptList;
    }

    @Override
    public OnHoldVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_onhold_receipt, parent, false);
        OnHoldRecyclerAdapter.OnHoldVH OnHoldVH = new OnHoldRecyclerAdapter.OnHoldVH(view);

        return OnHoldVH;
    }

    @Override
    public void onBindViewHolder(OnHoldVH holder, final int position) {

        holder.txtReceiptId.setText(orderReceiptList.get(position).getReceiptId() + "");
        holder.txtUniqueId.setText(orderReceiptList.get(position).getReceiptIdentification());

        List<OrderProduct> orderProductList = null;

        if(DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                .where(OrderReceiptDao.Properties.ReceiptId.eq(orderReceiptList.get(position).getReceiptId()))
                .list().size() > 0) {

            orderProductList = DBHelper.getDaoSession().getOrderReceiptDao().queryBuilder()
                    .where(OrderReceiptDao.Properties.ReceiptId.eq(orderReceiptList.get(position).getReceiptId()))
                    .list().get(0).getOrderProducts();

        }

        if(orderProductList != null && orderProductList.size() > 0){

            String orders = "";
            Map<Long, Double> productMap = new HashMap<>();

            for(OrderProduct orderProduct : orderProductList){

                long key = orderProduct.getProductId();
                double quantity = orderProduct.getProductQuantity();

                if(productMap.containsKey(key)){
                    double total = productMap.get(key) + quantity;
                    productMap.put(key, total);
                }else{
                    productMap.put(key, quantity);
                }
            }

            for(Map.Entry<Long, Double> product: productMap.entrySet()){

                Product prod = DBHelper.getDaoSession().getProductDao().load(product.getKey());
                orders += StringConverter.doubleFormatter(product.getValue()) + " x   " + prod.getName() + "\n";

            }

            holder.txtOrders.setText(orders);
        }

        holder.mainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SalesActivity)context).retrieveFromOnhold(orderReceiptList.get(position).getId());
                //Toast.makeText(context, "receipt id :" + orderReceiptList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderReceiptList.size();
    }

    class OnHoldVH extends RecyclerView.ViewHolder {

        TextView txtUniqueId;
        TextView txtReceiptId;
        TextView txtCreated;
        TextView txtOrders;
        LinearLayout mainItem;


        public OnHoldVH(View itemView) {
            super(itemView);

            txtUniqueId = (TextView) itemView.findViewById(R.id.txtUniqueId);
            txtReceiptId = (TextView) itemView.findViewById(R.id.txtReceiptId);
            txtCreated = (TextView) itemView.findViewById(R.id.txtCreated);
            txtOrders = (TextView) itemView.findViewById(R.id.txtOrders);
            mainItem = (LinearLayout) itemView.findViewById(R.id.mainItem);

        }


    }
}
